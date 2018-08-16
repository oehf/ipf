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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti31;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.core.TransactionOptionUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.HL7v2TransactionOption;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.FeedAuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.options.Iti31Options;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpointConfiguration;

import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.hl7v2.PAM.Interactions.ITI_31;

/**
 * Camel component for ITI-31 (Patient Encounter Management).
 * The endpoints take an additional parameter "options", that refer to the
 * transaction options as specified in {@link Iti31Options} that need to be supported.
 *
 * @author Christian Ohr
 */
public class Iti31Component extends MllpTransactionComponent<FeedAuditDataset> {


    public Iti31Component() {
        super(ITI_31);
    }

    public Iti31Component(CamelContext camelContext) {
        super(camelContext, ITI_31);
    }

    @Override
    protected MllpTransactionEndpointConfiguration createConfig(String uri, Map<String, Object> parameters) throws Exception {
        MllpTransactionEndpointConfiguration config = super.createConfig(uri, parameters);
        String options = getAndRemoveParameter(parameters, "options", String.class, Iti31Options.BASIC.name());
        List<? extends HL7v2TransactionOption> iti31Options = TransactionOptionUtils.split(options, Iti31Options.class);
        if (iti31Options.isEmpty()) {
            throw new IllegalArgumentException("Options parameter for pam-iti30 is invalid");
        }
        getInteractionId().init(iti31Options);
        return config;
    }

}
