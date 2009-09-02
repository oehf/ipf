package org.openehealth.ipf.commons.ihe.xds;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.openehealth.ipf.commons.ihe.xds.cxf.WsSecurityUnderstandingInInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.AuditDatasetEnrichmentInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.AuditFinalInterceptor;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ServerPayloadExtractorInterceptor;

public class ItiServiceFactory {
    private static final Log log = LogFactory.getLog(ItiServiceFactory.class);
    
    private final ItiServiceInfo serviceInfo;
    private final ItiAuditStrategy auditStrategy;
    private final String serviceAddress;

    public ItiServiceFactory(ItiServiceInfo serviceInfo, ItiAuditStrategy auditStrategy, String serviceAddress) {
        this.serviceInfo = serviceInfo;
        this.auditStrategy = auditStrategy;
        this.serviceAddress = serviceAddress;
    }
    
    public Object createService(Class<?> serviceImplClass) {
        try {
            Object service = serviceImplClass.newInstance();
            
            ServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
            configureService(svrFactory, service);
            configureBinding(svrFactory);
            configureInterceptors(svrFactory);        
            
            svrFactory.create();
            log.debug("Published webservice endpoint for: " + serviceInfo.getServiceName());
            return service;
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private void configureService(ServerFactoryBean svrFactory, Object service) {
        svrFactory.setServiceClass(serviceInfo.getServiceClass());
        svrFactory.setServiceName(serviceInfo.getServiceName());
        svrFactory.setWsdlLocation(serviceInfo.getWsdlLocation());
        svrFactory.setAddress(serviceAddress);
        svrFactory.setServiceBean(service);
        svrFactory.getFeatures().add(new WSAddressingFeature());
        if (serviceInfo.isMtom()) {
            svrFactory.setProperties(Collections.<String, Object>singletonMap("mtom-enabled", "true"));
        }
    }

    private void configureBinding(ServerFactoryBean svrFactory) {
        SoapBindingConfiguration bindingConfig = new SoapBindingConfiguration();
        bindingConfig.setBindingName(serviceInfo.getBindingName());
        svrFactory.setBindingConfig(bindingConfig);
    }

    private void configureInterceptors(ServerFactoryBean svrFactory) {
        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            svrFactory.getInInterceptors().add(new ServerPayloadExtractorInterceptor(auditStrategy));
            svrFactory.getInInterceptors().add(new AuditDatasetEnrichmentInterceptor(auditStrategy, true));
    
            AuditFinalInterceptor auditOutInterceptor = new AuditFinalInterceptor(auditStrategy, true);
            svrFactory.getOutInterceptors().add(auditOutInterceptor);
            svrFactory.getOutFaultInterceptors().add(auditOutInterceptor);
        }
        
        // protocol-related interceptors
        svrFactory.getInInterceptors().add(new WsSecurityUnderstandingInInterceptor());
    }
}
