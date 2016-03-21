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
package org.openehealth.ipf.platform.camel.flow.process;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultUnitOfWork;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.spi.DataFormat;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;
import org.openehealth.ipf.platform.camel.flow.PlatformMessageRenderer;
import org.openehealth.ipf.platform.camel.flow.PlatformPacket;
import org.openehealth.ipf.platform.camel.flow.PlatformPacketFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.HashMap;

import static org.openehealth.ipf.platform.camel.flow.PlatformPacket.serializableCopy;
import static org.openehealth.ipf.platform.camel.flow.util.DataFormats.marshal;
import static org.openehealth.ipf.platform.camel.flow.util.DataFormats.unmarshal;


/**
 * A base class for processors that trigger {@link FlowManager} operations.
 * 
 * @author Martin Krasser
 */
public abstract class FlowProcessor extends DelegateProcessor implements PlatformPacketFactory {

    @Autowired
    private CamelContext camelContext;
    
    @Autowired
    protected FlowManager flowManager;
    
    private PlatformMessageRenderer messageRenderer;
    
    private DataFormat inFormat;
    private DataFormat outFormat;
    
    private Class<?> inType;
    private Class<?> outType;
    
    private boolean outConversion = true;
    
    /**
     * Returns the {@link CamelContext}.
     * 
     * @return the {@link CamelContext}.
     */
    public CamelContext getCamelContext() {
        return camelContext;
    }
    
    /**
     * Sets the {@link CamelContext}. This property is {@link Autowired} when
     * used within a Spring 2.5 (or higher) container.
     * 
     * @param camelContext a {@link CamelContext}.
     */
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
    
    /**
     * Sets the {@link FlowManager} used by this processor. This property is
     * {@link Autowired} when used within a Spring 2.5 (or higher) container.
     * 
     * @param flowManager
     *            {@link FlowManager} used by this processor.
     */
    public void setFlowManager(FlowManager flowManager) {
        this.flowManager = flowManager;
    }

    /**
     * Sets the {@link PlatformMessageRenderer} used by this processor.
     * 
     * @param messageRenderer
     *            {@link PlatformMessageRenderer} used by this processor.
     */
    public void setMessageRenderer(PlatformMessageRenderer messageRenderer) {
        this.messageRenderer = messageRenderer;
    }
    
    /**
     * Returns the {@link PlatformMessageRenderer} used by this processor.
     * 
     * @return the {@link PlatformMessageRenderer} used by this processor.
     */
    public PlatformMessageRenderer getMessageRenderer() {
        return messageRenderer;
    }
    
    /**
     * Sets the {@link DataFormat} to be applied to the {@link Exchange#getIn()}
     * message body for creating a byte array representation. The original
     * message body is not replaced by the byte array. The data format is only
     * applied during {@link #createPacket} calls. Calls to this method are
     * made by the {@link PlatformMessage#createPacket()} method on messages
     * created by this processor.
     * 
     * @param inFormat
     *            a data formatting strategy.
     * @return this processor.
     */
    public FlowProcessor inFormat(DataFormat inFormat) {
        this.inFormat = inFormat;
        return this;
    }
    
    /**
     * Sets the {@link DataFormat} to be applied to a message body's byte array
     * representation for creating the {@link Exchange#getIn()} message body
     * (replacing the original message body). The outFormat is applied: 
     * <ul>
     * <li> if {@link #outConversion(boolean)} is set to true, during {@link #createPacket}
     * calls, made by {@link PlatformMessage#createPacket()}</li>
     * <li> during {@link #createExchange(PlatformPacket)} calls, made by {@link FlowBeginProcessor#replay(PlatformPacket)})</li></ul>
     * If both {@code outFormat(DataFormat)} and {@link #outType(Class)} are set,
     * outType is ignored and only outFormat is used.
     * 
     * @param outFormat
     *            a data formatting strategy.
     * @return this processor.
     */
    public FlowProcessor outFormat(DataFormat outFormat) {
        this.outFormat = outFormat;
        return this;
    }
    
    /**
     * Sets the data type the {@link Exchange#getIn()} message body shall be
     * converted to (replacing the original message body) before creating the
     * internal byte array representation. The conversion is only made during
     * {@link #createPacket} calls. Calls to this method are made by the
     * {@link PlatformMessage#createPacket()} method on messages created by this
     * processor.
     * 
     * @param inType
     *            type of the {@link Exchange#getIn()} message's body that
     *            replaces the original message body.
     * @return this processor.
     */
    public FlowProcessor inType(Class<?> inType) {
        this.inType = inType;
        return this;
    }
    
    /**
     * Sets the data type the {@link Exchange#getIn()} message body shall be
     * converted to (replacing the original message body) from the internal byte
     * array representation. The conversion is made:
     * <ul>
     * <li> if {@link #outConversion(boolean)} is set to true, during {@link #createPacket}
     * calls, made by {@link PlatformMessage#createPacket()}
     * <li> during {@link #createExchange(PlatformPacket)} calls, made by 
     * {@link FlowBeginProcessor#replay(PlatformPacket)})</li>  
     * </ul>
     * 
     * @param outType
     *            type of the {@link Exchange#getIn()} message's body that
     *            replaces the original message body.
     * @return this processor.
     */
    public FlowProcessor outType(Class<?> outType) {
        this.outType = outType;
        return this;
    }
    
    /**
     * Set to <code>false</code> to skip conversion of outgoing message
     * bodies. This is recommended to improve performance when using re-readable
     * message bodies. This setting is ignored when re-playing messages.
     * 
     * @param outConversion
     *            <code>false</code> to skip conversion of outgoing message
     *            bodies. Default is <code>true</code>.
     * @return this processor.
     * 
     * @see FlowBeginProcessor#replay(PlatformPacket)
     */
    public FlowProcessor outConversion(boolean outConversion) {
        this.outConversion = outConversion;
        return this;
    }
    
    /**
     * Sets the {@link PlatformMessageRenderer} instance, which will produce 
     * readable text representation of the platform message.
     * 
     * @param messageRenderer instance of {@link PlatformMessageRenderer}
     *              which will be used to render the {@link PlatformMessage}
     * @return this processor
     */
    public FlowProcessor renderer(PlatformMessageRenderer messageRenderer) {
        this.messageRenderer = messageRenderer;
        return this;
    }
    
    /**
     * Creates a {@link PlatformPacket} from an <code>exchange</code> applying
     * either native type conversion to byte array or using
     * {@link #inFormat(DataFormat)} (higher priority). If
     * {@link #outConversion} is <code>false</code> no further conversion of
     * the body of the {@link Exchange#getIn()} message is done, otherwise,
     * {@link #outFormat} or {@link #outType} are applied. If both are not set a
     * conversion to {@link InputStream} type is made.
     * 
     * @param exchange message exchange.
     */
    @Override
    public PlatformPacket createPacket(Exchange exchange) {
        PlatformPacket packet = new PlatformPacket();
        
        packet.setExchangeProperties(serializableCopy(exchange.getProperties()));
        packet.setMessageProperties(serializableCopy(exchange.getIn().getHeaders()));
        
        // create bytes from input body
        // (using data formats or converters)
        byte[] bytes = getInBody(exchange);

        // set body bytes on packet
        packet.setMessageBody(bytes);
        
        if (outConversion) {
            // set body content from bytes 
            // (using data formats or converters)
            setInBody(bytes, exchange);
        }
        return packet;
    }

    /**
     * Creates a {@link PlatformMessage} from an <code>exchange</code>.
     * 
     * @param exchange
     *            message exchange.
     * @return a managed message.
     */
    public PlatformMessage createMessage(Exchange exchange) {
        PlatformMessage platformMessage = new PlatformMessage(exchange);
        platformMessage.setPacketFactory(this);
        platformMessage.setMessageRenderer(messageRenderer);
        return platformMessage; 
    }
    
    /**
     * Creates a new {@link Exchange} from <code>packet</code> using the
     * current {@link CamelContext}.
     * 
     * @param packet message packet.
     * @return a new message exchange.
     */
    protected Exchange createExchange(PlatformPacket packet) {
        DefaultExchange exchange = new DefaultExchange(camelContext);
        
        exchange.setUnitOfWork(new DefaultUnitOfWork(exchange));
        exchange.setProperties(new HashMap<>(packet.getExchangeProperties()));
        exchange.getIn().setHeaders(new HashMap<>(packet.getMessageProperties()));

        setInBody(packet.getMessageBody(), exchange);
    
        return exchange;
    }

    /**
     * Processes <code>exchange</code> by delegating to{@link #processMessage}.
     * 
     * @param exchange
     *            exchange to process.
     */
    @Override
    public void processNext(Exchange exchange) throws Exception {
        processMessage(createMessage(exchange));
        super.processNext(exchange);
    }
    
    /**
     * Processes the {@link ManagedMessage} by delegating to a
     * {@link FlowManager}.
     * 
     * @param message
     *            a managed message.
     */
    protected abstract void processMessage(PlatformMessage message);
    
    private byte[] getInBody(Exchange exchange) {
        Message in = exchange.getIn();
        if (in.getBody() == null) {
            return null;
        }
        if (inFormat != null) {
            return marshal(in.getBody(), exchange, inFormat);
        } else if (inType != null) {
            in.setBody(in.getBody(inType));
        }
        return in.getBody(byte[].class); 
    }

    private void setInBody(byte[] bytes, Exchange exchange) {
        Message in = exchange.getIn();
        if (bytes == null) {
            in.setBody(null);
        } else if (outFormat != null) {
            in.setBody(unmarshal(bytes, exchange, outFormat));
        } else if (outType != null) {
            in.setBody(bytes, outType);
        } else { // fallback
            in.setBody(bytes, InputStream.class);
        }
    }

}
