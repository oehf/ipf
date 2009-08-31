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
package org.openehealth.ipf.platform.camel.ihe.pix.iti9;

import java.util.HashMap;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.openehealth.ipf.modules.hl7.parser.PipeParser;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpTransactionConfiguration;

import ca.uhn.hl7v2.parser.Parser;

/**
 * Camel component for ITI-9 (PIX Query).
 * @author Dmytro Rud
 */
public class Iti9Component extends MllpComponent {
    private static final MllpTransactionConfiguration CONFIGURATION =
        new MllpTransactionConfiguration(
                "2.5", 
                "PIX adapter", 
                "IPF",
                207, 
                AckTypeCode.AR, 
                207, 
                "QBP",
                new String[] {"Q23"}); 
  
    private static final MllpAuditStrategy clientAuditStrategy = new Iti9ClientAuditStrategy();
    private static final MllpAuditStrategy serverAuditStrategy = new Iti9ServerAuditStrategy();
    private static final Parser parser;
    
    static {
        HashMap<String, String[]> map = new HashMap<String, String[]>();
        map.put("2.5", new String[] {"org.openehealth.ipf.platform.camel.ihe.pix.iti9.def.v25"});
        CustomModelClassFactory factory = new CustomModelClassFactory(map); 
        parser = new PipeParser(factory);
    }
    
    
    public Iti9Component() {
        super();
    }

    public Iti9Component(CamelContext camelContext) {
        super(camelContext);
    }
    
    @Override
    public MllpAuditStrategy getClientAuditStrategy() {
        return clientAuditStrategy;
    }

    @Override
    public MllpAuditStrategy getServerAuditStrategy() {
        return serverAuditStrategy;
    }
    
    @Override
    public MllpTransactionConfiguration getTransactionConfiguration() {
        return CONFIGURATION;
    }

    @Override
    public Parser getParser() {
        return parser;
    }
}
