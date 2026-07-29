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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.vibur.objectpool.BasePool;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Shrinks idle Vibur object pools so that objects which are expensive to retain — XML parsers and
 * JAX-WS client stubs in particular — do not stay allocated after a traffic burst has passed.
 * <p>
 * This is the equivalent of Commons Pool's {@code timeBetweenEvictionRunsMillis} plus
 * {@code minEvictableIdleTime}, and it follows the same algorithm as Vibur's own
 * {@link org.vibur.objectpool.util.SamplingPoolReducer}: within each eviction interval the pool is
 * sampled {@link #SAMPLES_PROPERTY} times, the <em>smallest</em> observed number of idle objects is
 * remembered, and at the end of the interval that many objects are destroyed. Sampling the minimum
 * rather than the instantaneous count is what makes this safe — only objects that stayed idle for
 * the whole interval are considered evictable.
 * <p>
 * Unlike {@code SamplingPoolReducer}, which allocates one daemon thread per pool, this evictor
 * drives every registered pool from a single shared daemon thread. That matters because IPF creates
 * one client-stub pool per web-service endpoint, and those pools come and go with Camel routes.
 * Pools are held through a {@link WeakReference}, so a pool belonging to a discarded endpoint is
 * dropped automatically at the next run and neither the pool nor its objects are leaked.
 * <p>
 * Eviction is capped at {@link #MAX_REDUCTION_FRACTION} of the created objects per interval so that
 * a pool shrinks gradually instead of collapsing, and it never reduces a pool below its configured
 * initial size.
 * <p>
 * Setting {@link #EVICTION_INTERVAL_PROPERTY} to {@code 0} or a negative value disables eviction
 * altogether; no thread is then created.
 *
 * @since 6.0
 */
@Slf4j
@UtilityClass
public class PoolEvictor {

    /**
     * System property controlling the length of one eviction interval, in milliseconds.
     * Defaults to {@value #DEFAULT_INTERVAL_MILLIS}. Zero or negative disables eviction.
     */
    public static final String EVICTION_INTERVAL_PROPERTY = PoolEvictor.class.getName() + ".INTERVAL_MILLIS";

    /**
     * System property controlling how often a pool is sampled within one eviction interval.
     * Defaults to {@value #DEFAULT_SAMPLES}.
     */
    public static final String SAMPLES_PROPERTY = PoolEvictor.class.getName() + ".SAMPLES";

    private static final long DEFAULT_INTERVAL_MILLIS = 60_000L;
    private static final int DEFAULT_SAMPLES = 6;

    /** Largest fraction of the created objects that a single interval may destroy. */
    private static final double MAX_REDUCTION_FRACTION = 0.2;

    private static final long INTERVAL_MILLIS = Long.getLong(EVICTION_INTERVAL_PROPERTY, DEFAULT_INTERVAL_MILLIS);

    /** Package-visible so that tests can drive whole intervals deterministically. */
    static final int SAMPLES = Math.max(1, Integer.getInteger(SAMPLES_PROPERTY, DEFAULT_SAMPLES));

    private static final List<Entry> ENTRIES = new CopyOnWriteArrayList<>();

    private static ScheduledExecutorService scheduler;

    /**
     * Registers a pool for idle eviction. Registration does not prevent the pool from being garbage
     * collected; callers therefore need no matching deregistration step.
     *
     * @param pool the pool to shrink when it holds objects that stay idle, not {@code null}
     * @param name a short name used in log output
     */
    public static void register(BasePool pool, String name) {
        if (pool == null) {
            return;
        }
        if (INTERVAL_MILLIS <= 0) {
            log.debug("Idle eviction is disabled, not registering pool {}", name);
            return;
        }
        ENTRIES.add(new Entry(pool, name));
        ensureSchedulerStarted();
        log.debug("Registered pool {} for idle eviction every {} ms, sampled {} times per interval",
                name, INTERVAL_MILLIS, SAMPLES);
    }

    private static synchronized void ensureSchedulerStarted() {
        if (scheduler != null) {
            return;
        }
        scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            var thread = new Thread(runnable, "ipf-pool-evictor");
            thread.setDaemon(true);
            return thread;
        });
        var period = Math.max(1L, INTERVAL_MILLIS / SAMPLES);
        scheduler.scheduleWithFixedDelay(PoolEvictor::runOnce, period, period, TimeUnit.MILLISECONDS);
    }

    /**
     * One sampling tick. Never propagates an exception, because doing so would silently cancel the
     * scheduled task and stop eviction for every registered pool.
     * <p>
     * Package-visible so that tests can run complete intervals without depending on wall-clock time.
     */
    static void runOnce() {
        try {
            ENTRIES.forEach(entry -> {
                var pool = entry.ref.get();
                if (pool == null || pool.isTerminated()) {
                    ENTRIES.remove(entry);
                    log.debug("Pool {} is gone, removed from idle eviction", entry.name);
                    return;
                }
                entry.sample(pool);
                if (entry.intervalElapsed()) {
                    evict(pool, entry);
                }
            });
        } catch (Exception e) {
            log.warn("Idle eviction run failed", e);
        }
    }

    private static void evict(BasePool pool, Entry entry) {
        var reduction = entry.calculateReduction(pool);
        entry.resetInterval();
        if (reduction <= 0) {
            return;
        }
        try {
            var reduced = pool.reduceCreatedBy(reduction, false);
            log.debug("Evicted {} of {} idle objects from pool {}", reduced, reduction, entry.name);
        } catch (Exception e) {
            log.warn("Failed to evict idle objects from pool {}", entry.name, e);
        }
    }

    /**
     * Per-pool sampling state. Only ever touched from the single evictor thread, so it needs no
     * synchronization of its own.
     */
    private static final class Entry {
        private final WeakReference<BasePool> ref;
        private final String name;
        private int minRemainingCreated = Integer.MAX_VALUE;
        private int samplesTaken;

        Entry(BasePool pool, String name) {
            this.ref = new WeakReference<>(pool);
            this.name = name;
        }

        void sample(BasePool pool) {
            minRemainingCreated = Math.min(minRemainingCreated, pool.remainingCreated());
            samplesTaken++;
        }

        boolean intervalElapsed() {
            return samplesTaken >= SAMPLES;
        }

        void resetInterval() {
            minRemainingCreated = Integer.MAX_VALUE;
            samplesTaken = 0;
        }

        /**
         * Number of objects that stayed idle for the whole interval and may be destroyed, capped by
         * {@link #MAX_REDUCTION_FRACTION} and by the pool's initial size.
         */
        int calculateReduction(BasePool pool) {
            if (minRemainingCreated == Integer.MAX_VALUE) {
                return 0;
            }
            var createdTotal = pool.createdTotal();
            var maxReduction = (int) Math.ceil(createdTotal * MAX_REDUCTION_FRACTION);
            var reduction = Math.min(minRemainingCreated, maxReduction);
            reduction = Math.min(reduction, createdTotal - pool.initialSize());
            return Math.max(reduction, 0);
        }
    }
}
