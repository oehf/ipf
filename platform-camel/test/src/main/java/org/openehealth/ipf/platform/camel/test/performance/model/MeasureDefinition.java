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
package org.openehealth.ipf.platform.camel.test.performance.model;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.test.performance.dispatcher.MeasurementDispatcher;
import org.openehealth.ipf.platform.camel.core.model.DelegateDefinition;
import org.openehealth.ipf.platform.camel.core.util.Contexts;
import org.openehealth.ipf.platform.camel.test.performance.process.CheckpointProcessor;
import org.openehealth.ipf.platform.camel.test.performance.process.FinishProcessor;
import org.openehealth.ipf.platform.camel.test.performance.process.TimeProcessor;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Extension type to support performance measurement.
 * 
 * @author Mitko Kolev
 */
public class MeasureDefinition extends DelegateDefinition {
    private final static Logger LOG = LoggerFactory.getLogger(MeasureDefinition.class);

    private TimeProcessor delegate;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openehealth.ipf.platform.camel.core.model.DelegateType#doCreateDelegate
     * (org.apache.camel.spi.RouteContext)
     */
    @Override
    protected Processor doCreateDelegate(RouteContext routeContext)
            throws Exception {

        CamelContext camelContext = routeContext.getCamelContext();
        MeasurementDispatcher measurementDispatcher = Contexts.bean(
                MeasurementDispatcher.class, camelContext);
        LOG.debug("{} found.", MeasurementDispatcher.class.getName());
        delegate.setMeasurementDispatcher(measurementDispatcher);

        return delegate;
    }

    /**
     * Defines the initial measurement point
     */
    public MeasureDefinition time() {
        delegate = new TimeProcessor();
        LOG.debug("Using {}", TimeProcessor.class.getName());
        return this;
    }

    /**
     * Defines a measurement checkpoint, between the initial and the last measurement points
     */
    public MeasureDefinition checkpoint(String name) {
        notNull(name, "The name must not be null!");
        delegate = new CheckpointProcessor(name);
        LOG.debug("Using {} with name {}", CheckpointProcessor.class.getName(), name);
        return this;
    }

    /**
     * Defines the last measurement point
     */
    public MeasureDefinition finish(String name) {
        notNull(name, "The name must not be null!");
        delegate = new FinishProcessor(name);
        LOG.debug("Using {} with name {}", FinishProcessor.class.getName(), name);
        return this;
    }

}
