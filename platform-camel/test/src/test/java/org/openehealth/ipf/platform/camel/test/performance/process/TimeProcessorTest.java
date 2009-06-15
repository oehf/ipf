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
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for <code>TimeProcessor</code>.
 * 
 * @author Mitko Kolev
 */
public class TimeProcessorTest extends MeasureProcessorTest {

    TimeProcessor processor;
    MeasurementHistory initialHistory;
    
    @Override
    @Before
    public void setUp() throws Exception{
        super.setUp();
        processor = new TimeProcessor();
        processor.process(exchange);
        initialHistory = processor.getMeasurementHistory(
                exchange);
    }
    @Test
    public void testReferenceDateIsNotChanged() throws Exception {
        Date referenceDate = initialHistory.getReferenceDate();
        processor.process(exchange);
        processor.process(exchange);
        assertEquals(referenceDate, processor.getMeasurementHistory(exchange).getReferenceDate());
    }
    
    @Test
    public void testMeasurementHistoryIsCopied() throws Exception {
        processor.process(exchange);
        MeasurementHistory newHistory = processor.getMeasurementHistory(
                exchange);
        assertNotSame(initialHistory, newHistory);
    }
    
    @Test
    public void testTimeIsUpdated()throws Exception {
        Measurement last = initialHistory.getLastMeasurement();
        processor.process(exchange);
        MeasurementHistory newHistory = processor.getMeasurementHistory(exchange);
        assertNotSame(last, newHistory.getLastMeasurement());
    }
    
     @Test
     public void testUpdatedTimeIsGreater()throws Exception {
         Measurement initial = initialHistory.getLastMeasurement();
         processor.process(exchange);
         MeasurementHistory history = processor.getMeasurementHistory(exchange);
         Measurement last = history.getLastMeasurement();
         assertTrue(last.getTimestamp().getNanoValue() > initial.getTimestamp().getNanoValue());
     }
   
}
