package org.openehealth.ipf.commons.ihe.xds.iti14;

import javax.xml.namespace.QName;

import org.openehealth.ipf.commons.ihe.xds.core.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ItiServiceInfo;

/**
 * Provides access to the service and client factories for ITI-14.
 * @author Jens Riemschneider
 */
public class Iti14 {
    private final static ItiServiceInfo ITI_14 = new ItiServiceInfo(
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Service", "ihe"),
            Iti14PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Binding_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Port_Soap11", "ihe"),
            null,
            false,
            "wsdl/iti14.wsdl",
            false,
            false,
            false,
            false);
           
    /**
     * Returns a factory for client stubs of the ITI-14 service.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceUrl
     *          the URL of the service to use.
     * @return the client factory.
     */
    public static ItiClientFactory getClientFactory(boolean audit, boolean allowIncompleteAudit, String serviceUrl) {        
        return new ItiClientFactory(ITI_14, false, audit ? new Iti14ClientAuditStrategy(allowIncompleteAudit) : null, serviceUrl);
    }

    /**
     * Returns a factory for ITI-14 services.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceAddress
     *          the address used to publish the service in the current context.  
     * @return the service factory.
     */
    public static ItiServiceFactory getServiceFactory(boolean audit, boolean allowIncompleteAudit, String serviceAddress) {
        return new ItiServiceFactory(ITI_14, audit ? new Iti14ServerAuditStrategy(allowIncompleteAudit) : null, serviceAddress);
    }
}
