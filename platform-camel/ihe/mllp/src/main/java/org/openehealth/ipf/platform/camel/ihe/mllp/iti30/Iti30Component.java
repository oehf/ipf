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

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.hl7v2.TransactionOptionUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.FeedAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpointConfiguration;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.hl7v2.PAM.Interactions.ITI_30;

/**
 * Camel component for ITI-30 (Patient Identity Management).
 * The endpoints take an additional parameter "options", that refer to the
 * transaction options as specified in {@link Iti30Options} that need to be supported.
 *
 * @author Christian Ohr
 */
public class Iti30Component extends MllpTransactionComponent<FeedAuditDataset> {


    public Iti30Component() {
        super(ITI_30);
    }

    public Iti30Component(CamelContext camelContext) {
        super(camelContext, ITI_30);
    }

    @Override
    protected MllpTransactionEndpointConfiguration createConfig(String uri, Map<String, Object> parameters) throws Exception {
        MllpTransactionEndpointConfiguration config = super.createConfig(uri, parameters);
        String options = getAndRemoveParameter(parameters, "options", String.class, Iti30Options.MERGE.name());
        Iti30Options[] iti30Options = TransactionOptionUtils.split(options, Iti30Options.class);
        if (iti30Options == null) {
            throw new IllegalArgumentException("Options parameter for pam-iti30 is invalid");
        }
        getInteractionId().init(iti30Options);
        return config;
    }

}
