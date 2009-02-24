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
package org.openehealth.ipf.platform.camel.flow.osgi;

import static org.openehealth.ipf.platform.camel.flow.osgi.OsgiReplayStrategyRegistry.REPLAY_STRATEGY_IDENTIFIER_KEY;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.flow.FlowReplayException;
import org.openehealth.ipf.platform.camel.flow.PlatformFlowManager;
import org.openehealth.ipf.platform.camel.flow.PlatformPacket;
import org.openehealth.ipf.platform.camel.flow.ReplayStrategy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.osgi.context.BundleContextAware;

/**
 * @author Martin Krasser
 */
public class OsgiPlatformFlowManager extends PlatformFlowManager implements BundleContextAware {

    private static final Log LOG = LogFactory.getLog(OsgiReplayStrategyRegistry.class);
    
private BundleContext bundleContext;
    
    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /**
     * Delegates replay of the <code>packet</code> to
     * {@link ReplayStrategy#replay(PlatformPacket)}. The replay strategy is
     * looked up from the OSGi service registry.
     * 
     * @param packet
     *            {@link PlatformPacket} to be replayed.
     * @throws Exception
     *             if replay fails.
     * @return an optionally updated {@link PlatformPacket}.
     * 
     * @see PlatformPacket#deserialize(byte[])
     * @see PlatformPacket#serialize()
     */
    protected PlatformPacket replayFlow(PlatformPacket packet) throws Exception {
        String replayStrategyId = packet.getReplayStrategyId();
        ServiceReference reference = getReplayStrategyReference(replayStrategyId);
        if (reference == null) {
            throw new FlowReplayException("replay strategy " + replayStrategyId + 
                    " not found in OSGi service registry");
        }
        // In the following we deliberately ingnore that the service reference might be stale.  
        ReplayStrategy replayStrategy = (ReplayStrategy)bundleContext.getService(reference);
        LOG.debug("Using replay strategy " + replayStrategyId + " from OSGi service registry");
        try {
            return replayStrategy.replay(packet);
        } finally {
            bundleContext.ungetService(reference);
        }
    }

    private ServiceReference getReplayStrategyReference(String replayStrategyId) throws Exception {
        ServiceReference[] references = bundleContext.getServiceReferences(ReplayStrategy.class.getName(), 
                createSearchFilterExpression(replayStrategyId));
        return (references == null) ? null : references[0]; 
    }
    
    private static String createSearchFilterExpression(String replayStrategyId) {
        return "(" + REPLAY_STRATEGY_IDENTIFIER_KEY + "=" + replayStrategyId + ")";
    }
}
