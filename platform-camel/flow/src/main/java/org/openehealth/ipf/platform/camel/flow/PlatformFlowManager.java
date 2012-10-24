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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(PlatformFlowManager.class);
    
    private final Map<String, ReplayStrategy> replayStrategies;
    
    /**
     * Creates a new {@link PlatformFlowManager}.
     */
    public PlatformFlowManager() {
        replayStrategies = Collections.synchronizedMap(new HashMap<String, ReplayStrategy>());
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.flow.ReplayStrategyRegistry#register(org.openehealth.ipf.platform.camel.flow.ReplayStrategy)
     */
    @Override
    public ReplayStrategyRegistration register(ReplayStrategy replayStrategy) {
        replayStrategies.put(replayStrategy.getIdentifier(), replayStrategy);
        LOG.info("Registered replay strategy with identifier " + replayStrategy.getIdentifier() + " at flow manager");
        return new LocalReplayStrategyRegistration(replayStrategy);
    }
    
    /**
     * Delegates replay of the <code>packet</code> to
     * {@link ReplayStrategy#replay(PlatformPacket)}.
     * 
     * @param packet
     *            serialized {@link PlatformPacket} to be replayed.
     * @throws Exception
     *             if replay fails.
     * @return an optionally updated serialized {@link PlatformPacket}.
     * 
     * @see PlatformPacket#deserialize(byte[])
     * @see PlatformPacket#serialize()
     */
    @Override
    protected byte[] replayFlow(byte[] packet) throws Exception {
        return replayFlow(PlatformPacket.deserialize(packet)).serialize();
    }

    /**
     * Delegates replay of the <code>packet</code> to
     * {@link ReplayStrategy#replay(PlatformPacket)}.
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
        ReplayStrategy replayStrategy = replayStrategies.get(replayStrategyId);
        if (replayStrategy == null) {
            throw new FlowReplayException("replay strategy " + replayStrategyId + " not found");
        }
        return replayStrategy.replay(packet);
        
    }
    
    private class LocalReplayStrategyRegistration implements ReplayStrategyRegistration {
     
        private final ReplayStrategy replayStrategy;
        
        public LocalReplayStrategyRegistration(ReplayStrategy replayStrategy) {
            this.replayStrategy = replayStrategy;
        }

        @Override
        public void terminate() {
            replayStrategies.remove(replayStrategy.getIdentifier());
            LOG.info("Unregistered replay strategy with identifier " + replayStrategy.getIdentifier() + " from flow manager");
        }
        
    }
    
}
