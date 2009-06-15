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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.dispatcher.MeasurementDispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for <code>FinishProcessor</code>
 * 
 * @author Mitko Kolev
 */
public class FinishProcessorTest extends MeasureProcessorTest {

    FinishProcessor processor;
    MeasurementDispatcherMock dispatcher;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        processor = new FinishProcessor("finish");
        dispatcher = new MeasurementDispatcherMock();
        processor.setMeasurementDispatcher(dispatcher);
    }

    @Test
    public void testDispatcherIsSet() throws Exception {
        assertEquals(dispatcher, processor.getMeasurementDispatcher());
    }

    @Test
    public void testMeasurementHistoryIsDispatched() throws Exception {
        measure().time(exchange);
        measure().checkpoint(exchange);
        measure().time(exchange);
        processor.process(exchange);
        assertNotNull(dispatcher.getLastMeasurementHistory());
    }

    @Test
    public void testProcessReturnsPerofrmanceDataWithInitialTime()
            throws Exception {
        measure().time(exchange);
        measure().checkpoint(exchange);
        Date initialDate = processor.getMeasurementHistory(exchange)
                .getReferenceDate();

        // update the test dispatcher
        processor.process(exchange);
        // check the data in the test dispatcher
        assertEquals(initialDate, dispatcher.getLastMeasurementHistory()
                .getReferenceDate());
    }

    static class MeasurementDispatcherMock extends MeasurementDispatcher {
        private MeasurementHistory lastMeasurementHistory;

        @Override
        public void dispatch(MeasurementHistory measurementHistory) {
            lastMeasurementHistory = measurementHistory;

        }

        public MeasurementHistory getLastMeasurementHistory() {
            return lastMeasurementHistory;
        }

    }

}
