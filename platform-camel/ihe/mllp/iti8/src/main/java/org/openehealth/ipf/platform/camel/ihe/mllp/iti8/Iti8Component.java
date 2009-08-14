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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpointConfiguration;

/**
 * Camel component for ITI-8 (PIX Feed).
 * 
 * @author Dmytro Rud
 */
public class Iti8Component extends MllpComponent {
    private final MllpEndpointConfiguration ENDPOINT_CONFIGURATION =
        new MllpEndpointConfiguration(
                "2.3.1", 
                "PIX adapter", 
                "IPF", 
                207, 
                AckTypeCode.AR, 
                207, 
                "ADT",
                new String[] {"A01", "A02", "A05", "A08", "A40"}); 
  
    
    public Iti8Component() {
        super();
    }

    public Iti8Component(CamelContext camelContext) {
        super(camelContext);
    }
    
    public MllpAuditStrategy getClientAuditStrategy() {
        return new Iti8ClientAuditStrategy();
    }

    public MllpAuditStrategy getServerAuditStrategy() {
        return new Iti8ServerAuditStrategy();
    }
    
    public MllpEndpointConfiguration getErrorHandlingConfiguration() {
        return ENDPOINT_CONFIGURATION;
    }
}
