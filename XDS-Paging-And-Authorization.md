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

The quantity to minimize is precise: **decisions taken for documents that do not end up in the
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

**Enforce everything once, then cache the whole set.** A materialized per-requestor result cached across
pages. It amortizes, but it still pays the full match set up front to answer the first window, which is
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
for the tail of the block it was in. It never materializes the match set. And it fills the page whenever
the data can, so a short page keeps its meaning.

### The ordered stream

The requested `SortOrder` becomes the `ORDER BY`, key by key, with the entry UUID last —
`withStableTiebreaker()` is the consumer's side of that agreement. A registry that receives an order
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
a tuple of values carried from one block to the next within a request. That is also what stops it from
being a snapshot: it says where to continue and nothing about whether the rows behind it have changed.

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
- **Structural rules** — confidentiality code against clearance, author organization against the
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

### 2. The prefix — decide it once, if you can

`startIndex = 100` means "the hundred-and-first document this requestor may see", and there is no way to
find it except to decide the hundred before it. Over a sequence of pages that is quadratic: page *k*
re-decides everything pages 0…*k*−1 already decided.

The remedy is a **decision cache**. Key it on requestor, entry UUID and a policy epoch that any consent
change bumps; keep it for about the lifetime of a paging sequence, not longer. The prefix still gets
*scanned* on every page, but decided only once across the sequence, which is the part that costs. Cache
permits as conservatively as denials — a stale permit is a disclosure, a stale denial is only an
annoyance — so the epoch has to be driven by consent updates rather than by a timer alone.

Because it remembers decisions rather than positions, the cache serves every access pattern ITI-18
allows — forward, backward, a jump, a `maxResults` that changes between pages — and asks nothing of the
consumer beyond the `startIndex` the transaction already has.

Where the cache lives is the registry's business: in memory on a single node; in a distributed cache, a
table beside the document entries or behind sticky routing in a cluster. None of it shows on the wire.
Nor does the cache have to exist, or hit. A miss — an evicted entry, a different node, a bumped epoch —
costs the decisions for the prefix in front of the requested window once more, and nothing worse: the
answer stays correct, and only the price of that page rises to the no-cache row of the table below.

What the cache does not save is the scan. With only `startIndex` to go on, every page reads the match set
from its beginning, about (*k*+1)·*m* / *p* rows for page *k*. Next to decisions that is cheap; where it
is not, see *Resume tokens* under Known limits.

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

| strategy                                          | decisions for page *k* | wasted                      |
|---------------------------------------------------|------------------------|-----------------------------|
| enforce everything, then slice                    | \|M\|                  | \|M\| − *m*                 |
| lazy fill, no cache                               | (*k*+1)·*m* / *p*      | (*k*+1)·*m* / *p* − *m*     |
| lazy fill, decision cache                         | *m* / *p* amortized    | *m*·(1−*p*) / *p* amortized |
| lazy fill, decision cache, policy pushed into SQL | → *m* amortized        | → 0                         |

Concretely, against the extension's own example — four thousand seven hundred matches, fifty per page,
four fifths of them permitted — the first page costs about sixty-three decisions instead of four thousand
seven hundred, and with the cache warm the tenth page costs sixty-three again instead of six hundred and
twenty-five.

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

**`startIndex`: echo what was honored**, as an index into the authorized set — which is what the loop
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
- **Resume tokens.** Instead of remembering decisions, a registry could hand the consumer its position:
  the sort tuple of the last entry returned and the running count of authorized entries, in an
  authenticated `$ipfResumeToken` response slot that the consumer echoes on the next request. The next page
  would then neither scan nor decide the prefix, and no server state would be needed. It is not specified
  here because it saves no decisions over the cache, only helps forward paging (going back needs tokens
  the consumer kept, or a reverse seek), and adds a slot in each direction to the bilateral agreement,
  plus rules binding it to the query, the requestor and the policy epoch. The ordered stream already
  resumes from a tuple, so it can be added later without breaking anything.
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
  the SQL half (keyset chunks, `ORDER BY … NULLS LAST`, materialised sort columns).
