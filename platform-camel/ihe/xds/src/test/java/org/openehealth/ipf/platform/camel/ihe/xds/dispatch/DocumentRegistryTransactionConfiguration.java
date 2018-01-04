/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.dispatch;

import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;

import javax.xml.namespace.QName;

/**
 * Fake transaction configuration.  Only the "auditRequestPayload" property will be
 * actually used (the last but one parameter in the constructor).
 *
 * @author Dmytro Rud
 */
public class DocumentRegistryTransactionConfiguration extends WsTransactionConfiguration<XdsAuditDataset> {

    public DocumentRegistryTransactionConfiguration() {
        super("dummy",
                "dummy",
                false,
                null,
                null,
                new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
                DocumentRegistryPortType.class,
                new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
                true,
                "dispatch/DocumentRegistry.wsdl",
                true,
                false,
                true,
                false);
    }
}
