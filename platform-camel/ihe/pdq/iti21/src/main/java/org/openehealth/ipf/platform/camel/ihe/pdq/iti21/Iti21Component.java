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
package org.openehealth.ipf.platform.camel.ihe.pdq.iti21;

import java.util.HashMap;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.openehealth.ipf.modules.hl7.parser.PipeParser;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.pdq.core.PdqClientAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.pdq.core.PdqServerAuditStrategy;

import ca.uhn.hl7v2.parser.Parser;

/**
 * Camel component for ITI-21 (PDQ).
 * @author Dmytro Rud
 */
public class Iti21Component extends MllpComponent {
    private static final MllpTransactionConfiguration CONFIGURATION =
        new MllpTransactionConfiguration(
                "2.5", 
                "PDQ adapter", 
                "IPF",
                207, 
                AckTypeCode.AR, 
                207, 
                new String[] {"QBP", "QCN"},
                new String[] {"Q22", "J01"},
                new boolean[] {true, false},
                new String[] {"RSP", null},
                new String[] {"K22", null}); 
  
    private static final MllpAuditStrategy CLIENT_AUDIT_STRATEGY = 
        new PdqClientAuditStrategy("PDQ");
    private static final MllpAuditStrategy SERVER_AUDIT_STRATEGY = 
        new PdqServerAuditStrategy("PDQ");
    private static final Parser PARSER;
    
    static {
        HashMap<String, String[]> map = new HashMap<String, String[]>();
        map.put("2.5", new String[] {"org.openehealth.ipf.platform.camel.ihe.pdq.core.def.v25"});
        CustomModelClassFactory factory = new CustomModelClassFactory(map); 
        PARSER = new PipeParser(factory);
    }
    
    
    public Iti21Component() {
        super();
    }

    public Iti21Component(CamelContext camelContext) {
        super(camelContext);
    }
    
    @Override
    public MllpAuditStrategy getClientAuditStrategy() {
        return CLIENT_AUDIT_STRATEGY;
    }

    @Override
    public MllpAuditStrategy getServerAuditStrategy() {
        return SERVER_AUDIT_STRATEGY;
    }
    
    @Override
    public MllpTransactionConfiguration getTransactionConfiguration() {
        return CONFIGURATION;
    }

    @Override
    public Parser getParser() {
        return PARSER;
    }
}
