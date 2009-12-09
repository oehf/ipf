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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import ca.uhn.hl7v2.parser.Parser;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.hl7.HL7MLLPCodec;
import org.apache.camel.component.mina.MinaComponent;
import org.apache.camel.component.mina.MinaEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.MllpCustomInterceptor;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * Generic Camel component for IHE PIX/PDQ transactions.
 * 
 * @author Dmytro Rud
 */
public abstract class MllpComponent extends MinaComponent {
    private static final transient Log LOG = LogFactory.getLog(MllpComponent.class);
    
    public static final String ACK_TYPE_CODE_HEADER = "pixpdqAckTypeCode"; 
    
    private static final String DEFAULT_HL7_CODEC_FACTORY_BEAN_NAME = "#hl7codec";

    /**
     * Default constructor.
     */
    protected MllpComponent() {
        super();
    }

    /**
     * Camel context-based constructor.
     * 
     * @param camelContext
     */
    protected MllpComponent(CamelContext camelContext) {
        super(camelContext);
    }


    /**
     * Creates and configures the endpoint.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Endpoint createEndpoint(
            String uri,
            String remaining, 
            Map parameters) throws Exception 
    {
        // replace URL parts
        int pos = uri.indexOf(":");
        uri = new StringBuilder(uri).replace(0, pos, "mina:tcp").toString();
        remaining = "tcp://" + remaining;

        // extract & exclude parameters which cannot be handled by camel-mina
        boolean audit = getAndRemoveParameter(parameters, "audit", boolean.class, true);
        boolean allowIncompleteAudit = 
            getAndRemoveParameter(parameters, "allowIncompleteAudit", boolean.class, false); 
        boolean secure = getAndRemoveParameter(parameters, "secure", boolean.class, false);
        boolean mutualTLS = getAndRemoveParameter(parameters, "mutualTLS", boolean.class, false);
        String sslContextBean = getAndRemoveParameter(parameters, "sslContext", String.class, "");
        String interceptorBeans = getAndRemoveParameter(parameters, "interceptors", String.class, "");

        // explicitly overwrite some standard camel-mina parameters
        if (parameters == Collections.EMPTY_MAP) {
            parameters = new HashMap();
        }
        parameters.put("sync", true);
        parameters.put("lazySessionCreation", true);
        parameters.put("transferExchange", false);
        if( ! parameters.containsKey("codec")) {
            parameters.put("codec", DEFAULT_HL7_CODEC_FACTORY_BEAN_NAME);
        }

        // adopt character set configured for the HL7 codec
        String codecBean = extractBeanName((String) parameters.get("codec"));

        ProtocolCodecFactory codecFactory = getCamelContext().getRegistry().lookup(
                    codecBean, 
                    ProtocolCodecFactory.class);
        Charset charset = null;
        try {
            charset = ((HL7MLLPCodec) codecFactory).getCharset();
        } catch(ClassCastException cce) {
            LOG.error("Unsupported HL7 codec factory type " + codecFactory.getClass().getName());
        }
        if(charset == null) {
            charset = Charset.defaultCharset();
        }
        parameters.put("encoding", charset.name());
        
        // construct the endpoint
        Endpoint endpoint = super.createEndpoint(uri, remaining, parameters);
        MinaEndpoint minaEndpoint = (MinaEndpoint) endpoint;

        SSLContext sslContext = secure ? lookupSSLContext(sslContextBean) : null;
        List<MllpCustomInterceptor> customInterceptors = getCustomInterceptors(interceptorBeans);

        // wrap and return
        return new MllpEndpoint(
                minaEndpoint,
                audit,
                allowIncompleteAudit,
                getServerAuditStrategy(), 
                getClientAuditStrategy(),
                getTransactionConfiguration(),
                getParser(),
                sslContext,
                mutualTLS,
                customInterceptors);
    }

    private List<MllpCustomInterceptor> getCustomInterceptors(String interceptorBeans) {
        if (interceptorBeans.equals("")) {
            return Collections.emptyList();
        }
        List<MllpCustomInterceptor> interceptors = new ArrayList<MllpCustomInterceptor>();
        for (String bean : interceptorBeans.split(",")) {
            String beanName = extractBeanName(bean);
            MllpCustomInterceptor interceptor =
                getCamelContext().getRegistry().lookup(beanName, MllpCustomInterceptor.class);
            interceptors.add(interceptor);
        }
        return interceptors;
    }

    private SSLContext lookupSSLContext(String sslContextBean) throws NoSuchAlgorithmException {
        if (sslContextBean.equals("")) {
            return SSLContext.getDefault();
        }
        return getCamelContext().getRegistry().lookup(extractBeanName(sslContextBean), SSLContext.class);
    }

    private String extractBeanName(String codecBean) {
        return codecBean.startsWith("#") ? codecBean.substring(1) : codecBean;
    }


    // ----- abstract methods -----

    /**
     * Returns server-side ATNA audit strategy. 
     */
    public abstract MllpAuditStrategy getServerAuditStrategy();

    /**
     * Returns client-side ATNA audit strategy. 
     */
    public abstract MllpAuditStrategy getClientAuditStrategy();

    /**
     * Returns component configuration. 
     */
    public abstract MllpTransactionConfiguration getTransactionConfiguration();
    
    /**
     * Returns HL7 parser. 
     */
    public abstract Parser getParser();

}
