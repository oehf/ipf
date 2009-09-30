package org.openehealth.ipf.commons.ihe.xds;

import javax.xml.namespace.QName;

import org.openehealth.ipf.commons.ihe.xds.core.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ItiServiceInfo;

/**
 * Provides access to the service and client factories for ITI-15.
 * @author Jens Riemschneider
 */
public class Iti15 {
    private final static ItiServiceInfo ITI_15 = new ItiServiceInfo(
            new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Service", "ihe"),
            Iti15PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Binding_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Port_Soap11", "ihe"),
            null,
            false,
            "wsdl/iti15.wsdl",
            false,
            true,
            false,
            false);
           
    /**
     * Returns a factory for client stubs of the ITI-15 service.
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
        return new ItiClientFactory(ITI_15, false, audit ? new Iti15ClientAuditStrategy(allowIncompleteAudit) : null, serviceUrl);
    }

    /**
     * Returns a factory for ITI-15 services.
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
        return new ItiServiceFactory(ITI_15, audit ? new Iti15ServerAuditStrategy(allowIncompleteAudit) : null, serviceAddress);
    }
}
