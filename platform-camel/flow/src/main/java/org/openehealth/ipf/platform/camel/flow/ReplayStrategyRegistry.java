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
 * A registry for {@link ReplayStrategy}s.
 * 
 * @author Martin Krasser
 */
public interface ReplayStrategyRegistry {

    /**
     * Registers the <code>replayStrategy</code> under the id returned by
     * {@link ReplayStrategy#getIdentifier()}.
     * 
     * @param replayStrategy
     *            a replay strategy.
     */
    public void register(ReplayStrategy replayStrategy);
    
    /**
     * Unregisters the <code>replayStrategy</code> if it has been previously
     * registered under the id returned by
     * {@link ReplayStrategy#getIdentifier()}.
     * 
     * @param replayStrategy
     *            a replay strategy.
     */
    public void unregister(ReplayStrategy replayStrategy);
    
}
