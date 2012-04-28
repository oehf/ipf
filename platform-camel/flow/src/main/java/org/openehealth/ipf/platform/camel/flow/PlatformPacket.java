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
import org.openehealth.ipf.commons.core.io.IOUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A serializable data container for {@link Exchange} content. The data
 * contained in this packet are sufficient to re-create an {@link Exchange} and
 * replay it with the flow manager.
 * 
 * @author Martin Krasser
 */
public class PlatformPacket implements Serializable {

    private static final long serialVersionUID = -1727312493187004318L;

    private Map<String, Object> exchangeProperties;
    
    private Map<String, Object> messageProperties;
    
    private byte[] messageBody;
    
    private String replayStrategyId;
    
    /**
     * Returns this packet's exchange properties.
     * 
     * @return this packet's exchange properties.
     */
    public Map<String, Object> getExchangeProperties() {
        return exchangeProperties;
    }

    /**
     * Set this packet's exchange properties from a copy of {@link Exchange}
     * properties.
     * 
     * @param exchangeProperties
     *            copy of {@link Exchange} properties.
     *            
     * @see Exchange#getProperties()
     */
    public void setExchangeProperties(Map<String, Object> exchangeProperties) {
        this.exchangeProperties = exchangeProperties;
    }

    /**
     * Returns this packet's message properties.
     * 
     * @return this packet's message properties.
     */
    public Map<String, Object> getMessageProperties() {
        return messageProperties;
    }

    /**
     * Set this packet's message properties from a copy of
     * {@link Exchange#getIn()} message properties.
     * 
     * @param messageProperties
     *            copy of {@link Exchange#getIn()} message properties.
     * 
     * @see Exchange#getProperties()
     */
    public void setMessageProperties(Map<String, Object> messageProperties) {
        this.messageProperties = messageProperties;
    }

    /**
     * Returns this packet's message body as byte array.
     * 
     * @return this packet's message body as byte array.
     */
    public byte[] getMessageBody() {
        return messageBody;
    }

    /**
     * Returns this packet's message body as byte[].
     * 
     * @param messageBody
     *            byte array representation of {@link Exchange#getIn()} message
     *            body.
     */
    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }
    
    /**
     * Returns the {@link ReplayStrategy} identifier.
     * 
     * @return the {@link ReplayStrategy} identifier.
     */
    public String getReplayStrategyId() {
        return replayStrategyId;
    }

    /**
     * @param replayStrategyId
     *          the replay strategy.
     */
    public void setReplayStrategyId(String replayStrategyId) {
        this.replayStrategyId = replayStrategyId;
    }

    /**
     * Deserializes a {@link PlatformPacket} from a byte array.
     * 
     * @param packet
     *            byte array representation of a {@link PlatformPacket}.
     * @return a deserialized {@link PlatformPacket}.
     */
    public static PlatformPacket deserialize(byte[] packet) {
        try {
            return (PlatformPacket)IOUtils.deserialize(packet);
        } catch (Exception e) {
            throw new PlatformPacketException("cannot deserialize packet", e);
        }
    }
    
    /**
     * Serializes this packet into a byte array.
     * 
     * @return the serialized packet.
     */
    public byte[] serialize() {
        try {
            return IOUtils.serialize(this);
        } catch (Exception e) {
            throw new PlatformPacketException("cannot serialize packet", e);
        }
    }
 
    /**
     * Creates a copy of <code>map</code> omitting entries with values that
     * are not {@link Serializable}.
     * 
     * @param map map to copy.
     * @return serializable copy.
     */
    public static Map<String, Object> serializableCopy(Map<String, Object> map) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (Exchange.AGGREGATION_STRATEGY.equals(entry.getKey())){
                // The aggregation strategies are hold in a Map, which is serializable 
                // Do not serialize the aggregation streategies map. 
                // See org.apache.camel.Splitter#setAggregationStrategyOnExchange
                // See GroovyFlowTest for test case
                continue;
            }
            if (entry.getValue() instanceof Serializable) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
    
}
