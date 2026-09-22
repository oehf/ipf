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

import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * Walks a paging sequence with and without a verdict cache. What the cache removes is the re-decision of
 * the prefix, which is the whole cost of paging deep with {@code startIndex}.
 *
 * @author Christian Ohr
 */
class TestCachingDecisionPoint {

    private static final int MATCH_SET = 240
    private static final int PAGE_SIZE = 20
    private static final int PAGES = 4

    private final entries = (1..MATCH_SET).collect { new DocumentEntry(entryUuid: 'urn:uuid:' + it) }

    /** Denies every third entry, so that filling a page always scans further than the page. */
    private final policy = new DemoDecisionPoint(rule: { XDSMetaClass entry ->
        (entry.entryUuid.substring(entry.entryUuid.lastIndexOf(':') + 1) as int) % 3 != 0
    })

    /**
     * Without a cache, page <i>k</i> decides everything pages 0…<i>k</i>-1 already decided. With one, the
     * prefix is still scanned but each entry is decided once across the whole sequence -- so the sequence
     * costs what its deepest page costs, instead of the sum of all of them.
     */
    @Test
    void testTheSequenceDecidesEachEntryOnce() {
        def uncached = walk(new AuthorizedPager(decisionPoint: policy))

        policy.reset()
        def cache = new CachingDecisionPoint(delegate: policy)
        def cached = walk(new AuthorizedPager(decisionPoint: cache))

        // the same four pages either way -- a cache that changes an answer is a bug, not an optimisation
        assertEquals(uncached.entries, cached.entries)

        // four pages of twenty cost 419 decisions when each one re-decides its prefix, and 119 when they
        // do not -- the sequence then costs what its deepest page costs instead of the sum of them all
        assertEquals(419, uncached.decisions)
        assertEquals(119, cached.decisions)
        assertEquals(119, cache.misses)
        assertEquals(300, cache.hits)
    }

    /**
     * The counts above are the proof; this is the demonstration. A decision point that takes a millisecond
     * per entry -- a modest figure for a call out of process with a policy set to evaluate -- turns the
     * three hundred saved decisions into wall clock.
     */
    @Test
    void testTheSavedDecisionsAreSavedTime() {
        policy.latencyPerDecision = 1

        def uncachedMillis = timed { walk(new AuthorizedPager(decisionPoint: policy)) }
        def cachedMillis = timed { walk(new AuthorizedPager(decisionPoint: new CachingDecisionPoint(delegate: policy))) }

        assertTrue(cachedMillis * 1.5 < uncachedMillis,
                "the cached sequence took ${cachedMillis} ms, the uncached one ${uncachedMillis} ms")
    }

    /**
     * A consent change invalidates what was cached, and the next page pays for its prefix again. The
     * newer decision is the correct one, which is the point of the epoch -- a cache that outlived a
     * revocation would keep disclosing.
     */
    @Test
    void testInvalidatingForgetsEverything() {
        def cache = new CachingDecisionPoint(delegate: policy)
        def pager = new AuthorizedPager(decisionPoint: cache)
        pager.fill('anyone', entries.iterator(), 0, PAGE_SIZE)
        def decidedForTheFirstPage = policy.decisions

        cache.invalidate()
        policy.reset()
        pager.fill('anyone', entries.iterator(), 0, PAGE_SIZE)

        assertEquals(decidedForTheFirstPage, policy.decisions)
        assertEquals(0, cache.hits)
    }

    /** Verdicts belong to a requestor, and one requestor's entitlements say nothing about another's. */
    @Test
    void testVerdictsAreNotSharedBetweenRequestors() {
        def cache = new CachingDecisionPoint(delegate: policy)
        def pager = new AuthorizedPager(decisionPoint: cache)

        pager.fill('dr-jones', entries.iterator(), 0, PAGE_SIZE)
        pager.fill('dr-smith', entries.iterator(), 0, PAGE_SIZE)

        assertEquals(0, cache.hits)
    }

    /**
     * Walks the whole paging sequence the way a consumer that only speaks startIndex has to.
     *
     * @return the unique ids collected, page by page, and what the decision point was asked
     */
    private def walk(AuthorizedPager pager) {
        policy.reset()
        def collected = []
        (0..<PAGES).each { page ->
            collected.addAll(pager.fill('anyone', entries.iterator(), page * PAGE_SIZE, PAGE_SIZE).entries*.entryUuid)
        }
        [entries: collected, decisions: policy.decisions]
    }

    private static long timed(Closure work) {
        def started = System.currentTimeMillis()
        work()
        System.currentTimeMillis() - started
    }
}
