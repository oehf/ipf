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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.mbean;

import java.util.List;

import org.apache.camel.component.mina.MinaConfiguration;
import org.apache.camel.management.mbean.ManagedEndpoint;
import org.apache.mina.common.IoFilter;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpClientAuthType;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.MllpCustomInterceptor;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * 
 * @author Stefan Ivanov
 * 
 */
@ManagedResource("Managed IPF MLLP ITI Endpoint")
public class ManagedMllpItiEndpoint extends ManagedEndpoint {
    private final MinaConfiguration minaConfiguration;

    public ManagedMllpItiEndpoint(MllpEndpoint endpoint, MinaConfiguration configuration) {
        super(endpoint);
        this.minaConfiguration = configuration;
    }
    
    @ManagedAttribute(description = "Component Type Name")
    public String getComponentType() {
        return getEndpoint().getComponent().getClass().getName();
    }
    
    @ManagedAttribute(description = "Mina Host")
    public String getHost() {
        return getMinaConfiguration().getHost();
    }
    
    @ManagedAttribute(description = "Mina Port")
    public int getPort() {
        return getMinaConfiguration().getPort();
    }
    
    @ManagedAttribute(description = "Mina Encoding")
    public String getEncoding() {
        return getMinaConfiguration().getEncoding();
    }
    
    @ManagedAttribute(description = "Mina Timeout")
    public long getTimeout() {
        return getMinaConfiguration().getTimeout();
    }
    
    @ManagedAttribute(description = "Mina Filters")
    public String[] getIoFilters() {
        List<IoFilter> filters = getMinaConfiguration().getFilters();
        return toStringArray(filters);
    }

    @ManagedAttribute(description = "Audit Enabled")
    public boolean isAudit() {
        return getEndpoint().isAudit();
    }
    
    @ManagedAttribute(description = "Incomplete Audit Allowed")
    public boolean isAllowIncompleteAudit() {
        return getEndpoint().isAllowIncompleteAudit();
    }
    
    @ManagedAttribute(description = "Auto Cancel Enabled")
    public boolean isAutoCancel() {
        return getEndpoint().isAutoCancel();
    }
    
    @ManagedAttribute(description = "Support Interactive Continuation Enabled")
    public boolean isInteractiveContinuationSupport() {
        return getEndpoint().isSupportInteractiveContinuation();
    }
    
    @ManagedAttribute(description = "Interactive Continuation Default Threshold")
    public int getInteractiveContinuationDefaultThreshold() {
        return getEndpoint().getInteractiveContinuationDefaultThreshold();
    }
    
    @ManagedAttribute(description = "Interactive Continuation Storage Cache Type")
    public String getInteractiveContinuationStorageType() {
        return getEndpoint().isSupportInteractiveContinuation() ?
            getEndpoint().getInteractiveContinuationStorage().getClass().getName() : "";
    }
    
    @ManagedAttribute(description = "Support Unsolicited Fragmentation Enabled")
    public boolean isUnsolicitedFragmentationSupport() {
        return getEndpoint().isSupportUnsolicitedFragmentation();
    }
    
    @ManagedAttribute(description = "Unsolicited Fragmentation Threshold")
    public int getUnsolicitedFragmentationThreshold() {
        return getEndpoint().getUnsolicitedFragmentationThreshold();
    }
    
    @ManagedAttribute(description = "Unsolicited Fragmentation Storage Cache Type")
    public String getUnsolicitedFragmentationStorageType() {
        return getEndpoint().isSupportUnsolicitedFragmentation() ?
            getEndpoint().getUnsolicitedFragmentationStorage().getClass().getName() : "";
    }

    @ManagedAttribute(description = "Support Segment Fragmentation Enabled")
    public boolean isSegmentFragmentationSupport() {
        return getEndpoint().isSupportSegmentFragmentation();
    } 
    
    @ManagedAttribute(description = "Segment Fragmentation Threshold")
    public int getSegmentFragmentationThreshold() {
        return getEndpoint().getSegmentFragmentationThreshold();
    }
    
    @ManagedAttribute(description = "Client Authentication Type")
    public MllpClientAuthType getClientAuthType() {
        return getEndpoint().getClientAuthType();
    }
    
    @ManagedAttribute(description = "SSL Secure Enabled")
    public boolean isSslSecure() {
        return getEndpoint().getSslContext() != null;
    }
    
    @ManagedAttribute(description = "Defined SSL Protocols")
    public String[] getSslProtocols() {
        return getEndpoint().getSslProtocols();
    }
    
    @ManagedAttribute(description = "Defined SSL Ciphers")
    public String[] getSslCiphers() {
        return getEndpoint().getSslCiphers();
    }
    
    @ManagedAttribute(description = "Custom Interceptors")
    public String[] getCustomInterceptors() {
        List<MllpCustomInterceptor> interceptors = getEndpoint().getCustomInterceptors();
        return toStringArray(interceptors);
    }

    @Override
    public MllpEndpoint getEndpoint() {
        return (MllpEndpoint) super.getEndpoint();
    }

    public MinaConfiguration getMinaConfiguration() {
        return minaConfiguration;
    }
    
    private String[] toStringArray(List<?> list) {
        final String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).getClass().getCanonicalName();
        }
        return result;
    }

}
