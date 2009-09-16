package org.openehealth.ipf.commons.ihe.xds.core;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.openehealth.ipf.commons.ihe.xds.core.cxf.WsSecurityUnderstandingInInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.cxf.audit.AuditDatasetEnrichmentInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.cxf.audit.AuditFinalInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.core.cxf.audit.ServerPayloadExtractorInterceptor;

/**
 * Factory for ITI web-services.
 * @author Jens Riemschneider
 */
public class ItiServiceFactory {
    private static final Log log = LogFactory.getLog(ItiServiceFactory.class);
    
    private final ItiServiceInfo serviceInfo;
    private final ItiAuditStrategy auditStrategy;
    private final String serviceAddress;

    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the service to produce.
     * @param auditStrategy
     *          the auditing strategy to use.
     * @param serviceAddress
     *          the address of the service that it should be published with.
     */
    public ItiServiceFactory(ItiServiceInfo serviceInfo, ItiAuditStrategy auditStrategy, String serviceAddress) {
        this.serviceInfo = serviceInfo;
        this.auditStrategy = auditStrategy;
        this.serviceAddress = serviceAddress;
    }
    
    /**
     * Creates, configures and publishes a service.
     * @param <T>
     *          the type of the service implementation.
     * @param serviceImplClass
     *          the type of the service implementation.
     * @return the service implementation.
     */
    public <T> T createService(Class<T> serviceImplClass) {
        try {
            T service = serviceImplClass.newInstance();
            
            ServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
            configureService(svrFactory, service);
            configureBinding(svrFactory);
            configureInterceptors(svrFactory);
            svrFactory.create();
            
            if(service instanceof JaxbContextAware) {
                JAXBDataBinding dataBinding = (JAXBDataBinding) svrFactory.getServiceFactory().getDataBinding(); 
                ((JaxbContextAware) service).setJaxbContext(dataBinding.getContext());
            }
            
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
