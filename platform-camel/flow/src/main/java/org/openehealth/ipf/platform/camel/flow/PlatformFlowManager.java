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
package org.openehealth.ipf.platform.camel.flow;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.FlowManagerBase;
import org.openehealth.ipf.commons.flow.FlowReplayException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A <a href="http://activemq.apache.org/camel">Camel</a>-specific
 * {@link FlowManager} implementation. Instances of this class are
 * {@link Autowired} with a {@link ReplayStrategy} instance if managed within a
 * Spring application context.
 * 
 * @author Martin Krasser
 */
public class PlatformFlowManager extends FlowManagerBase implements ReplayStrategyRegistry {

    private static final Log LOG = LogFactory.getLog(PlatformFlowManager.class);
    
    private Map<String, ReplayStrategy> replayStrategies;
    
    /**
     * Creates a new {@link PlatformFlowManager}.
     */
    public PlatformFlowManager() {
        this.replayStrategies = new HashMap<String, ReplayStrategy>();
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.flow.ReplayStrategyRegistry#register(org.openehealth.ipf.platform.camel.flow.ReplayStrategy)
     */
    public void register(ReplayStrategy replayStrategy) {
        replayStrategies.put(replayStrategy.getIdentifier(), replayStrategy);
        LOG.info("Registered replay strategy with identifier " + replayStrategy.getIdentifier());
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.flow.ReplayStrategyRegistry#unregister(org.openehealth.ipf.platform.camel.flow.ReplayStrategy)
     */
    public void unregister(ReplayStrategy replayStrategy) {
        replayStrategies.remove(replayStrategy.getIdentifier());
        LOG.info("Unregistered replay strategy with identifier " + replayStrategy.getIdentifier());
    }
    
    /**
     * Delegates replay of the <code>packet</code> to
     * {@link ReplayStrategy#replay(PlatformPacket)}.
     * 
     * @param packet
     *            packet to be replayed.
     * @throws Exception
     *             if replay fails.
     * @return an optionally updated packet.
     * 
     * @see PlatformPacket#deserialize(byte[])
     * @see PlatformPacket#serialize()
     */
    @Override
    protected byte[] replayFlow(byte[] packet) throws Exception {
        PlatformPacket input = PlatformPacket.deserialize(packet);
        ReplayStrategy strategy = getReplayStrategy(input);
        if (strategy == null) {
            throw new FlowReplayException("no replay strategy found");
        }
        return strategy.replay(input).serialize();
    }

    private ReplayStrategy getReplayStrategy(PlatformPacket packet) {
        return replayStrategies.get(packet.getReplayStrategyId());
    }
    
}
