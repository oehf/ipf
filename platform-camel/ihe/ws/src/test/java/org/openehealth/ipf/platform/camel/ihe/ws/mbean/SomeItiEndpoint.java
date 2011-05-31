package org.openehealth.ipf.platform.camel.ihe.ws.mbean;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;

public class SomeItiEndpoint extends DefaultItiEndpoint<ItiServiceInfo> {
    
    @SuppressWarnings("unchecked")
    public SomeItiEndpoint(
            String endpointUri,
            String address,
            SomeItiComponent someItiComponent,
            InterceptorProvider interceptorProvider) {
        super(endpointUri, address, someItiComponent, interceptorProvider);
    }
    
    @Override
    public Producer createProducer() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
