package org.openehealth.ipf.commons.ihe.xds;

import static org.apache.commons.lang.Validate.notNull;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.addressing.MAPAggregator;
import org.apache.cxf.ws.addressing.soap.MAPCodec;
import org.openehealth.ipf.commons.ihe.xds.cxf.FixContentTypeOutInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.MustUnderstandDecoratorInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.ProvidedAttachmentOutInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.AuditDatasetEnrichmentInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.AuditFinalInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ClientOutputStreamSubstituteInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ClientPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.utils.SoapUtils;

public class ItiClientFactory {
    private static final Log log = LogFactory.getLog(ItiClientFactory.class);

    private final ThreadLocal<Object> threadLocalPort = new ThreadLocal<Object>();
    private final ItiServiceInfo serviceInfo;
    private final boolean soap11;
    private final String serviceUrl;
    private final ItiAuditStrategy auditStrategy;

    public ItiClientFactory(ItiServiceInfo serviceInfo, boolean soap11, ItiAuditStrategy auditStrategy, String serviceUrl) {
        notNull(serviceInfo, "serviceInfo");
        this.serviceInfo = serviceInfo;
        this.soap11 = soap11;
        this.auditStrategy = auditStrategy;
        this.serviceUrl = serviceUrl;
    }

    public synchronized Object getClient() {
        if (threadLocalPort.get() == null) {
            URL wsdlURL = getClass().getClassLoader().getResource(serviceInfo.getWsdlLocation());
            Service service = Service.create(wsdlURL, serviceInfo.getServiceName());
            
            QName portName = 
                ((serviceInfo.getPortName12() == null) || soap11) ?
                    serviceInfo.getPortName11() : 
                    serviceInfo.getPortName12();

            Object port = service.getPort(portName, serviceInfo.getServiceClass());
            configureBinding(port);
            configureInterceptors(port);

            threadLocalPort.set(port);
            log.debug("Created client adapter for: " + serviceInfo.getServiceName());
        }        
        return threadLocalPort.get();
    }

    public ItiServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    private void configureInterceptors(Object port) {
        Client client = ClientProxy.getClient(port);
        
        // WS-Addressing-related interceptors
        if (serviceInfo.isAddressing()) {
            MustUnderstandDecoratorInterceptor interceptor = new MustUnderstandDecoratorInterceptor();
            for (String nsUri : SoapUtils.WS_ADDRESSING_NS_URIS) {
                interceptor.addHeader(new QName(nsUri, "Action"));
                interceptor.addHeader(new QName(nsUri, "ReplyTo"));
            }

            client.getOutInterceptors().add(interceptor);

            MAPCodec mapCodec = new MAPCodec();
            MAPAggregator mapAggregator = new MAPAggregator();
            client.getInInterceptors().add(mapCodec);
            client.getInInterceptors().add(mapAggregator);
            client.getInFaultInterceptors().add(mapCodec);
            client.getInFaultInterceptors().add(mapAggregator);
            client.getOutInterceptors().add(mapCodec);
            client.getOutInterceptors().add(mapAggregator);
            client.getOutFaultInterceptors().add(mapCodec);
            client.getOutFaultInterceptors().add(mapAggregator);
        }
        
        // install auditing-related interceptors if the user has not switched
        // auditing off
        if (auditStrategy != null) {
            client.getOutInterceptors().add(new AuditDatasetEnrichmentInterceptor(auditStrategy, false));
            client.getOutInterceptors().add(new ClientOutputStreamSubstituteInterceptor(auditStrategy));
            client.getOutInterceptors().add(new ClientPayloadExtractorInterceptor(auditStrategy));
        
            AuditFinalInterceptor finalInterceptor = new AuditFinalInterceptor(auditStrategy, false);
            client.getInInterceptors().add(finalInterceptor);
            client.getInFaultInterceptors().add(finalInterceptor);
        }
        
        if (serviceInfo.isSwaOutSupport()) {
            client.getOutInterceptors().add(new ProvidedAttachmentOutInterceptor());
            client.getOutInterceptors().add(new FixContentTypeOutInterceptor());            
        }
    }

    private void configureBinding(Object port) {
        BindingProvider bindingProvider = (BindingProvider) port;

        Map<String, Object> reqContext = bindingProvider.getRequestContext();
        reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

        Binding binding = bindingProvider.getBinding();
        SOAPBinding soapBinding = (SOAPBinding) binding;
        soapBinding.setMTOMEnabled(serviceInfo.isMtom());
    }
}
