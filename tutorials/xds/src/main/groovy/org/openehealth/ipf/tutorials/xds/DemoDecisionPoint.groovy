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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.atomic.AtomicLong

/**
 * A decision point that permits everything, and counts what it was asked.
 * <p>
 * It stands in for the thing this tutorial deliberately does not have: a policy decision point that
 * evaluates consent for one document and one requestor. Permitting everything keeps the tutorial's
 * behaviour unchanged for anyone not interested in enforcement, while the counters make the quantity
 * {@link AuthorizedPager} minimises -- decisions taken for documents that do not end up in the response
 * -- visible in the log and assertable in a test.
 * <p>
 * A {@link #rule} can be installed to deny entries, which is how a demonstration shows that a page stays
 * full when entries in the scanned band are denied. It is not meant as a policy language.
 *
 * @author Christian Ohr
 */
class DemoDecisionPoint implements AuthorizationDecisionPoint {
    private final static Logger log = LoggerFactory.getLogger(DemoDecisionPoint.class)

    /** Decides a single entry. Permits everything unless a demonstration replaces it. */
    Closure<Boolean> rule = { XDSMetaClass entry -> true }

    /**
     * Milliseconds this decision point pretends to spend per entry decided, zero by default.
     * <p>
     * A real one costs a call out of process, a policy set to evaluate and possibly a consent document to
     * fetch, which is what makes a decision the unit worth rationing rather than the round trip. Deciding
     * here is a closure call, so without a delay the saving is only visible as a count.
     */
    long latencyPerDecision = 0

    private final AtomicLong decisionCount = new AtomicLong()
    private final AtomicLong callCount = new AtomicLong()
    private final AtomicLong permitCount = new AtomicLong()

    @Override
    Set<String> decide(String requestor, List<? extends XDSMetaClass> entries) {
        callCount.incrementAndGet()
        decisionCount.addAndGet(entries.size())
        if (latencyPerDecision > 0) {
            // once for the whole block, because the cost of a batched decision point is in the entries
            // it evaluates, not in the call that carries them
            sleep(entries.size() * latencyPerDecision)
        }
        def permitted = entries.findAll { rule(it) }*.entryUuid as Set
        permitCount.addAndGet(permitted.size())
        log.debug('decided {} entries for {}, permitted {}', entries.size(), requestor, permitted.size())
        permitted
    }

    /** Number of entries decided, which is what a real decision point charges for. */
    long getDecisions() { decisionCount.get() }

    /** Number of batched invocations, which is what the round trips cost. */
    long getCalls() { callCount.get() }

    long getPermits() { permitCount.get() }

    void reset() {
        decisionCount.set(0)
        callCount.set(0)
        permitCount.set(0)
    }

    /** Restores the permit-everything default. */
    void permitAll() {
        rule = { XDSMetaClass entry -> true }
    }
}
