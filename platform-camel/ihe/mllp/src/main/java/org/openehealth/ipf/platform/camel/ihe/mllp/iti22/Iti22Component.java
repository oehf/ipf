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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti22;

import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.Version;
import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.QueryAuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti22.Iti22ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti22.Iti22ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerSegmentEchoingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.QpdAwareNakFactory;
import org.openehealth.ipf.platform.camel.ihe.mllp.pdqcore.PdqTransactionConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * Camel component for ITI-22 (PDQ).
 * @author Dmytro Rud
 */
public class Iti22Component extends MllpTransactionComponent<QueryAuditDataset> {
    public static final Hl7v2TransactionConfiguration CONFIGURATION =
        new PdqTransactionConfiguration(
                new Version[] {Version.V25},
                "PDQ adapter", 
                "IPF",
                ErrorCode.APPLICATION_INTERNAL_ERROR,
                ErrorCode.APPLICATION_INTERNAL_ERROR,
                new String[] {"QBP", "QCN"},
                new String[] {"ZV1", "J01"},
                new String[] {"RSP", "ACK"},
                new String[] {"ZV2", "*"}, 
                new boolean[] {true, false},                
                new boolean[] {true, false},
                HapiContextFactory.createHapiContext(
                        CustomModelClassUtils.createFactory("pdq", "2.5"),
                        PixPdqTransactions.ITI22));
    private static final AuditStrategy<QueryAuditDataset> CLIENT_AUDIT_STRATEGY =
        new Iti22ClientAuditStrategy();
    private static final AuditStrategy<QueryAuditDataset> SERVER_AUDIT_STRATEGY =
        new Iti22ServerAuditStrategy();
    private static final NakFactory NAK_FACTORY =
        new QpdAwareNakFactory(CONFIGURATION, "RSP", "ZV2");


    public Iti22Component() {
        super();
    }

    public Iti22Component(CamelContext camelContext) {
        super(camelContext);
    }
    
    @Override
    public AuditStrategy<QueryAuditDataset> getClientAuditStrategy() {
        return CLIENT_AUDIT_STRATEGY;
    }

    @Override
    public AuditStrategy<QueryAuditDataset> getServerAuditStrategy() {
        return SERVER_AUDIT_STRATEGY;
    }
    
    @Override
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return CONFIGURATION;
    }

    @Override
    public NakFactory getNakFactory() {
        return NAK_FACTORY;
    }

    @Override
    public List<Interceptor> getAdditionalConsumerInterceptors() {
        return Collections.<Interceptor> singletonList(new ConsumerSegmentEchoingInterceptor("QPD"));
    }
}
