/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.flow.dedupe;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.flow.FlowException;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * A duplicate filter predicate that delegates predicate evaluation to 
 * {@link FlowManager#filterFlow(ManagedMessage)}.
 * 
 * @author Martin Krasser
 */
public class Dedupe implements Predicate<Exchange> {

    private static final Log LOG = LogFactory.getLog(Dedupe.class);
    
    @Autowired
    private FlowManager flowManager;
    
    /**
     * Sets the {@link FlowManager} used by this predicate. This property is
     * {@link Autowired} when used within a Spring 2.5 (or higher) container.
     * 
     * @param flowManager
     *            {@link FlowManager} used by this predicate.
     */
    public void setFlowManager(FlowManager flowManager) {
        this.flowManager = flowManager;
    }

    /**
     * Negates
     * {@link FlowManager#filterFlow(ManagedMessage)}.
     * 
     * @param exchange
     *            message exchange
     */
    public boolean matches(Exchange exchange) {
        try {
            return !flowManager.filterFlow(new PlatformMessage(exchange));
        } catch (FlowException e) {
            // thrown if flow id is unknown to flow manager
            LOG.warn("filter flow operation failed", e);
        } catch (Exception e) {
            // keep processing exchange (only log error)
            LOG.error(e);
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.apache.camel.Predicate#assertMatches(java.lang.String, java.lang.Object)
     */
    public void assertMatches(String text, Exchange exchange) throws AssertionError {
        if (!matches(exchange)) {
            throw new AssertionError(text);
        }
    }

}
