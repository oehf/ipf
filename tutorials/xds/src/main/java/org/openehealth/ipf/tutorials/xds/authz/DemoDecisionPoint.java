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

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A decision point that permits everything, and counts what it was asked.
 * <p>
 * It stands in for the thing this tutorial deliberately does not have: a policy decision point that
 * evaluates consent for one document and one requestor. Permitting everything keeps the tutorial's
 * behavior unchanged for anyone not interested in enforcement, while the counters make the quantity
 * {@link AuthorizedPager} minimises -- decisions taken for documents that do not end up in the response
 * -- visible in the log and assertable in a test.
 * <p>
 * A {@link #rule} can be installed to deny entries, which is how a demonstration shows that a page stays
 * full when entries in the scanned band are denied. It is not meant as a policy language.
 *
 * @author Christian Ohr
 */
@Slf4j
public class DemoDecisionPoint implements AuthorizationDecisionPoint {

    private static final Predicate<XDSMetaClass> PERMIT_ALL = entry -> true;

    /** Decides a single entry. Permits everything unless a demonstration replaces it. */
    @Getter @Setter
    private Predicate<XDSMetaClass> rule = PERMIT_ALL;

    /**
     * Milliseconds this decision point pretends to spend per entry decided, zero by default.
     * <p>
     * A real one costs a call out of process, a policy set to evaluate and possibly a consent document to
     * fetch, which is what makes a decision the unit worth rationing rather than the round trip. Deciding
     * here is a predicate call, so without a delay the saving is only visible as a count.
     */
    @Getter @Setter
    private long latencyPerDecision = 0;

    private final AtomicLong decisionCount = new AtomicLong();
    private final AtomicLong callCount = new AtomicLong();
    private final AtomicLong permitCount = new AtomicLong();

    @Override
    public Set<String> decide(String requestor, List<? extends XDSMetaClass> entries) {
        callCount.incrementAndGet();
        decisionCount.addAndGet(entries.size());
        if (latencyPerDecision > 0) {
            // once for the whole block, because the cost of a batched decision point is in the entries
            // it evaluates, not in the call that carries them
            pause(entries.size() * latencyPerDecision);
        }
        var rule = Objects.requireNonNull(this.rule, "rule");
        var permitted = entries.stream()
                .filter(rule)
                .map(XDSMetaClass::getEntryUuid)
                .collect(Collectors.toSet());
        permitCount.addAndGet(permitted.size());
        log.debug("decided {} entries for {}, permitted {}", entries.size(), requestor, permitted.size());
        return permitted;
    }

    /** Number of entries decided, which is what a real decision point charges for. */
    public long getDecisions() {
        return decisionCount.get();
    }

    /** Number of batched invocations, which is what the round trips cost. */
    public long getCalls() {
        return callCount.get();
    }

    public long getPermits() {
        return permitCount.get();
    }

    public void reset() {
        decisionCount.set(0);
        callCount.set(0);
        permitCount.set(0);
    }

    /** Restores the permit-everything default. */
    public void permitAll() {
        rule = PERMIT_ALL;
    }

    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
