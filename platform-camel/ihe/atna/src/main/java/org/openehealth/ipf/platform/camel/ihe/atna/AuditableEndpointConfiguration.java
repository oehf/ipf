/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.atna;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.spi.UriParam;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.platform.camel.ihe.atna.util.AuditConfiguration;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableEndpointConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Abstract base class for configuration for endpoints that support auditing
 *
 * @since 3.5
 */
public abstract class AuditableEndpointConfiguration extends InterceptableEndpointConfiguration {

    @Getter
    private AuditContext auditContext;

    protected AuditableEndpointConfiguration(DefaultComponent component, Map<String, Object> parameters) {
        super(component, parameters);
        auditContext = AuditConfiguration.obtainAuditContext(component, parameters);
    }

    public boolean isAudit() {
        return auditContext.isAuditEnabled();
    }

}
