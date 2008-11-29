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

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.commons.flow.history.SplitHistory;

/**
 * A Camel-specific {@link ManagedMessage} implementation. It acts as adapter to
 * a {@link Exchange}.
 * 
 * @author Martin Krasser
 */
public class PlatformMessage implements ManagedMessage {

    /**
     * Header name of flow identifier. 
     */
    public static final String FLOW_ID_KEY = "flow.id"; 
    
    /**
     * Header name of flow split history.
     */
    public static final String FLOW_SPLIT_HISTORY = "flow_split_history";
    
    private PlatformPacketFactory packetFactory;
    
    private PlatformMessageRenderer messageRenderer;
    
    private final Exchange exchange;
    
    /**
     * Creates a new {@link PlatformMessage} with the given
     * <code>exchange</code>.
     * 
     * @param exchange a message exchange.
     */
    public PlatformMessage(Exchange exchange) {
        this.exchange = exchange;
    }
    
    /**
     * Returns the contained exchange.
     * 
     * @return the contained exchange.
     */
    public Exchange getExchange() {
        return exchange;
    }
    
    /**
     * Returns the packet factory used to create {@link PlatformPacket}s.
     * 
     * @return the packet factory used to create {@link PlatformPacket}s.
     */
    public PlatformPacketFactory getPacketFactory() {
        return packetFactory;
    }

    /**
     * Sets the packet factory used to create {@link PlatformPacket}s.
     * 
     * @param packetFactory
     *            a packet factory.
     */
    public void setPacketFactory(PlatformPacketFactory packetFactory) {
        this.packetFactory = packetFactory;
    }


    /**
     * Returns the message renderer responsible to visualize this message.
     * 
     * @return 
     *        the {@link PlatformMessageRenderer} instance, responsible to 
     *        visualize this message.
     */
    public PlatformMessageRenderer getMessageRenderer() {
        return messageRenderer;
    }

    /**
     * Sets the message renderer responsible to visualize this Message.
     * 
     * @param messageRenderer
     *                      a {@link PlatformMessageRenderer} instnace.
     */
    public void setMessageRenderer(PlatformMessageRenderer messageRenderer) {
        this.messageRenderer = messageRenderer;
    }
    
    
    /**
     * Creates a byte array representation of this message. The returned packet
     * is a snapshot of the current state of this message.
     * 
     * @return a byte array representation of this message.
     * 
     * @see PlatformPacketFactory#createPacket(Exchange)
     * @see PlatformPacket#serialize()
     */
    public byte[] createPacket() {
        return packetFactory.createPacket(exchange).serialize();
    }

    /**
     * Reads the flow id from the contained {@link Exchange}.
     * 
     * @return flow identifier. 
     */
    public Long getFlowId() {
        return (Long)exchange.getIn().getHeader(FLOW_ID_KEY);
    }

    /**
     * Writes the flow id to the contained {@link Exchange}.
     * 
     * @param flowId
     *            flow identifier.
     */
    public void setFlowId(Long flowId) {
        exchange.getIn().setHeader(FLOW_ID_KEY, flowId);
    }

    /**
     * Reads the {@link SplitHistory} from the contained {@link Exchange}. 
     * 
     * @return split history. 
     */
    public SplitHistory getSplitHistory() {
        String history = (String)exchange.getIn().getHeader(FLOW_SPLIT_HISTORY);
        if (history == null) {
            return new SplitHistory();
        }
        return SplitHistory.parse(history);
    }

    /**
     * Writes the {@link SplitHistory} to the contained {@link Exchange}.
     * 
     * @param history
     *            split history.
     */
    public void setSplitHistory(SplitHistory history) {
        exchange.getIn().setHeader(FLOW_SPLIT_HISTORY, history.toString());
    }

    /**
     * Returns <code>true</code> if the message represents an error message.
     * 
     * @return <code>true</code> if the message represents an error message.
     * 
     * @see Exchange#isFailed()
     */
    public boolean isFailed() {
        return exchange.isFailed();
    }

    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.flow.ManagedMessage#render()
     */
    public String render() {
        if (messageRenderer != null) {
           return messageRenderer.render(this);
        }
        return null;
    }
}
