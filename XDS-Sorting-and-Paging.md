# Sorting and Paging for ITI-18

**Status:** implemented in IPF 6.0
**Scope:** `ipf-commons-ihe-xds` — Registry Stored Query [ITI-18] and the transactions built on it

## What this is

FHIR search defines `_sort`, `_count` and paging links. IHE XDS does not: ITI-18 gives a Document
Consumer no way to ask a registry for an ordered result set, or for a window of one. A gateway
translating MHD to XDS therefore has to satisfy the FHIR contract on the consumer side, which either
costs a full result-set fetch or silently returns arbitrary order.

IPF adds both as a **bilaterally agreed extension**. It is not part of ITI-18, and IHE explicitly
discourages combining stored queries with ebRS pagination (ITI-18 §3.18.4.1.2.6). What makes this safe
to ship anyway is that it is invisible unless asked for: a query that sets neither produces byte-identical
XML to one built before the extension existed, and a registry that does not implement it ignores what it
does not understand, exactly as ITI-18 requires of any unknown parameter.

## Two typical uses

**"What has happened with my patient recently?"** A physician opens the record of someone under
long-term care. The registry holds several hundred documents; the twenty most recent answer the
question. Ordering by `-$XDSDocumentEntryCreationTime` and asking for `maxResults=20` returns them in a
single round trip, and `totalResultCount` lets the UI show how much more there is without fetching any
of it. Without the extension the gateway has to retrieve *every* match to find out which twenty are the
newest — precisely the full-result fetch the two-phase query pattern exists to avoid.

**"Show me the course of treatment."** Same patient, different question: a timeline of care rather than
of filing. The key here is `$XDSDocumentEntryServiceStartTime`, ascending — when the care happened, not
when the document reached the registry. The two diverge routinely. A discharge summary transcribed a
week after the stay, or a historical record scanned in during a migration, is recent by creation time
and old by service time. Sorting a "recent treatment" list by the wrong one puts a decade-old scanned
record at the top.

That second case is also the clearest illustration of why a sort key names a *metadata attribute*: both
questions are about the same documents and use the same filter, and only the ordering attribute
separates them.

## Sorting

The requested order lives on `StoredQuery`, so it is available on every stored query:

```java
var query = new FindDocumentsQuery();
query.setPatientId(patientId);
query.setSortOrder(new SortOrder(
        SortKey.descending("$XDSDocumentEntryCreationTime"),
        SortKey.ascending("$XDSDocumentEntryAuthorPerson"))
    .withStableTiebreaker());
```

A `SortKey` names an **XDS metadata attribute**, not a query parameter — the two are not the same
vocabulary. Creation time is *filtered* by the pair `$XDSDocumentEntryCreationTimeFrom` /
`...To` but *sorted* by the attribute itself, `$XDSDocumentEntryCreationTime`. Extension attributes are
allowed. Keys are in order of precedence: the second only decides where the first ties.

`withStableTiebreaker()` appends the entry UUID, unless it is already among the keys. Sorting alone does
not need it; **paging does**. An order that leaves ties is not a defined sequence, and a window into an
undefined sequence can repeat or skip entries even against a registry whose content never changes.

### On the wire

The order travels in one extra query slot, by default `$ipfSortOrder`, one value per key in precedence
order, with a leading `-` marking a descending one. The values use IPF's ordinary coded-list encoding, so
a registry reading them with standard slot tooling gets a list of names rather than anything
IPF-specific.

The slot name is part of the agreement with the registry and can be changed once at startup:

```java
SortOrder.setSlotName("$ipfSortOrder");   // default is SortOrder.DEFAULT_SLOT_NAME
```

It is deliberately global rather than per query: which slot carries the order is a property of the
deployment, not something a single query chooses.

## Paging

The window maps onto the ebRS 3.0 pagination attributes of `AdhocQueryRequest`, which are already in the
schema — nothing proprietary:

```java
var request = new QueryRegistry(query);
request.setStartIndex(100);
request.setMaxResults(50);
request.setRequestId("urn:uuid:6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd");
```

`maxResults = 0` is meaningful: it asks for the count alone, without any results. Whether that question
can be answered at all is up to the registry — see `totalResultCount` below, which is optional precisely
because counting can be the expensive part.

`requestId` is pure correlation — the registry echoes it in the response so a sequence of paged requests
is recognisable in logs and audit records. It is **not** a cursor: no server-side state is implied, and
IPF mints no snapshot handle.

### Window bounds

**A start index at or below zero means "from the beginning".** ebRS defaults the attribute to 0 and its
getter reports that default, so absent, zero and negative are indistinguishable on arrival and are all
read the same way. `maxResults` is still honoured, so `startIndex=-5, maxResults=50` yields the first
fifty. Negative `maxResults` likewise means unbounded, which is ebRS's own spelling of it.

**A start index past the end is rejected**, with `ValidationMessage.START_INDEX_BEYOND_END`. Answering
with an empty page would be ambiguous: the requester could not tell it from a query that simply matched
nothing, and the two mean very different things to whoever is paging. Only the registry can detect this,
since only it knows the total — so it is a registry-side rule rather than something the request validator
can enforce.

Offset zero is exempt. A search that matches nothing is a legitimate empty answer, not a window out of
range, and `startIndex=0` over an empty result set succeeds with no entries. The rule is therefore
"reject when `startIndex > 0` and `startIndex >= ` the size of the result set" -- the size the registry
knows, which is not necessarily one it reports.

### Only search queries can be paged

`QueryType.isPageable()` is true exactly for `QueryKind.SEARCH` — the `Find…` queries and the
cross-community `Fetch`. A `Get…` query returns the objects whose identifiers the requester passed in, so
the requester already knows how many there will be and a window buys nothing.

For several of them a window would not merely be useless but corrupting.
`GetSubmissionSetAndContents` returns submission sets, folders, documents **and the associations tying
them together**; cutting the lists would answer with associations whose endpoints are no longer in the
response. There is also no unit to count in — "fifty results" of a mixed set means nothing.

The request validator therefore rejects a window on a non-pageable query with
`ValidationMessage.QUERY_TYPE_NOT_PAGEABLE`. Rejecting rather than ignoring is deliberate: ignoring a
window returns a correct answer, applying one returns a corrupt one.

Ordering has no such limit. Sort keys name their object type — `$XDSFolderUniqueId` can only mean a
folder — so an order applies to the kind it names and leaves the rest alone. Sorting a `GetAll` result by
document creation time is well defined.

## The response

`QueryResponse` carries three additions, **all of them optional**:

| field | meaning |
|---|---|
| `totalResultCount` | size of the whole result set this response is a window into |
| `startIndex` | index of the first result in this response |
| `honoredSortOrder` | the order the registry says it actually applied |

`totalResultCount` is the one piece that costs nothing to *standardise* — ebRS 3.0 defines the attribute
already, so a registry may report a total with no bilateral agreement at all — and it maps directly onto
FHIR `Bundle.total`. But it can cost a great deal to *produce*, which is why nothing requires it.

Most registries authorize per document against an inbound SAML or JWT token, and the only honest total is
then the number of documents the requester is actually allowed to see, not the number that matched the
query. Producing it means running the authorization decision across the entire match set at the moment
only the first window is being returned. For a patient with thousands of documents that can cost more
than the query itself, and it is work thrown away if the consumer never asks for a second page. A
registry in that position should simply omit the attribute.

An absent total therefore stays absent rather than becoming zero: "I did not count" and "there are none"
are different answers, and a consumer must not render the first as the second.

Omitting it does not stop a registry from rejecting a start index past the end — it knows the size of its
own result set whether or not it chooses to publish it. A consumer that has no total detects the end the
usual way, by receiving a page shorter than the `maxResults` it asked for. Only a result set that is an
exact multiple of the page size leaves no short page, and there the out-of-range error is the stop
signal.

`honoredSortOrder` exists because a registry that ignores the sort slot is otherwise indistinguishable
from one that applied it — the consumer would have to assume the worst and sort again. It travels in the
ebRS response slot list, under the same slot name as the request.

## Example

A Find Documents query asking for the newest documents first, the second page of fifty:

```xml
<query:AdhocQueryRequest startIndex="100" maxResults="50"
                         id="urn:uuid:6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd">
    <query:ResponseOption returnType="ObjectRef" returnComposedObjects="true"/>
    <AdhocQuery id="urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d">
        <Slot name="$ipfSortOrder">
            <ValueList>
                <Value>('-$XDSDocumentEntryCreationTime')</Value>
                <Value>('$XDSDocumentEntryAuthorPerson')</Value>
                <Value>('$XDSDocumentEntryEntryUUID')</Value>
            </ValueList>
        </Slot>
        <Slot name="$XDSDocumentEntryPatientId">
            <ValueList>
                <Value>'p1^^^&amp;1.2.3.4&amp;ISO'</Value>
            </ValueList>
        </Slot>
        <Slot name="$XDSDocumentEntryStatus">
            <ValueList>
                <Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')</Value>
            </ValueList>
        </Slot>
    </AdhocQuery>
</query:AdhocQueryRequest>
```

The registry answers with the window, the total, and what it honoured (result objects omitted):

```xml
<query:AdhocQueryResponse startIndex="100" totalResultCount="4711"
                          status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"
                          requestId="urn:uuid:6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd">
    <rs:ResponseSlotList>
        <Slot name="$ipfSortOrder">
            <ValueList>
                <Value>('-$XDSDocumentEntryCreationTime')</Value>
                <Value>('$XDSDocumentEntryAuthorPerson')</Value>
                <Value>('$XDSDocumentEntryEntryUUID')</Value>
            </ValueList>
        </Slot>
    </rs:ResponseSlotList>
    <RegistryObjectList>
        <!-- fifty ObjectRefs -->
    </RegistryObjectList>
</query:AdhocQueryResponse>
```

A registry that does not implement the extension returns the whole, unordered result set with no
`ResponseSlotList` — a valid ITI-18 answer, and one the consumer can recognise as unordered.

## Applying an order (registry side)

A registry built on IPF does not have to interpret the attribute names itself:

```java
var comparator = SortOrderComparators.documentEntryComparator(query.getSortOrder());
if (comparator != null) {
    documentEntries.sort(comparator);
    response.setHonoredSortOrder(query.getSortOrder());
}
```

There is one comparator per kind of result — `documentEntryComparator`, `folderComparator`,
`submissionSetComparator` — because the attribute names differ by object type. `null` means none of the
keys named an attribute of that kind, which the caller should read as "leave this list alone".

Two conventions:

- **Missing values sort last, in both directions.** Reversing an ascending comparator would move them to
  the front of a descending result, so entries lacking the attribute would crowd out the ones the
  requester asked to see first.
- **Multivalued attributes compare by their first value** in metadata order — an author person, for
  instance. Well defined and stable, but arbitrary when an entry has more than one.

`supportedDocumentEntryAttributes()` and its siblings list what is understood; an attribute outside that
set is skipped rather than rejected, the way a registry ignores a query parameter it does not know.
Extension attributes are registered with `sortableBy`:

```java
Map.of("$ourOwnAttribute", SortOrderComparators.sortableBy(
    (DocumentEntry entry) -> entry.getExtraMetadata().get("urn:ours").get(0)))
```

`tutorials/xds` implements the whole registry side of this, including the acknowledgement, and is the
place to look for a worked example.

## Errors

| condition | reported as | detected by |
|---|---|---|
| window on a `Get…` query | `QUERY_TYPE_NOT_PAGEABLE` | request validator |
| sort slot holding something that names no attribute | `INVALID_SORT_ORDER` | request transformer |
| start index past the end of the result set | `START_INDEX_BEYOND_END` | the registry |

All three are ordinary `XDSMetaDataException`s, so the transaction turns them into a RegistryError rather
than an unhandled fault. The first two are caught before a query runs; the last one cannot be, because
the total is not known until it has.

## Known limits

- **Snapshot consistency.** Windowing across separate ITI-18 calls over a registry whose content changes
  produces duplicates and gaps. IPF mints no cursor: `requestId` correlates, it does not freeze. If this
  matters, keyset pagination — "the fifty after (creationTime, entryUUID)" — is stable without server
  state and composes with the total order the tiebreaker already guarantees. The correlation id and the
  response slot list are the hooks a continuation token would ride on, so adding one later is additive.
- **XCA.** An initiating gateway forwards the slots, but responding communities will not honour them, so
  order across communities stays undefined even when each community sorts.
- **Non-XDS sort keys.** Anything without an XDS metadata counterpart — FHIR's `_lastUpdated`, say —
  has to be rejected or degraded by the gateway; there is nothing for the registry to sort by.
- **Object references.** With `returnType=ObjectRef` a result carries only id and home, so nothing is
  left to sort by. A registry must order *before* projecting to references, not after.

## References

- [ITI-18 Registry Stored Query — IHE ITI TF Vol. 2](https://profiles.ihe.net/ITI/TF/Volume2/ITI-18.html)
- [Annotated StoredQuery Transaction — IHE Wiki](https://wiki.ihe.net/index.php/Annotated_StoredQuery_Transaction)
- [Handling extra XDS data — IPF documentation](https://oehf.github.io/ipf-docs/docs/ihe/xdsHandlingExtra/)
