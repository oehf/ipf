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
package org.openehealth.ipf.platform.camel.lbs.mina.mllp;

import static org.apache.commons.lang.Validate.notNull;

import org.apache.camel.CamelContext;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.openehealth.ipf.commons.lbs.resource.ResourceFactory;

/**
 * Codec implementation for MLLP messages stored as a resource
 * @author Jens Riemschneider
 */
public class MllpStoreCodec implements ProtocolCodecFactory {
    public static final char START_MESSAGE = '\u000b';
    public static final char END_MESSAGE = '\u001c';   
    public static final char LAST_CHARACTER = 13;
    
    private final ResourceFactory resourceFactory;
    private final CamelContext camelContext;

    /**
     * Constructs the codec
     * @param resourceFactory 
     *          the factory used to create new resources
     * @param camelContext
     *          the camel context used to access type converters
     */
    public MllpStoreCodec(ResourceFactory resourceFactory, CamelContext camelContext) {
        notNull(resourceFactory, "resourceFactory cannot be null");
        notNull(camelContext, "camelContext cannot be null");
        this.resourceFactory = resourceFactory;
        this.camelContext = camelContext;
    }
    
    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolCodecFactory#getDecoder()
     */
    @Override
    public ProtocolDecoder getDecoder() throws Exception {        
        return new MllpDecoder(resourceFactory);
    }

    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolCodecFactory#getEncoder()
     */
    @Override
    public ProtocolEncoder getEncoder() throws Exception {
        return new MllpEncoder(camelContext.getTypeConverter());
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: resourceFactory=%2$s, camelContext=%3$s}",
                getClass().getSimpleName(), resourceFactory, camelContext);        
    }
}
