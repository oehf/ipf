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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti44;

import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v3.iti44.Iti44XdsPortType;

import javax.xml.namespace.QName;

/**
 * The Camel component for the ITI-44 transaction (XDS.b).
 */
public class Iti44XdsComponent extends AbstractIti44Component {

    private static final String NS_URI_XDS = "urn:ihe:iti:xds-b:2007";
    public final static Hl7v3WsTransactionConfiguration WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            IpfInteractionId.ITI_44_XDS,
            new QName(NS_URI_XDS, "DocumentRegistry_Service", "ihe"),
            Iti44XdsPortType.class,
            new QName(NS_URI_XDS, "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti44/iti44-xds-raw.wsdl",
            "MCCI_IN000002UV01",
            null,
            false,
            false);

    @Override
    public Hl7v3WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }
}
