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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.flow.FlowReplayException;
import org.openehealth.ipf.platform.camel.flow.PlatformFlowManager;
import org.openehealth.ipf.platform.camel.flow.PlatformPacket;
import org.openehealth.ipf.platform.camel.flow.ReplayStrategy;

/**
 * @author Martin Krasser, Boris Stanojevic
 */
public class OsgiPlatformFlowManager extends PlatformFlowManager {

    private static final Log LOG = LogFactory.getLog(OsgiPlatformFlowManager.class);

    private List<ReplayStrategy> replayStrategies;

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
        ReplayStrategy replayStrategy = getReplayStrategy(replayStrategyId);
        if (replayStrategy == null) {
            throw new FlowReplayException("replay strategy " + replayStrategyId
                    + " not found in OSGi service registry");
        }
        LOG.debug("Using replay strategy " + replayStrategyId + " from OSGi service registry");
        return replayStrategy.replay(packet);
    }

    private ReplayStrategy getReplayStrategy(String replayStrategyId) throws Exception {
        Iterator<ReplayStrategy> iterator = getReplayStrategies().iterator();
        while (iterator.hasNext()) {
            ReplayStrategy replayStrategy = iterator.next();
            if (replayStrategy.getIdentifier().equals(replayStrategyId)) {
                return replayStrategy;
            }
        }
        return null;
    }

    public List<ReplayStrategy> getReplayStrategies() {
        return replayStrategies;
    }

    public void setReplayStrategies(List<ReplayStrategy> replayStrategies) {
        this.replayStrategies = replayStrategies;
        LOG.debug("Replay strategies list was set");
    }
}
