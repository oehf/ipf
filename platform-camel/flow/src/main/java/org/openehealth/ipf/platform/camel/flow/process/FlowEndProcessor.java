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
package org.openehealth.ipf.platform.camel.flow.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.flow.FlowException;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;


/**
 * A processor that triggers a
 * {@link FlowManager#acknowledgeFlow(ManagedMessage)} operation.
 * 
 * @author Martin Krasser
 */
public class FlowEndProcessor extends FlowProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(FlowEndProcessor.class);
    
    @Override
    public String toString() {
        return "FlowEndProcessor[" + getProcessor() + "]";
    }

    /**
     * Delegates to {@link FlowManager#acknowledgeFlow(ManagedMessage)}
     * 
     * @param message
     *            managed message.
     */
    @Override
    protected void processMessage(PlatformMessage message) {
        if (message.isFailed()) {
            return;
        }
        try {
            flowManager.acknowledgeFlow(message);
        } catch (FlowException e) {
            // thrown if flow id is unknown to flow manager
            LOG.warn("acknowledge flow operation failed", e);
        } catch (Exception e) {
            // keep processing exchange (only log error)
            LOG.error(e.getMessage(), e);
        }
        
    }

}
