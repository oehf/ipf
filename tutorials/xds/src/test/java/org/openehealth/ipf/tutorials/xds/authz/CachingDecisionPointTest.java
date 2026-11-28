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
package org.openehealth.ipf.tutorials.xds.authz;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.tutorials.xds.authz.AuthorizedPagerTest.entryList;
import static org.openehealth.ipf.tutorials.xds.authz.AuthorizedPagerTest.positionOf;

/**
 * Walks a paging sequence with and without a verdict cache. What the cache removes is the re-decision of
 * the prefix, which is the whole cost of paging deep with {@code startIndex}.
 *
 * @author Christian Ohr
 */
class CachingDecisionPointTest {

    private static final int MATCH_SET = 240;
    private static final int PAGE_SIZE = 20;
    private static final int PAGES = 4;

    private final List<DocumentEntry> entries = entryList(MATCH_SET);

    /** Denies every third entry, so that filling a page always scans further than the page. */
    private final DemoDecisionPoint policy = new DemoDecisionPoint();

    {
        policy.setRule(entry -> positionOf(entry) % 3 != 0);
    }

    /** The unique ids collected over a paging sequence, and what the decision point was asked for them. */
    private record Walk(List<String> entries, long decisions) {
    }

    /**
     * Without a cache, page <i>k</i> decides everything pages 0…<i>k</i>-1 already decided. With one, the
     * prefix is still scanned but each entry is decided once across the whole sequence -- so the sequence
     * costs what its deepest page costs, instead of the sum of all of them.
     */
    @Test
    void theSequenceDecidesEachEntryOnce() {
        var uncached = walk(pager(policy));

        policy.reset();
        var cache = cache();
        var cached = walk(pager(cache));

        // the same four pages either way -- a cache that changes an answer is a bug, not an optimization
        assertEquals(uncached.entries(), cached.entries());

        // four pages of twenty cost 419 decisions when each one re-decides its prefix, and 119 when they
        // do not -- the sequence then costs what its deepest page costs instead of the sum of them all
        assertEquals(419, uncached.decisions());
        assertEquals(119, cached.decisions());
        assertEquals(119, cache.getMisses());
        assertEquals(300, cache.getHits());
    }

    /**
     * The counts above are the proof; this is the demonstration. A decision point that takes a millisecond
     * per entry -- a modest figure for a call out of process with a policy set to evaluate -- turns the
     * three hundred saved decisions into wall clock.
     */
    @Test
    void theSavedDecisionsAreSavedTime() {
        policy.setLatencyPerDecision(1);

        var uncachedMillis = timed(() -> walk(pager(policy)));
        var cachedMillis = timed(() -> walk(pager(cache())));

        assertTrue(cachedMillis * 1.5 < uncachedMillis,
                "the cached sequence took " + cachedMillis + " ms, the uncached one " + uncachedMillis + " ms");
    }

    /**
     * A consent change invalidates what was cached, and the next page pays for its prefix again. The
     * newer decision is the correct one, which is the point of the epoch -- a cache that outlived a
     * revocation would keep disclosing.
     */
    @Test
    void invalidatingForgetsEverything() {
        var cache = cache();
        var pager = pager(cache);
        pager.fill("anyone", entries.iterator(), 0, PAGE_SIZE);
        var decidedForTheFirstPage = policy.getDecisions();

        cache.invalidate();
        policy.reset();
        pager.fill("anyone", entries.iterator(), 0, PAGE_SIZE);

        assertEquals(decidedForTheFirstPage, policy.getDecisions());
        assertEquals(0, cache.getHits());
    }

    /** Verdicts belong to a requestor, and one requestor's entitlements say nothing about another's. */
    @Test
    void verdictsAreNotSharedBetweenRequestors() {
        var cache = cache();
        var pager = pager(cache);

        pager.fill("dr-jones", entries.iterator(), 0, PAGE_SIZE);
        pager.fill("dr-smith", entries.iterator(), 0, PAGE_SIZE);

        assertEquals(0, cache.getHits());
    }

    /**
     * Walks the whole paging sequence the way a consumer that only speaks startIndex has to.
     */
    private Walk walk(AuthorizedPager pager) {
        policy.reset();
        var collected = new ArrayList<String>();
        for (var page = 0; page < PAGES; page++) {
            pager.fill("anyone", entries.iterator(), page * PAGE_SIZE, PAGE_SIZE).getEntries()
                    .forEach(entry -> collected.add(entry.getEntryUuid()));
        }
        return new Walk(collected, policy.getDecisions());
    }

    private CachingDecisionPoint cache() {
        var cache = new CachingDecisionPoint();
        cache.setDelegate(policy);
        return cache;
    }

    private static AuthorizedPager pager(AuthorizationDecisionPoint decisionPoint) {
        var pager = new AuthorizedPager();
        pager.setDecisionPoint(decisionPoint);
        return pager;
    }

    private static long timed(Runnable work) {
        var started = System.currentTimeMillis();
        work.run();
        return System.currentTimeMillis() - started;
    }
}
