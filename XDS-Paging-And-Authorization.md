# Paging an authorized result set

**Status:** design note, companion to [XDS-Sorting-and-Paging.md](XDS-Sorting-and-Paging.md)
**Scope:** a registry backed by a relational database that authorizes every document individually

## The situation

The sorting and paging extension says what a registry may be asked for. This note says how a particular
kind of registry answers: one that keeps document metadata in a relational database and, for every
document it is about to return, asks a policy decision point whether *this* requestor may see it. The
decision is expensive — a call out of process, a policy set to evaluate, possibly a consent document to
fetch — and it dominates the cost of the whole transaction. Everything below is about making as few of
those calls as possible.

The quantity to minimise is precise: **decisions taken for documents that do not end up in the
response.** A decision that yields a document in the answer is work the consumer asked for. A decision
that yields a denial, or that yields a permit for a document outside the requested window, is work thrown
away.

## The premise: the window indexes the authorized set

Two result sets are in play. The **match set** is what the query filter selects, in the requested order.
The **authorized set** is the subsequence of it this requestor may see. `startIndex` and `maxResults`
index the *authorized* set, and `totalResultCount`, if reported at all, is its size.

That is not a free choice. If the window indexed the match set instead, a consumer asking for fifty
results would receive however many of those fifty survived enforcement — a short page. And a short page
is the signal a consumer without a total uses to detect the end of the result set, so every page would
look like the last one. `startIndex` would be equally useless: a number the consumer cannot advance,
because it cannot know how many entries the previous page skipped. Enforcement has to be invisible in the
shape of the answer, which means the registry, not the consumer, absorbs it.

From which the governing rule follows: **a page is short only when the result set is exhausted.** Every
implementation choice below is constrained by it.

## Why the obvious implementations fail

**Filter after the window.** `ORDER BY … OFFSET 100 LIMIT 50`, then enforce the fifty rows. Cheapest
possible — fifty decisions — and wrong: it returns a short page whenever anything is denied, and its
`startIndex` counts denied documents the consumer never sees, so page boundaries drift against a set the
consumer is trying to walk.

**Enforce everything, then slice.** Fetch the match set, decide every entry, cut the window out of what
survives. Correct, trivially, and it is the design the extension exists to avoid: for the physician's
"twenty most recent" against a patient with four thousand documents it takes four thousand decisions to
return twenty, and it takes them again for the next page. The wasted fraction is the entire match set
minus one page.

**Enforce everything once, then cache the whole set.** A materialised per-requestor result cached across
pages. It amortises, but it still pays the full match set up front to answer the first window, which is
usually the only window anyone asks for, and it needs invalidation against both metadata and policy
changes. The up-front cost is exactly what the consumer was trying not to pay.

## The shape of the answer

Produce the match set as an **ordered stream**, and consume it lazily, deciding as you go, stopping the
moment the window is full.

```java
var collected = new ArrayList<DocumentEntry>(maxResults);
var skipped = 0;
var cursor = openOrderedStream(query, sortOrder);   // ORDER BY, keyset chunks

while (collected.size() < maxResults && cursor.hasNext()) {
    var block = cursor.next(blockSize(maxResults - collected.size(), passRate));
    var decisions = pdp.decide(requestor, block);   // one batched call per block
    for (var entry : block) {
        if (!decisions.permits(entry)) continue;
        if (skipped < startIndex) { skipped++; continue; }
        collected.add(entry);
        if (collected.size() == maxResults) break;
    }
    passRate.observe(block, decisions);
}
```

Three properties matter. The loop never decides an entry beyond the one that filled the window, except
for the tail of the block it was in. It never materialises the match set. And it fills the page whenever
the data can, so a short page keeps its meaning.

### The ordered stream

The requested `SortOrder` becomes the `ORDER BY`, key by key, with the entry UUID last —
`withStableTiebreaker()` is the consumer's side of that agreement, and a registry that receives an order
without a unique final key should append one itself, because it is the registry's page boundaries that
become undefined otherwise.

Two things have to match `SortOrderComparators` exactly, or a consumer that re-sorts locally will
disagree with the server about which entries belong on which page:

- **Missing values last in both directions.** `Direction.comparator()` wraps in `nullsLast` for ascending
  *and* descending. SQL does not: PostgreSQL puts nulls first under `DESC` by default. Spell it out —
  `ORDER BY creation_time DESC NULLS LAST` — and on databases without the modifier, emulate it with a
  leading `ORDER BY (creation_time IS NULL), creation_time DESC`.
- **Multivalued attributes by their first value in metadata order.** `$XDSDocumentEntryAuthorPerson`,
  `ConfidentialityCode` and `EventCodeList` are lists. A correlated subquery per row reproduces the
  semantics and destroys the index, so materialise the sort value into a column of the document entry
  table at submit time and order by that.

#### Keyset, not offset

The stream is chunked by **keyset** — the set of columns the order is made of, which is the `ORDER BY`
tuple with the unique tiebreaker included. Each block asks for the rows that come after the keyset *values*
of the last row of the previous block, instead of for the rows after a count:

```sql
-- offset: the database produces five thousand rows and throws them away
ORDER BY creation_time DESC, entry_uuid DESC OFFSET 5000 LIMIT 50

-- keyset: a seek to one position in the index, then fifty rows
WHERE (creation_time, entry_uuid) < (:lastTime, :lastUuid)
ORDER BY creation_time DESC, entry_uuid DESC LIMIT 50
```

`(a, b) < (x, y)` is SQL's row-value comparison, which is exactly lexicographic tuple order: "an earlier
creation time, or the same time and a smaller UUID". With a composite index on the filter columns plus the
sort columns plus the UUID, the second form costs the same at depth ten thousand as at depth ten, where the
first grows with the offset. That matters more here than in ordinary pagination, because the lazy fill
reads *further* than the window and in several blocks: a registry paying the offset penalty pays it per
block rather than per page.

Two constraints come with it.

**The order has to be total.** If two rows share the whole keyset, `<` cannot separate them, and one is
returned twice or not at all. That is the tiebreaker again, seen from the database side.

**The predicate has to mirror the `ORDER BY` exactly** — every comparison flipped per key direction, and
the nulls-last convention spelled into the seek as well as into the order. Row values only compare cleanly
when all keys run the same way; mixed directions have to be written out:

```sql
WHERE creation_time < :t
   OR (creation_time = :t AND (author_person > :a
   OR (author_person = :a AND entry_uuid > :u)))
```

And "cursor" here means a position, not a database cursor: no open transaction, no server-side handle, just
a tuple of values. That is what lets the same tuple travel in a resume token (§2) and be picked up by a
different node — and equally what stops it from being a snapshot, since it says where to continue and
nothing about whether the rows behind it have changed.

## Three sources of waste, and what removes each

Wasted decisions come from three places, and each has a different remedy.

### 1. Denials inside the scanned band — raise the pass rate

For each page the loop scans roughly `maxResults / p` entries, where `p` is the fraction of the match set
this requestor may see. The `(1 - p)` share is waste, and per document it is irreducible: the only way to
learn that a document is denied is to ask.

What is reducible is how many denied documents reach the decision point at all. Most consent regimes
decompose into a part that is a function of stored metadata and the requestor's constant attributes, and
a residue that genuinely needs evaluation:

- **Patient-level gates** — an opt-out, a blocked-consumer entry, a missing consent — are one decision for
  the whole query, not one per document. Evaluate them before touching the document table, and answer an
  opt-out with an empty result set rather than with four thousand denials. For the multi-patient queries
  this is one decision per patient, and denied patients drop out of the `WHERE` clause.
- **Structural rules** — confidentiality code against clearance, author organisation against the
  requestor's own, validity windows, explicit per-document blocks — are joins and `IN` lists. Every
  document they exclude is a document that never enters the stream and never counts as anything.
- **The residue** — delegation, break-glass, anything context-dependent — goes to the decision point.

The pre-filter must be **one-sided**: it may only exclude what the policy would deny anyway, never admit
what the policy would deny. Over-admitting is free, because the decision point catches it; under-admitting
silently loses documents the requestor was entitled to, and nothing downstream will notice. A rule that
cannot be shown to be one-sided stays in the residue.

This is the largest lever available. Pushing the policy into the query does not make enforcement cheaper;
it makes most of it unnecessary, by raising `p` toward 1 and shrinking the scanned band toward the page
itself.

### 2. The prefix — do not re-decide it on every page

`startIndex = 100` means "the hundred-and-first document this requestor may see", and there is no way to
find it except to decide the hundred before it. Over a sequence of pages that is quadratic: page *k*
re-decides everything pages 0…*k*−1 already decided.

Two ways out, in order of preference.

**Resume instead of skip.** The registry puts the sort tuple of the last returned entry into the response
slot list, the consumer echoes it on the next request, and the registry resumes the stream there. Page
cost stops depending on page depth, and neither side holds state between calls: everything the next
request needs travels with it, so any node of a cluster can serve it. This is the keyset pagination the
extension's *Known limits* names, and it composes with the total order the tiebreaker already guarantees.
What it costs is an extension of the bilateral agreement — one slot in each direction, spelled out
below — not a session, a cursor or a snapshot.

**A decision cache, for consumers that only speak `startIndex`.** Key it on requestor, entry UUID and a
policy epoch that any consent change bumps; keep it for the lifetime of the paging sequence, not longer.
The prefix still gets *scanned* on every page, but decided only once across the sequence, which is the
part that costs. Cache permits as conservatively as denials — a stale permit is a disclosure, a stale
denial is only an annoyance — so the epoch has to be driven by consent updates rather than by a timer
alone.

#### The resume token on the wire

One slot carries it in both directions, `$ipfResumeToken` by the same convention that names `$ipfSortOrder`.
Nothing else in the message format changes, and a registry that does not implement it ignores a slot it
does not know, exactly as ITI-18 requires.

**Page one** asks as it always did — no token exists yet:

```xml
<query:AdhocQueryRequest maxResults="50" id="urn:uuid:6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd">
    <query:ResponseOption returnType="LeafClass" returnComposedObjects="true"/>
    <AdhocQuery id="urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d">
        <Slot name="$ipfSortOrder">
            <ValueList>
                <Value>('-$XDSDocumentEntryCreationTime')</Value>
                <Value>('$XDSDocumentEntryEntryUUID')</Value>
            </ValueList>
        </Slot>
        <Slot name="$XDSDocumentEntryPatientId">
            <ValueList><Value>'p1^^^&amp;1.2.3.4&amp;ISO'</Value></ValueList>
        </Slot>
        <Slot name="$XDSDocumentEntryStatus">
            <ValueList><Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')</Value></ValueList>
        </Slot>
    </AdhocQuery>
</query:AdhocQueryRequest>
```

The answer carries the position the consumer has reached, next to the order the registry honoured. No
`startIndex`, because it was zero; no `totalResultCount`, because this registry does not count an
authorized set:

```xml
<query:AdhocQueryResponse status="…:Success"
                          requestId="urn:uuid:6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd">
    <rs:ResponseSlotList>
        <Slot name="$ipfSortOrder">
            <ValueList>
                <Value>('-$XDSDocumentEntryCreationTime')</Value>
                <Value>('$XDSDocumentEntryEntryUUID')</Value>
            </ValueList>
        </Slot>
        <Slot name="$ipfResumeToken">
            <ValueList><Value>'v1.eyJxIjoiOWYyYyIsImsiOlsiMjAyNi0wMy0xMVQwOToxNDowMFoi…'</Value></ValueList>
        </Slot>
    </rs:ResponseSlotList>
    <RegistryObjectList><!-- fifty ExtrinsicObjects --></RegistryObjectList>
</query:AdhocQueryResponse>
```

**Page two** echoes the token and says nothing about a start index — the token *is* the position:

```xml
<query:AdhocQueryRequest maxResults="50" id="urn:uuid:6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd">
    <query:ResponseOption returnType="LeafClass" returnComposedObjects="true"/>
    <AdhocQuery id="urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d">
        <Slot name="$ipfResumeToken">
            <ValueList><Value>'v1.eyJxIjoiOWYyYyIsImsiOlsiMjAyNi0wMy0xMVQwOToxNDowMFoi…'</Value></ValueList>
        </Slot>
        <!-- the sort order and every query parameter repeated verbatim -->
    </AdhocQuery>
</query:AdhocQueryRequest>
```

```xml
<query:AdhocQueryResponse startIndex="50" status="…:Success"
                          requestId="urn:uuid:6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd">
    <rs:ResponseSlotList>
        <Slot name="$ipfSortOrder">…</Slot>
        <Slot name="$ipfResumeToken">
            <ValueList><Value>'v1.eyJxIjoiOWYyYyIsImsiOlsiMjAyNi0wMi0xOFQxNzowMjowMFoi…'</Value></ValueList>
        </Slot>
    </rs:ResponseSlotList>
    <RegistryObjectList><!-- fifty more --></RegistryObjectList>
</query:AdhocQueryResponse>
```

`startIndex="50"` is the running count of authorized entries the token carried, so the registry can still
echo an honest index into the authorized set without having skipped — or decided — a single entry of the
prefix.

**The last page** is short and carries no token. The two say the same thing, and a consumer that
understands neither still reads the short page correctly:

```xml
<query:AdhocQueryResponse startIndex="100" status="…:Success" requestId="urn:uuid:6f8d…">
    <rs:ResponseSlotList>
        <Slot name="$ipfSortOrder">…</Slot>
    </rs:ResponseSlotList>
    <RegistryObjectList><!-- seventeen --></RegistryObjectList>
</query:AdhocQueryResponse>
```

The token is opaque to the consumer and authenticated by the registry — it is the registry's own state,
parked on the consumer for the duration:

```json
{ "v": 1,
  "q":  "9f2c…",                                     // hash of AdhocQuery id, all slots and returnType
  "r":  "3ab8…",                                     // hash of the requestor's identity and claims
  "k":  ["2026-02-18T17:02:00Z", "urn:uuid:b7…"],    // sort tuple of the last entry returned
  "n":  100,                                         // authorized entries emitted so far
  "e":  "epoch-2026-02-18T09:00Z",                   // policy epoch
  "iat": 1771417320 }
```

Five rules come with it:

- **A token and a `startIndex` are mutually exclusive.** A request carrying both is rejected: the token is
  the position, and a start index beside it is a second answer to one question.
- **`q` and `r` have to match**, or the registry answers with a RegistryError. A mismatch is either a stale
  sequence or an attempt to walk another requestor's entitlements, and the registry cannot tell which. The
  consumer's recovery is to start again at offset zero.
- **A stale `e`** is a judgement call: reject and restart, or resume and let the newer decisions apply from
  here on. The position of this note — the newer decision is the correct one — argues for resuming.
- **The token freezes nothing.** It is robust to submissions outside the window; an entry whose sort
  attributes change mid-sequence can still cross a page boundary. No snapshot is implied, and no server
  state exists to hold one.
- **`maxResults` may change between pages.** The token says where, not how many.

### 3. Overshoot — size the block, and keep what you paid for

Deciding one document at a time minimises overshoot and maximises round trips; deciding the whole match
set in one call does the reverse. The block size is the knob between them, and it wants to be derived,
not configured: `ceil(remaining / p)` against the observed pass rate, clamped to a sane range and
re-estimated as the query runs. The estimate converges within the first block or two, and a per-role
running average gives it a decent starting point.

The tail of the block that filled the window is the overshoot. It stops being waste the moment those
decisions go into the same cache used above: if the consumer asks for the next page, they are prefetch
rather than loss. Only a consumer that stops after one page pays for them, which is what the clamp is
for.

The estimate needs a floor under it, because it is only as good as its assumption that denials are spread
evenly. Put an escalating ladder of prefetch sizes beneath it — HAPI FHIR does the same one layer down,
where `search-prefetch-thresholds` says how much of a result set to materialise before knowing how much of
it survives — with each rung reached only by a round that failed to fill the window: the window itself,
then a few hundred, then the rest. A run of restricted documents then costs a handful of large calls
instead of a long series of small ones, and the number of round trips is bounded by the length of the
ladder rather than by how deep the run goes. The first rung has to be at least `startIndex + maxResults`, since nothing
smaller can fill the page even if everything in it is permitted. Rungs sized for a database — HAPI's first
jump is to five hundred — are too far to copy directly: there the unit is a row on a query already paid
for, here it is a decision.

## What it costs

Page size *m*, pass rate *p*, page index *k*, match set *M*:

| strategy | decisions for page *k* | wasted |
|---|---|---|
| enforce everything, then slice | \|M\| | \|M\| − *m* |
| lazy fill, `startIndex` paging, no cache | (*k*+1)·*m* / *p* | (*k*+1)·*m* / *p* − *m* |
| lazy fill, `startIndex` paging, decision cache | *m* / *p* amortised | *m*·(1−*p*) / *p* amortised |
| lazy fill, resume token | *m* / *p* | *m*·(1−*p*) / *p* |
| lazy fill, resume token, policy pushed into SQL | → *m* | → 0 |

Concretely, against the extension's own example — four thousand seven hundred matches, fifty per page,
four fifths of them permitted — the first page costs about sixty-three decisions instead of four thousand
seven hundred, and the tenth page costs sixty-three again instead of six hundred and twenty-five.

## What the response says

**`totalResultCount`: omit it.** The only honest total is the size of the authorized set, and computing it
means deciding the entire match set to answer one window — the cost this whole design exists to avoid, and
work discarded outright if the consumer never asks for a second page. Reporting the size of the *match*
set instead is worse than saying nothing: it is not the number the consumer will page to, and the
difference between it and what arrives tells the consumer exactly how many documents are being withheld,
which is a disclosure channel the registry has no reason to open. An absent total is a defined answer, and
the consumer detects the end of the set by the short page.

A registry that wants to report a total anyway can do it honestly in one case: when the match set is small
enough that deciding all of it is cheap regardless. Make that a threshold, report the total below it and
nothing above it, and never report a partial one.

**`startIndex`: echo what was honoured**, as an index into the authorized set — which is what the loop
above counts.

**`honoredSortOrder`: only the keys actually applied.** A key naming an attribute with no column is
dropped, and the reported order is then the sub-order that survived, not what was asked for. One
exception: if the *tiebreaker* cannot be applied, the order is not total, page boundaries are undefined,
and the registry should refuse the window rather than serve an unstable one.

**`START_INDEX_BEYOND_END`** falls out of the loop: the stream was exhausted before `startIndex` permitted
entries were skipped. It is the one case that costs a full scan and a full pass of enforcement, and there
is nothing cheaper, because the registry cannot know the size of the authorized set without producing it.
It is also pathological by construction — a consumer paging in sequence reaches a short page first. Offset
zero stays exempt: a query that matches nothing, or whose every match is denied, is a legitimate empty
answer and not an out-of-range window.

## Two things enforcement must not be placed after

**Object references.** With `returnType=ObjectRef` the response carries entry UUIDs and nothing else.
Enforcing after the projection is impossible — the metadata the policy reads is gone — and skipping
enforcement because "it is only a reference" hands the consumer the identifiers of documents it may not
see, and lets it count them. Authorize, then sort, then project.

**The second phase.** The `GetDocuments` call that follows a two-phase query is a `Get…` query: not
pageable, and the requestor names the UUIDs. It still has to be enforced, against the same policy, or the
second phase becomes a way around the first. It is the natural consumer of the decision cache, since every
UUID in it was decided moments ago.

## Known limits

- **Snapshot consistency.** The lazy fill reads the match set across several statements, so a submission
  landing mid-scan can shift entries between pages. Keyset resumption is robust to insertions outside the
  window but not to changes to the sort attributes of entries inside it. A read-only transaction spanning
  one page bounds the damage to page granularity; bounding it across a paging sequence needs a snapshot,
  which means server state, which the extension deliberately does not mint.
- **Policy changes mid-sequence.** A consent revoked between page three and page four legitimately changes
  the authorized set under the consumer's feet. The policy epoch keeps the cache honest; it does not make
  the sequence coherent, and it should not — the newer decision is the correct one.
- **Estimating `p` for a mixed match set.** A single pass rate assumes denials are spread evenly through
  the order. When they cluster — a run of restricted documents from one encounter, adjacent because the
  sort is by creation time — the block estimate undershoots and the loop takes extra round trips. The
  prefetch ladder bounds how many, and within the request the estimate corrects itself; what neither can
  do is tell a clustered result set from an evenly denied one in advance.
- **Cross-community.** None of this survives an XCA gateway: each responding community enforces its own
  policy over its own result set, and the initiating gateway cannot merge windows of authorized sets it
  cannot index.

## References

- [Sorting and Paging for ITI-18](XDS-Sorting-and-Paging.md) — the extension this implements
- `SortOrderComparators` — the ordering semantics the SQL `ORDER BY` has to reproduce
- `tutorials/xds` — a worked example of the lazy fill, over an in-memory store and a decision point that
  permits everything and counts what it was asked. `AuthorizedPager` is the loop above; `Iti18RouteBuilder`
  is the surrounding policy — enforcement on every path including the second phase, the threshold rule for
  `totalResultCount`, and `START_INDEX_BEYOND_END` falling out of an exhausted stream.
  `CachingDecisionPoint` is the decision cache of §2, as a decorator, with a constant requestor standing in
  for the one a real registry reads from a token. What has no counterpart there, and stays described only:
  the SQL half (keyset chunks, `ORDER BY … NULLS LAST`, materialised sort columns) and resume tokens
