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

import static org.apache.commons.lang.Validate.notNull;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.test.performance.dispatcher.MeasurementDispatcher;
import org.openehealth.ipf.platform.camel.core.model.DelegateDefinition;
import org.openehealth.ipf.platform.camel.core.util.Contexts;
import org.openehealth.ipf.platform.camel.test.performance.process.CheckpointProcessor;
import org.openehealth.ipf.platform.camel.test.performance.process.FinishProcessor;
import org.openehealth.ipf.platform.camel.test.performance.process.TimeProcessor;

/**
 * Extension type to support performance measurement.
 * 
 * @author Mitko Kolev
 * @dsl platform-camel-test
 */
public class MeasureDefinition extends DelegateDefinition {
    private final static Log LOG = LogFactory.getLog(MeasureDefinition.class);

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
        LOG.debug(MeasurementDispatcher.class.getName() + " found.");
        delegate.setMeasurementDispatcher(measurementDispatcher);

        return delegate;
    }

    /**
     * Defines the initial measurement point
     * @ipfdoc Performance Measurement#dsl-extensions
     * @dsl platform-camel-test
     */
    public MeasureDefinition time() {
        this.delegate = new TimeProcessor();
        LOG.debug("Using " + TimeProcessor.class.getName());
        return this;
    }

    /**
     * Defines a measurement checkpoint, between the initial and the last measurement points
     * @ipfdoc Performance Measurement#dsl-extensions
     * @dsl platform-camel-test
     */
    public MeasureDefinition checkpoint(String name) {
        notNull(name, "The name must not be null!");
        this.delegate = new CheckpointProcessor(name);
        LOG.debug("Using " + CheckpointProcessor.class.getName() + " with name "
                + name);
        return this;
    }

    /**
     * Defines the last measurement point
     * @ipfdoc Performance Measurement#dsl-extensions
     * @dsl platform-camel-test
     */
    public MeasureDefinition finish(String name) {
        notNull(name, "The name must not be null!");
        this.delegate = new FinishProcessor(name);
        LOG.debug("Using " + FinishProcessor.class.getName() + " with name "
                + name);
        return this;
    }

}
