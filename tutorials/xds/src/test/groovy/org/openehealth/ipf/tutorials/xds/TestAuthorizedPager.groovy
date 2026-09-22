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
 * Tests the lazy fill directly, without a registry around it, because what matters here is how much it
 * decides and in how many calls -- and that is easier to state as a list of block sizes than to infer
 * from a query response.
 *
 * @author Christian Ohr
 */
class TestAuthorizedPager {

    /**
     * Records what it was asked, which is the whole point of these tests, and denies by the position of
     * an entry in the stream so that a denied <em>run</em> can be described.
     */
    private static class RecordingDecisionPoint implements AuthorizationDecisionPoint {
        Closure<Boolean> rule = { XDSMetaClass entry -> true }
        def blockSizes = []

        @Override
        Set<String> decide(String requestor, List<? extends XDSMetaClass> block) {
            blockSizes.add(block.size())
            block.findAll { rule(it) }*.entryUuid as Set
        }
    }

    private final decisionPoint = new RecordingDecisionPoint()

    /**
     * The first prefetch is the window itself: a registry that permits everything pays exactly one call
     * and decides exactly the entries it returns.
     */
    @Test
    void testTheFirstPrefetchIsTheWindow() {
        def page = pager().fill('anyone', entries(100), 0, 20)

        assertEquals([20], decisionPoint.blockSizes)
        assertEquals(20, page.entries.size())
        assertEquals(0, page.skipped)
    }

    /**
     * The prefix counts towards it. {@code startIndex} is an index into the authorized set, so reaching
     * the window means deciding everything before it as well -- which is the cost the design note's
     * resume token would remove.
     */
    @Test
    void testTheFirstPrefetchCoversTheSkippedPrefix() {
        def page = pager().fill('anyone', entries(100), 30, 20)

        assertEquals([50], decisionPoint.blockSizes)
        assertEquals(30, page.skipped)
        assertEquals(20, page.entries.size())
    }

    /**
     * A run of denied documents is what the ladder is for. The pass rate estimate keeps proposing a block
     * barely bigger than what is missing -- and is capped besides -- so without the escalation the loop
     * would crawl through the run in small steps. Each round that fails to fill the window reaches the
     * next threshold instead, and the last one takes everything that is left.
     */
    @Test
    void testTheLadderEscalatesThroughADeniedRun() {
        def pager = pager()
        pager.prefetchThresholds = [0, 50, 200, -1]
        pager.maxBlockSize = 25
        denyFirst(250)

        def page = pager.fill('anyone', entries(300), 0, 20)

        // the window, then to 50, then to 200, then the rest -- four calls for a run the estimate would
        // have taken a dozen to walk through
        assertEquals([20, 30, 150, 100], decisionPoint.blockSizes)
        assertEquals(20, page.entries.size())
        assertEquals(300, page.decided)
    }

    /**
     * The ladder is a floor, not a cap: where the estimate asks for more than the next threshold, the
     * estimate wins, and a well-behaved pass rate still fills the window in one or two calls.
     */
    @Test
    void testTheEstimateWinsWhereItAsksForMore() {
        def pager = pager()
        pager.prefetchThresholds = [0, 25, -1]
        denyEveryOther()

        def page = pager.fill('anyone', entries(200), 0, 20)

        assertEquals(20, page.entries.size())
        // the first block is the window, and the second is sized from the observed pass rate of a half --
        // twenty more permitted entries lie about twenty further on, not the five the ladder asked for
        assertEquals([20, 20], decisionPoint.blockSizes)
    }

    /**
     * A ladder without a "rest of the stream" rung simply runs out, and the estimate carries the loop on
     * its own. Correctness never depended on the ladder: a short page still means an exhausted stream.
     */
    @Test
    void testTheLadderMayRunOut() {
        def pager = pager()
        pager.prefetchThresholds = [0]
        pager.maxBlockSize = 50
        denyFirst(100)

        def page = pager.fill('anyone', entries(300), 0, 20)

        assertEquals([20, 50, 50], decisionPoint.blockSizes)
        assertEquals(20, page.entries.size())
    }

    /**
     * A page is short only when the result set is exhausted, whatever enforcement removed on the way --
     * which is what lets a consumer without a total detect the end of the set.
     */
    @Test
    void testAShortPageMeansExhaustion() {
        denyEveryOther()

        def page = pager().fill('anyone', entries(30), 0, 20)

        assertEquals(15, page.entries.size())
        assertTrue(page.exhausted)
    }

    /**
     * An unbounded fill has no window to stop at and decides everything -- the shape enforcement takes
     * where no page bounds the work, such as a Get... query.
     */
    @Test
    void testAnUnboundedFillDecidesEverything() {
        denyEveryOther()

        def page = pager().fill('anyone', entries(100), 0, null)

        assertEquals(50, page.entries.size())
        assertEquals(100, page.decided)
    }

    private AuthorizedPager pager() {
        new AuthorizedPager(decisionPoint: decisionPoint)
    }

    /** Denies a run at the head of the stream, the way one restricted encounter would. */
    private void denyFirst(int count) {
        decisionPoint.rule = { XDSMetaClass entry -> positionOf(entry) > count }
    }

    private void denyEveryOther() {
        decisionPoint.rule = { XDSMetaClass entry -> positionOf(entry) % 2 == 1 }
    }

    private static int positionOf(XDSMetaClass entry) {
        entry.entryUuid.substring(entry.entryUuid.lastIndexOf(':') + 1) as int
    }

    /** A stream of entries carrying nothing but their position, which is all the rules here read. */
    private static Iterator<DocumentEntry> entries(int count) {
        (1..count).collect { new DocumentEntry(entryUuid: 'urn:uuid:' + it) }.iterator()
    }
}
