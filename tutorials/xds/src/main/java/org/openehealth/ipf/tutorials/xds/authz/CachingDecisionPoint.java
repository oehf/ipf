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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Remembers verdicts, so that the same object is decided once per requestor rather than once per page.
 * <p>
 * {@code startIndex = 100} means "the hundred-and-first document this requestor may see", and there is no
 * way to find it except to decide the hundred before it -- so over a sequence of pages the cost is
 * quadratic: page <i>k</i> re-decides everything pages 0…<i>k</i>-1 already decided. The prefix still gets
 * <em>scanned</em> on every page, which is cheap, but it is decided only once, which is the part that
 * costs. The overshoot of the block that filled a window turns from loss into prefetch for the same
 * reason: the next page finds it cached.
 * <p>
 * A decorator rather than a feature of {@link AuthorizedPager}: the pager asks a decision point and does
 * not care whether the answer came from a policy engine or from memory, and a deployment that must not
 * cache simply leaves this out.
 * <p>
 * Cache entries are keyed by requestor and entry UUID, and {@link #invalidate()} discards them all.
 * Anything a consent change touches has to call it: a stale permit is a disclosure, where a stale denial
 * is only an annoyance, so invalidation must be driven by consent updates rather than by a timer alone.
 *
 * @author Christian Ohr
 * @see AuthorizationDecisionPoint
 */
@Slf4j
public class CachingDecisionPoint implements AuthorizationDecisionPoint {

    /** The decision point that decides what is not cached. */
    @Getter @Setter
    private AuthorizationDecisionPoint delegate;

    /**
     * Unbounded, and node-local, like everything else this tutorial keeps in memory. A deployment wants a
     * bounded one -- Caffeine, or the Spring cache abstraction that {@code SpringCachePagingProvider}
     * uses -- and a distributed one has to carry the epoch in the key, since it cannot clear every node
     * at once.
     */
    private final Map<String, Boolean> verdicts = new ConcurrentHashMap<>();

    private final AtomicLong hitCount = new AtomicLong();
    private final AtomicLong missCount = new AtomicLong();

    /**
     * Answers from the cache what it can and asks the delegate for the rest -- in one call, so that the
     * batching the block size was chosen for survives the caching.
     */
    @Override
    public Set<String> decide(String requestor, List<? extends XDSMetaClass> entries) {
        var permitted = new HashSet<String>();
        var misses = new ArrayList<XDSMetaClass>(entries.size());

        for (var entry : entries) {
            var verdict = verdicts.get(key(requestor, entry.getEntryUuid()));
            if (verdict == null) {
                misses.add(entry);
            } else {
                hitCount.incrementAndGet();
                if (verdict) {
                    permitted.add(entry.getEntryUuid());
                }
            }
        }

        if (!misses.isEmpty()) {
            var decided = delegate.decide(requestor, misses);
            missCount.addAndGet(misses.size());
            for (var entry : misses) {
                var permit = decided.contains(entry.getEntryUuid());
                verdicts.put(key(requestor, entry.getEntryUuid()), permit);
                if (permit) {
                    permitted.add(entry.getEntryUuid());
                }
            }
        }

        log.debug("{} of {} verdicts came from the cache", entries.size() - misses.size(), entries.size());
        return permitted;
    }

    /**
     * Discards every cached verdict, which is what a consent change has to do -- the node-local spelling
     * of advancing the policy epoch.
     */
    public void invalidate() {
        verdicts.clear();
    }

    public long getHits() {
        return hitCount.get();
    }

    public long getMisses() {
        return missCount.get();
    }

    public void reset() {
        hitCount.set(0);
        missCount.set(0);
    }

    private static String key(String requestor, String entryUuid) {
        return requestor + '|' + entryUuid;
    }
}
