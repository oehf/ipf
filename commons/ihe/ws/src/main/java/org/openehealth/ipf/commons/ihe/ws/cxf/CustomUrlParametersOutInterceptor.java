package org.openehealth.ipf.commons.ihe.ws.cxf;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * CXF Interceptor to add custom static URL parameters to the SOAP Client Request.
 */
public class CustomUrlParametersOutInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

    private final String queryParameters;

    public CustomUrlParametersOutInterceptor(String queryParameters) {
        super(Phase.PREPARE_SEND);
        this.queryParameters = queryParameters;
    }

    public CustomUrlParametersOutInterceptor(String queryParameters, String phase) {
        super(phase);
        this.queryParameters = queryParameters;
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        if (queryParameters != null && !queryParameters.isEmpty()) {
            message.put(Message.QUERY_STRING, queryParameters);
        }
    }
}
