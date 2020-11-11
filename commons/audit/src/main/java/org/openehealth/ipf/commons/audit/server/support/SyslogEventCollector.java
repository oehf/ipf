/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit.server.support;

import com.github.palindromicity.syslog.dsl.SyslogFieldKeys;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.unmarshal.AuditParser;
import org.openehealth.ipf.commons.audit.unmarshal.dicom.DICOMAuditParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A simple collector of Syslog events
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class SyslogEventCollector implements Consumer<Map<String, Object>> {

    private static final Logger LOG = LoggerFactory.getLogger(SyslogEventCollector.class);
    private static final AuditParser PARSER = new DICOMAuditParser();
    private final Collection<Map<String, Object>> syslogMaps = new ConcurrentLinkedQueue<>();

    public static SyslogEventCollector newInstance() {
        return new SyslogEventCollector();
    }

    public SyslogEventCollector withExpectation(Predicate<Map<String, Object>> predicate, int expectedElements) {
        return new WithExpectation(this, predicate, expectedElements);
    }

    public SyslogEventCollector withExpectation(int expectedElements) {
        return new WithExpectation(this, expectedElements);
    }

    public SyslogEventCollector withDelay(long delay) {
        return new WithDelay(this, delay);
    }

    private SyslogEventCollector() {
    }

    @Override
    public void accept(Map<String, Object> syslogMap) {
        LOG.debug("Collecting syslog event {}", syslogMap);
        syslogMaps.add(syslogMap);
    }

    public Collection<Map<String, Object>> getSyslogEvents() {
        return getSyslogEvents(syslogEvent -> true);
    }

    public Collection<Map<String, Object>> getSyslogEvents(Predicate<Map<String, Object>> predicate) {
        return syslogMaps.stream()
                .filter(predicate)
                .collect(Collectors.toUnmodifiableList());
    }

    public void clear() {
        syslogMaps.clear();
    }

    public static AuditMessage parse(Map<String, Object> syslogMap, boolean validate) {
        return PARSER.parse(syslogMap.get(SyslogFieldKeys.MESSAGE.getField()).toString(), validate);
    }

    public boolean await(long timeout, TimeUnit timeUnit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    private static class WithExpectation extends SyslogEventCollector {

        private final Predicate<Map<String, Object>> predicate;
        private final CountDownLatch latch;
        private final SyslogEventCollector wrapped;

        private WithExpectation(SyslogEventCollector wrapped, int expectedElements) {
            this(wrapped, m -> true, expectedElements);
        }

        private WithExpectation(SyslogEventCollector wrapped, Predicate<Map<String, Object>> predicate, int expectedElements) {
            this.wrapped = wrapped;
            this.predicate = predicate;
            this.latch = new CountDownLatch(expectedElements);
        }

        @Override
        public void accept(Map<String, Object> syslogMap) {
            if (predicate.test(syslogMap)) {
                wrapped.accept(syslogMap);
                latch.countDown();
            }
        }

        @Override
        public boolean await(long timeout, TimeUnit timeUnit) throws InterruptedException {
            return latch.await(timeout, timeUnit);
        }

        @Override
        public Collection<Map<String, Object>> getSyslogEvents() {
            return wrapped.getSyslogEvents();
        }

        @Override
        public Collection<Map<String, Object>> getSyslogEvents(Predicate<Map<String, Object>> predicate) {
            return wrapped.getSyslogEvents(predicate);
        }

        public long missingElements() {
            return latch.getCount();
        }

    }

    private static class WithDelay extends SyslogEventCollector {
        private final long delay;
        private final SyslogEventCollector wrapped;

        public WithDelay(SyslogEventCollector wrapped, long delay) {
            this.delay = delay;
            this.wrapped = wrapped;
        }

        @Override
        public void accept(Map<String, Object> syslogMap) {
            try {
                Thread.sleep(delay);
                wrapped.accept(syslogMap);
            } catch (InterruptedException ignored) {
            }
        }

        @Override
        public boolean await(long timeout, TimeUnit timeUnit) throws InterruptedException {
            return wrapped.await(timeout, timeUnit);
        }

        @Override
        public Collection<Map<String, Object>> getSyslogEvents() {
            return wrapped.getSyslogEvents();
        }

        @Override
        public Collection<Map<String, Object>> getSyslogEvents(Predicate<Map<String, Object>> predicate) {
            return wrapped.getSyslogEvents(predicate);
        }
    }
}
