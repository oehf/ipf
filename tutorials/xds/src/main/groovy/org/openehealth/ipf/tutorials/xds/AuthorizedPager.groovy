/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.tutorials.xds

import groovy.transform.Canonical
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass

/**
 * Fills one window of an authorized result set, deciding as few registry objects as it can.
 * <p>
 * Two result sets are in play. The <em>match set</em> is what the query filter selects, in the requested
 * order; the <em>authorized set</em> is the subsequence of it the requestor may see. The window
 * ({@code startIndex}, {@code maxResults}) indexes the <em>authorized</em> set, and that is not a free
 * choice: if it indexed the match set, a consumer asking for fifty would receive however many of those
 * fifty survived enforcement, and a short page is exactly the signal a consumer without a total uses to
 * detect the end of the result set. Enforcement has to be invisible in the shape of the answer, from
 * which the governing rule follows: <b>a page is short only when the result set is exhausted.</b>
 * <p>
 * The obvious implementations both fail that rule or cost too much. Filtering after the window is
 * cheapest and returns short pages; enforcing the whole match set and then slicing is correct and takes
 * a decision per match to return one page of them. So the match set is consumed as an ordered stream,
 * lazily, deciding blocks as it goes and stopping the moment the window is full. The loop never decides
 * an entry beyond the one that filled the window, except for the tail of the block it was in, and it
 * fills the page whenever the data can.
 * <p>
 * How much to decide per round is settled by two mechanisms that cover each other. The observed pass
 * rate estimates how far ahead the window's worth of permitted entries lies, which is exact when denials
 * are spread evenly and useless when they come in runs; {@link #prefetchThresholds}, a ladder of
 * escalating prefetch sizes in the manner of HAPI FHIR's {@code search-prefetch-thresholds}, puts a floor
 * under each successive round so that a long denied run costs a few large calls instead of many small
 * ones. The first rung is never smaller than the window being filled, and the rungs sit an order of
 * magnitude below HAPI's, where a unit is a database row on a query already paid for rather than a policy
 * decision.
 * <p>
 * In a registry backed by a database the stream is a keyset cursor and its order is the {@code ORDER BY}
 * built from the requested {@link org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrder};
 * here it is an iterator over the already sorted match set, because this tutorial keeps everything in
 * memory. What the two have in common is the expensive part -- one decision per XDS entry -- and that is
 * what this class economises. Sorting before deciding is not the same as the projection to object
 * references, which must happen <em>after</em> enforcement: an {@code ObjectRef} response carries entry
 * UUIDs and nothing the policy could read.
 * <p>
 * What is deliberately not demonstrated here: keyset chunking, because there is no I/O to save, and
 * resume tokens instead of a skipped prefix, because that changes the bilateral agreement. With
 * {@code startIndex} paging, page <i>k</i> costs {@code (k+1) * maxResults / p} decisions -- the price of
 * finding the start of the window at all -- of which {@link CachingDecisionPoint} removes the repetition,
 * though not the scan.
 *
 * @author Christian Ohr
 * @see AuthorizationDecisionPoint
 */
class AuthorizedPager {

    /** Result of one fill. */
    @Canonical
    static class Page {
        /** The entries of the window, all of them permitted. */
        List<XDSMetaClass> entries
        /** Whether the stream ran out, i.e. whether a short page means "no more". */
        boolean exhausted
        /** How many permitted entries were skipped before the window, i.e. the honored start index. */
        int skipped
        /** How many entries were decided to produce this page, for the log. */
        int decided
    }

    /** Size of a block that takes whatever is left of the stream. */
    private static final int UNBOUNDED = Integer.MAX_VALUE

    AuthorizationDecisionPoint decisionPoint

    /**
     * Largest block the pass rate estimate may ask for -- a cap on the estimate alone, not on the block:
     * a rung of {@link #prefetchThresholds} that reaches further still applies in full. See
     * {@link #blockSize} for how the two combine.
     */
    int maxBlockSize = 200

    /** Lowest pass rate the estimate will assume, however much of what it has seen was denied. */
    double minPassRate = 0.05

    /**
     * How far ahead to decide when a round leaves the window unfilled, as ascending cumulative counts of
     * entries decided -- the shape of HAPI FHIR's {@code search-prefetch-thresholds}. Each rung is a
     * floor under the block size, so it overrides both the estimate and {@link #maxBlockSize}.
     * <p>
     * A rung below what this request needs to scan ({@code startIndex + maxResults}) is raised to it, so
     * {@code 0} means "exactly the window". A negative rung means "the rest of the stream" and belongs
     * last; without one the ladder runs out and the estimate carries on alone.
     */
    List<Integer> prefetchThresholds = [0, 100, 1000, -1]

    /**
     * @param requestor  whoever the query arrived on behalf of
     * @param stream     the match set, in the requested order, all of one kind -- document entries,
     *                   folders or submission sets, whichever the query searched for
     * @param startIndex how many permitted entries to skip; zero or less means "from the beginning"
     * @param maxResults window size, or null for "everything from startIndex on". Zero is not a window
     *                   to fill and is answered by the caller, which alone can decide whether counting
     *                   the authorized set is affordable at all.
     */
    Page fill(String requestor, Iterator<? extends XDSMetaClass> stream, int startIndex, Integer maxResults) {
        def collected = new ArrayList<XDSMetaClass>(maxResults != null ? maxResults : 16)
        def skipped = 0
        def decided = 0
        def permitsSeen = 0
        // What this request needs to scan at the very least: the prefix it has to skip plus the page it
        // has to fill, all of them permitted. The first prefetch is never smaller than that.
        def window = maxResults != null ? startIndex + maxResults : 0

        while ((maxResults == null || collected.size() < maxResults) && stream.hasNext()) {
            // What still has to be found: the unskipped part of the prefix plus the unfilled window.
            // Both cost decisions, so both belong in the estimate -- sizing on the window alone makes
            // every page but the first take needless round trips.
            def remaining = maxResults != null
                    ? (startIndex - skipped) + (maxResults - collected.size())
                    : maxBlockSize
            def block = next(stream, blockSize(remaining, decided, permitsSeen, window))
            def permitted = decisionPoint.decide(requestor, block)
            decided += block.size()
            permitsSeen += permitted.size()

            for (entry in block) {
                if (!permitted.contains(entry.entryUuid)) continue
                if (skipped < startIndex) {
                    skipped++
                    continue
                }
                collected.add(entry)
                if (maxResults != null && collected.size() == maxResults) break
            }
        }

        new Page(collected, !stream.hasNext(), skipped, decided)
    }

    /**
     * {@code max(min(ceil(remaining / p), maxBlockSize), nextRung - decided)} -- the estimate proposes,
     * the ladder insists, and only the estimate is capped.
     * <p>
     * The pass rate starts at one, so a registry that permits everything -- the default of this tutorial
     * -- fills a window in a single call with no overshoot at all, and converges within a block or two
     * otherwise. The ladder is what carries the loop when that estimate is fooled: it does not react to
     * the pass rate at all, it just reaches further every time the window comes up short.
     */
    private int blockSize(int remaining, int decided, int permits, int window) {
        def prefetch = prefetch(decided, window)
        if (prefetch == UNBOUNDED) {
            return UNBOUNDED
        }
        def passRate = decided == 0 ? 1.0d : Math.max((permits as double) / decided, minPassRate)
        def estimate = Math.min(Math.ceil(remaining / passRate).intValue(), maxBlockSize)
        Math.max(1, Math.max(estimate, prefetch))
    }

    /**
     * How much further the next threshold reaches. The thresholds are cumulative, so the first one that
     * lies beyond what has already been decided is the one still to be reached; the ones behind are spent
     * -- which is what makes each failed round grab more than the last.
     *
     * @return the number of entries to the next threshold, {@link #UNBOUNDED} for "the rest of the
     *         stream", or 0 once the ladder is spent, leaving the estimate to carry on alone
     */
    private int prefetch(int decided, int window) {
        for (threshold in prefetchThresholds) {
            if (threshold < 0) {
                return UNBOUNDED
            }
            def target = Math.max(threshold, window)
            if (target > decided) {
                return target - decided
            }
        }
        0
    }

    private static List<XDSMetaClass> next(Iterator<? extends XDSMetaClass> stream, int count) {
        // the count can be "the rest of the stream", which is no guide at all to how much to allocate
        def block = new ArrayList<XDSMetaClass>(Math.min(count, 1024))
        while (block.size() < count && stream.hasNext()) {
            block.add(stream.next())
        }
        block
    }
}
