/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.iti30;

import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.Version;
import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.core.TransactionOptionUtils;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti30.Iti30AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti30.Iti30ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti30.Iti30ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.pam.PamTransactions;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpointConfiguration;

import java.util.Map;

/**
 * Camel component for ITI-30 (Patient Identity Management).
 * The endpoints take an additional parameter "options", that refer to the
 * transaction options as specified in {@link Iti30Options} that need to be supported.
 *
 * @author Christian Ohr
 */
public class Iti30Component extends MllpTransactionComponent<Iti30AuditDataset> {

    private static final AuditStrategy<Iti30AuditDataset> CLIENT_AUDIT_STRATEGY =
            new Iti30ClientAuditStrategy();
    private static final AuditStrategy<Iti30AuditDataset> SERVER_AUDIT_STRATEGY =
            new Iti30ServerAuditStrategy();

    private Hl7v2TransactionConfiguration hl7v2TransactionConfiguration;
    private NakFactory nakFactory;


    public Iti30Component() {
        super();
    }

    public Iti30Component(CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    public AuditStrategy<Iti30AuditDataset> getClientAuditStrategy() {
        return CLIENT_AUDIT_STRATEGY;
    }

    @Override
    public AuditStrategy<Iti30AuditDataset> getServerAuditStrategy() {
        return SERVER_AUDIT_STRATEGY;
    }

    @Override
    protected MllpTransactionEndpointConfiguration createConfig(Map<String, Object> parameters) throws Exception {
        MllpTransactionEndpointConfiguration config = super.createConfig(parameters);
        String options = getAndRemoveParameter(parameters, "options", String.class, Iti30Options.MERGE.name());
        Iti30Options[] iti30Options = TransactionOptionUtils.split(options, Iti30Options.class);
        if (iti30Options == null) {
            throw new IllegalArgumentException("Options parameter for pam-iti30 is invalid");
        }
        initConfiguration(iti30Options);
        return config;
    }

    private void initConfiguration(Iti30Options... options) {
        hl7v2TransactionConfiguration = new Hl7v2TransactionConfiguration(
                new Version[]{Version.V25},
                "PIM adapter",
                "IPF",
                ErrorCode.APPLICATION_INTERNAL_ERROR,
                ErrorCode.APPLICATION_INTERNAL_ERROR,
                new String[]{"ADT"},
                TransactionOptionUtils.concatAllToString(options),
                new String[]{"ACK"},
                new String[]{"*"},
                new boolean[]{true},
                new boolean[]{false},
                HapiContextFactory.createHapiContext(PamTransactions.ITI30));
        nakFactory = new NakFactory(hl7v2TransactionConfiguration);
    }

    @Override
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return hl7v2TransactionConfiguration;
    }

    @Override
    public NakFactory getNakFactory() {
        return nakFactory;
    }
}
