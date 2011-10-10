/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.ws.mbean;

import org.apache.camel.management.mbean.ManagedEndpoint;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * 
 * @author Stefan Ivanov
 * 
 */
@Deprecated
@ManagedResource("Managed IPF WS ITI Endpoint")
public class ManagedWsItiEndpoint extends ManagedEndpoint {
    private final WsTransactionConfiguration wsTransactionConfiguration;

    public ManagedWsItiEndpoint(DefaultItiEndpoint<?> endpoint,
        WsTransactionConfiguration wsTransactionConfiguration) {
        super(endpoint);
        this.wsTransactionConfiguration = wsTransactionConfiguration;
    }
    
    @ManagedAttribute(description = "Service Address")
    public String getServiceAddress() {
        return getEndpoint().getServiceAddress();
    }
    
    @ManagedAttribute(description = "HomeCommunityId")
    public String getHomeCommunityId() {
        return getEndpoint().getHomeCommunityId();
    }
    
    @ManagedAttribute(description = "Audit Enabled")
    public boolean isAudit() {
        return getEndpoint().isAudit();
    }
    
    @ManagedAttribute(description = "Security Enabled")
    public boolean isSecure() {
        return getEndpoint().isSecure();
    }
    
    @ManagedAttribute(description = "Addressing Enabled")
    public boolean isAddressing() {
        return getWsTransactionConfiguration().isAddressing();
    }
    
    @ManagedAttribute(description = "Mtom Enabled")
    public boolean isMtom() {
        return getWsTransactionConfiguration().isMtom();
    }
    
    @ManagedAttribute(description = "SOAP With Attachments Output Enabled")
    public boolean isSwaOutSupport() {
        return getWsTransactionConfiguration().isSwaOutSupport();
    }

    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return wsTransactionConfiguration;
    }
    
    @Override
    public DefaultItiEndpoint<?> getEndpoint() {
        return (DefaultItiEndpoint<?>) super.getEndpoint();
    }

}
