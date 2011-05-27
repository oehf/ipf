package org.openehealth.ipf.platform.camel.ihe.ws.mbean;

import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

public class SomeItiComponent extends AbstractWsComponent<ItiServiceInfo> {
    
    private static final String NS_URI = "urn:iti:some:mai:2011";
    public static final ItiServiceInfo WS_CONFIG = new ItiServiceInfo(
            new QName(NS_URI, "SomeConsumer_Service", "iti"),
            String.class,
            new QName(NS_URI, "SomeConsumer_Binding_Soap12", "iti"),
            false,
            "wsdl/some/some.wsdl",
            true,
            false,
            false);
    
    @Override
    public ItiServiceInfo getWebServiceConfiguration() {
        return WS_CONFIG;
    }
    
    @SuppressWarnings("unchecked")
    protected Endpoint createEndpoint(final String uri, final String remaining,
        @SuppressWarnings("rawtypes") final Map parameters) throws Exception {
        return new SomeItiEndpoint(uri, remaining, this, getCustomInterceptors(parameters));
    }

}
