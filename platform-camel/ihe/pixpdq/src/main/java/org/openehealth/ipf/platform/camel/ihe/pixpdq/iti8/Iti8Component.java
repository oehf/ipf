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
package org.openehealth.ipf.platform.camel.ihe.pixpdq.iti8;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.modules.hl7.parser.PipeParser;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionConfiguration;

import ca.uhn.hl7v2.parser.Parser;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.pixpdq.BasicNakFactory;

/**
 * Camel component for ITI-8 (PIX Feed).
 * @author Dmytro Rud
 */
public class Iti8Component extends MllpComponent {
    private static final MllpTransactionConfiguration CONFIGURATION =
        new MllpTransactionConfiguration(
                "2.3.1", 
                "PIX adapter", 
                "IPF", 
                207, 
                207, 
                new String[] {"ADT"},
                new String[] {"A01 A04 A05 A08 A40"},
                new String[] {"ACK"},
                new String[] {"*"},
                new boolean[] {true},
                new boolean[] {false}); 
  
    private static final MllpAuditStrategy CLIENT_AUDIT_STRATEGY = 
        new Iti8ClientAuditStrategy();
    private static final MllpAuditStrategy SERVER_AUDIT_STRATEGY = 
        new Iti8ServerAuditStrategy();
    private static final Parser PARSER = new PipeParser();
    private static final NakFactory NAK_FACTORY = new BasicNakFactory();

    
    public Iti8Component() {
        super();
    }

    public Iti8Component(CamelContext camelContext) {
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

    @Override
    public NakFactory getNakFactory() {
        return NAK_FACTORY;
    }
}
