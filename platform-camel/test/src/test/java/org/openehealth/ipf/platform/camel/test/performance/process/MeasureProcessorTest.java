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

import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Base test for subclasses of <code>TimeProcessor</code>.
 * 
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-application.xml" })
public abstract class MeasureProcessorTest {
    @Autowired
    private CamelContext context;

    /**
     * Helper exchange, created in {@link #setUp()} 
     */
    protected Exchange exchange;

    @Before
    public void setUp() throws Exception {
        exchange = Exchanges.createExchange(context, ExchangePattern.InOnly);
    }

    /**
     * Simulates measure().time() updating the given <code>exchange</code>
     * 
     * @param exchange
     * @return
     */
    protected Measurement time(Exchange exchange) {
        try {
            TimeProcessor processor = new TimeProcessor();
            processor.process(exchange);
            MeasurementHistory history = processor
                    .getMeasurementHistory(exchange);
            return history.getFirstMeasurement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Simulates measure().checkpoint(String) updating the given
     * <code>exchange</code>
     * 
     * @param exchange
     * @return
     */
    protected Measurement checkpoint(Exchange exchange) throws Exception {
        CheckpointProcessor processor = new CheckpointProcessor(UUID
                .randomUUID().toString());
        processor.process(exchange);
        MeasurementHistory history = processor.getMeasurementHistory(exchange);
        return history.getLastMeasurement();
    }

    /**
     * Simulates the measure() DSL extension
     * 
     * @return the <code>this</code> instance
     */
    MeasureProcessorTest measure() {
        return this;
    }

}
