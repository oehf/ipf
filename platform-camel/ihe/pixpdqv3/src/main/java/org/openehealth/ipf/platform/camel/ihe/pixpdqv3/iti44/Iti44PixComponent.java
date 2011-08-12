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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti44;

import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.ihe.hl7v3.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti44.Iti44PixPortType;

import javax.xml.namespace.QName;

/**
 * The Camel component for the ITI-44 transaction (PIX Feed v3).
 */
public class Iti44PixComponent extends AbstractIti44Component {
    private static final String NS_URI_PIX = "urn:ihe:iti:pixv3:2007";
    public static final Hl7v3ServiceInfo WS_CONFIG = new Hl7v3ServiceInfo(
            IpfInteractionId.ITI_44_PIX,
            new QName(NS_URI_PIX, "PIXManager_Service", "ihe"),
            Iti44PixPortType.class,
            new QName(NS_URI_PIX, "PIXManager_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti44/iti44-pix-raw.wsdl",
            "MCCI_IN000002UV01",
            false,
            false);

    @Override
    public Hl7v3ServiceInfo getWebServiceConfiguration() {
        return WS_CONFIG;
    }
}
