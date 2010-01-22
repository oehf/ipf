/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.osgi.config.ihe.atna;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.camel.ihe.atna.util.CamelEndpointSender;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;

/**
 * 
 * Listener bean to registration of AuditMessageSender type in the OSGi service
 * registry and sets it as a default sender to AuditorModuleContext. On
 * unregister it sets back the previous sender.
 * 
 * @author Boris Stanojevic
 * 
 */
public class CamelEndpointSenderRegistration {

    private static final Log LOG = LogFactory
            .getLog(CamelEndpointSenderRegistration.class);

    private AuditorModuleContext iheAuditorContext;

    private AuditMessageSender oldSender;

    private String auditRepositoryHost;

    private int auditRepositoryPort;

    /**
     * 
     * @param auditMessageSender
     *            incoming CamelEndpointSender which replaces the default
     *            auditSender
     * @param map
     */
    public void setCamelEndpointSenderService(
            CamelEndpointSender auditMessageSender, Map<?, ?> map) {
        try {
            oldSender = iheAuditorContext.getSender();
            replaceSender(auditMessageSender, "0.0.0.0", 0);
            LOG.info("CamelEndpointSender set as default audit message sender");
        } catch (Exception e) {
            replaceSender(oldSender, auditRepositoryHost, auditRepositoryPort);
            LOG.error("Failed to set the CamelEndpointSender"
                    + "as default audit message sender" + e);
        }
    }

    /**
     * 
     * @param auditMessageSender
     *            the outgoing CamelEndpointSender, will be replaced with the
     *            old default sender
     * @param map
     */
    public void unsetCamelEndpointSenderService(
            CamelEndpointSender auditMessageSender, Map<?, ?> map) {
        replaceSender(oldSender, auditRepositoryHost, auditRepositoryPort);
        LOG.info("Audit message sender set back to default: "
                + auditRepositoryHost + ":" + auditRepositoryPort);
    }

    private synchronized void replaceSender(AuditMessageSender sender,
            String host, int port) {
        iheAuditorContext.setSender(sender);
        iheAuditorContext.getConfig().setAuditRepositoryHost(host);
        iheAuditorContext.getConfig().setAuditRepositoryPort(port);
    }

    public AuditorModuleContext getIheAuditorContext() {
        return iheAuditorContext;
    }

    public void setIheAuditorContext(AuditorModuleContext iheAuditorContext) {
        this.iheAuditorContext = iheAuditorContext;
    }

    public String getAuditRepositoryHost() {
        return auditRepositoryHost;
    }

    public void setAuditRepositoryHost(String auditRepositoryHost) {
        this.auditRepositoryHost = auditRepositoryHost;
    }

    public int getAuditRepositoryPort() {
        return auditRepositoryPort;
    }

    public void setAuditRepositoryPort(int auditRepositoryPort) {
        this.auditRepositoryPort = auditRepositoryPort;
    }
}