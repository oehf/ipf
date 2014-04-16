/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.atna.util;

import java.net.URI;

import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomainManager;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper configuration bean that initializes the underlying ATNA facilities when
 * TLS is used as transport protocol.
 */
public class AuditorTLSConfig {

    public static final String DEFAULT_SECURITY_DOMAIN_NAME = "mpi-atna-tls";
    private static final Logger LOG = LoggerFactory.getLogger(AuditorTLSConfig.class);

    private String securityDomainName = DEFAULT_SECURITY_DOMAIN_NAME;
    private AuditorModuleConfig auditorModuleConfig;

    public AuditorTLSConfig(AuditorModuleConfig auditorModuleConfig) {
        super();
        this.auditorModuleConfig = auditorModuleConfig;
    }

    public void setSecurityDomainName(String securityDomainName) {
        this.securityDomainName = securityDomainName;
    }

    public void init() throws Exception {
        String transport = auditorModuleConfig.getAuditRepositoryTransport();
        if ("TLS".equals(transport)) {
            LOG.info("ATNA uses {}, setting up Security Domain", transport);
            SecurityDomain securityDomain = new SecurityDomain(securityDomainName, System.getProperties());
            NodeAuthModuleContext nodeContext = NodeAuthModuleContext.getContext();
            SecurityDomainManager securityDomainManager = nodeContext.getSecurityDomainManager();
            securityDomainManager.registerSecurityDomain(securityDomain);
            URI uri = new URI("atna://"
                    + auditorModuleConfig.getAuditRepositoryHost() + ":"
                    + auditorModuleConfig.getAuditRepositoryPort());
            securityDomainManager.registerURItoSecurityDomain(uri, securityDomainName);
            LOG.info("Registered {} for domain ", uri, securityDomainName);
        } else {
            LOG.info("ATNA uses {}, no need to setup Security Domain", transport);
        }
    }
}
