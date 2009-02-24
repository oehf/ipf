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

/**
 * Implemented by classes being able to replay message packets.
 * 
 * @author Martin Krasser
 */
public interface ReplayStrategy {
    
    /**
     * Returns this strategy's unique identifier.
     * 
     * @return this strategy's unique identifier.
     */
    String getIdentifier();
    
    /**
     * Replays <code>packet</code> and returns a optionally updated packet.
     * 
     * @param packet
     *            packet to be replayed.
     * @throws Exception
     *             if replay fails.
     * @return an optionally updated packet.
     */
    PlatformPacket replay(PlatformPacket packet) throws Exception;

    /**
     * Registers this strategy at a {@link ReplayStrategyRegistry}.
     */
    void register();

}
