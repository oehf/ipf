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
package org.openehealth.ipf.commons.flow;

import org.openehealth.ipf.commons.flow.history.SplitHistory;

/**
 * Interface that defines the contract between a platform-specific message and
 * the {@link FlowManager}.
 * 
 * @author Martin Krasser
 */
public interface ManagedMessage {
    
    /**
     * Creates a byte array representation of this message. This representation
     * is used by the {@link FlowManager} to store and replay messages.
     * Implementations should not cache this representation as it must reflect
     * changes made to this message via {@link #setFlowId(Long)} and
     * {@link #setSplitHistory(SplitHistory)}. Implementations must be able to
     * re-create native messages from this representation.
     * 
     * @return a byte array representation of this message.
     */
    byte[] createPacket();
    
    /** 
     * Converts the content of this ManagedMessage to a {@link String}.
     *   
     * @return the rendered message.
     */
    public String render();
    
    /**
     * Returns the flow identifier of this message.
     * 
     * @return the flow identifier of this message.
     */
    Long getFlowId();
    
    /**
     * Sets the flow identifier of this message.
     * 
     * @param flowId
     *            flow identifier
     */
    void setFlowId(Long flowId);
    
    /**
     * Returns the {@link SplitHistory} of this message.
     * 
     * @return the {@link SplitHistory} of this message.
     */
    SplitHistory getSplitHistory();
    
    /**
     * Sets the {@link SplitHistory} of this message.
     * 
     * @param history
     *            split history.
     */
    void setSplitHistory(SplitHistory history);
   
}
