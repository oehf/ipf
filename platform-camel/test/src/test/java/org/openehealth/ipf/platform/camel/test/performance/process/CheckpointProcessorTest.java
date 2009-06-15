/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.test.performance.process;

import java.util.Date;
import java.util.List;

import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for <code>CheckpointProcessor</code>
 * 
 * @author Mitko Kolev
 */
public class CheckpointProcessorTest extends MeasureProcessorTest {

    CheckpointProcessor processor;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        processor = new CheckpointProcessor("this name");

    }

    @Test
    public void testReferenceDateIsNotChanged() throws Exception {
        measure().time(exchange);
        Date time = processor.getMeasurementHistory(exchange)
                .getReferenceDate();

        processor.process(exchange);

        assertEquals(time, processor.getMeasurementHistory(exchange)
                .getReferenceDate());
    }

    @Test
    public void testMeasurementIsAdded() throws Exception {
        measure().time(exchange);

        MeasurementHistory initialHistory = processor
                .getMeasurementHistory(exchange);

        List<Measurement> measurementsBefore = initialHistory.getMeasurements();

        processor.process(exchange);

        MeasurementHistory newHistory = processor
                .getMeasurementHistory(exchange);
        List<Measurement> measurementsAfter = newHistory.getMeasurements();
        assertEquals(measurementsBefore.size() + 1, measurementsAfter.size());
    }

    @Test
    public void testNameIsSetInTheMeasurement() throws Exception {
        measure().time(exchange);
        processor.process(exchange);
        MeasurementHistory history = processor.getMeasurementHistory(exchange);
        Measurement m = history.getLastMeasurement();
        assertEquals("this name", m.getName());
    }

    @Test
    public void testMeasurementHistoryIsCopied() throws Exception {
        measure().time(exchange);

        MeasurementHistory initialHistory = processor
                .getMeasurementHistory(exchange);

        processor.process(exchange);
        MeasurementHistory newHistory = processor
                .getMeasurementHistory(exchange);
        assertNotSame(initialHistory, newHistory);
    }

    @Test
    public void testMeasurementHistoryIsCopiedAdvanced() throws Exception {
        measure().time(exchange);
        processor.process(exchange);
        processor.process(exchange);
        Message message = exchange.getIn();
        // simulate the flow is split
        Message copiedMessage = message.copy();
        processor.process(exchange);
        processor.process(exchange);

        MeasurementHistory history = processor.getMeasurementHistory(exchange);

        exchange.setIn(copiedMessage);
        MeasurementHistory historyOfCopiedMessage = processor
                .getMeasurementHistory(exchange);

        assertEquals(5, history.getMeasurements().size());
        assertEquals(3, historyOfCopiedMessage.getMeasurements().size());
    }

}
