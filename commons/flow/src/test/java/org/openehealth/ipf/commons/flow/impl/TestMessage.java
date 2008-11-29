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
package org.openehealth.ipf.commons.flow.impl;

import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.commons.flow.history.SplitHistory;

/**
 * @author Martin Krasser
 */
public class TestMessage implements ManagedMessage {

    private Long flowId;
    
    private final String implementation;
    
    private SplitHistory history;
    
    public TestMessage(String implementation) {
        this.history = new SplitHistory();
        this.implementation = implementation;
    }
    
    @Override
    public byte[] createPacket() {
        return implementation.getBytes();
    }

    @Override
    public Long getFlowId() {
        return flowId;
    }

    @Override
    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    @Override
    public SplitHistory getSplitHistory() {
        return history;
    }

    @Override
    public void setSplitHistory(SplitHistory history) {
        this.history = history;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.flow.ManagedMessage#render()
     */
    @Override
    public String render() {
        return "Rendered: " + implementation; 
    }
}
