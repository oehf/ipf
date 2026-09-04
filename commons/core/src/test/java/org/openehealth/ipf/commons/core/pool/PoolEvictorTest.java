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
package org.openehealth.ipf.commons.core.pool;

import org.junit.jupiter.api.Test;
import org.vibur.objectpool.ConcurrentPool;
import org.vibur.objectpool.PoolObjectFactory;
import org.vibur.objectpool.PoolService;
import org.vibur.objectpool.util.ConcurrentLinkedQueueCollection;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Drives {@link PoolEvictor#runOnce()} directly instead of waiting for its scheduler, so the
 * assertions do not depend on wall-clock timing.
 */
class PoolEvictorTest {

    private final AtomicInteger created = new AtomicInteger();
    private final AtomicInteger destroyed = new AtomicInteger();

    private PoolService<Object> newPool(int maxSize) {
        return new ConcurrentPool<>(new ConcurrentLinkedQueueCollection<>(), new CountingFactory(), 0, maxSize, false);
    }

    /** Runs exactly one full eviction interval. */
    private static void runOneInterval() {
        for (var i = 0; i < PoolEvictor.SAMPLES; i++) {
            PoolEvictor.runOnce();
        }
    }

    @Test
    void evictsObjectsThatStayIdle() {
        var pool = newPool(20);
        PoolEvictor.register(pool, "evictsObjectsThatStayIdle");

        // Allocate 10 objects, then hand them all back so they are idle for the whole interval.
        var taken = new ArrayList<Object>();
        for (var i = 0; i < 10; i++) {
            taken.add(pool.take());
        }
        taken.forEach(pool::restore);

        assertEquals(10, pool.createdTotal());
        assertEquals(10, pool.remainingCreated());

        runOneInterval();

        // Reduction is capped at 20% of the created objects per interval, so exactly 2 go away.
        assertEquals(8, pool.createdTotal(), "one interval should evict 20% of the created objects");
        assertEquals(2, destroyed.get(), "evicted objects must be destroyed, not just dropped");

        // Repeated quiet intervals keep shrinking the pool.
        runOneInterval();
        assertTrue(pool.createdTotal() < 8, "a second quiet interval should shrink the pool further");
    }

    @Test
    void doesNotEvictObjectsCurrentlyInUse() {
        var pool = newPool(20);
        PoolEvictor.register(pool, "doesNotEvictObjectsCurrentlyInUse");

        // Every object is taken, so none is ever idle during the interval.
        var taken = new ArrayList<Object>();
        for (var i = 0; i < 5; i++) {
            taken.add(pool.take());
        }
        assertEquals(0, pool.remainingCreated());

        runOneInterval();

        assertEquals(5, pool.createdTotal(), "objects in use must not be evicted");
        assertEquals(0, destroyed.get());

        taken.forEach(pool::restore);
    }

    @Test
    void dropsTerminatedPoolsWithoutFailing() {
        var pool = newPool(5);
        PoolEvictor.register(pool, "dropsTerminatedPools");
        pool.take();
        pool.terminate();

        // A terminated pool must be discarded quietly rather than breaking the shared evictor.
        runOneInterval();
        runOneInterval();
    }

    @Test
    void capacityIsNotLostWhenAnObjectIsRestoredAsInvalid() {
        var pool = newPool(2);

        // Mirrors DomBuildersPool.restore(): an object whose reset() failed is handed back as invalid
        // so that it is destroyed while its capacity is still released.
        var first = pool.take();
        pool.restore(first, false);
        assertEquals(1, destroyed.get());

        // If restoring an invalid object leaked capacity, these takes would block forever.
        var a = pool.take();
        var b = pool.take();
        assertEquals(2, pool.taken(), "invalid restore must release capacity");
        pool.restore(a);
        pool.restore(b);
    }

    private class CountingFactory implements PoolObjectFactory<Object> {
        @Override
        public Object create() {
            created.incrementAndGet();
            return new Object();
        }

        @Override
        public boolean readyToTake(Object obj) {
            return true;
        }

        @Override
        public boolean readyToRestore(Object obj) {
            return true;
        }

        @Override
        public void destroy(Object obj) {
            destroyed.incrementAndGet();
        }
    }
}
