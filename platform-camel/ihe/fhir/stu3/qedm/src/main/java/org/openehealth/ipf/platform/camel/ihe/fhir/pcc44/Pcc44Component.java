/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.pcc44;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.core.TransactionOptionUtils;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionOptions;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.pcc44.Pcc44Options;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.QEDM.Interactions.PCC_44;


/**
 * Component for QEDM (PCC-44)
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class Pcc44Component extends FhirComponent<FhirQueryAuditDataset> {


    public Pcc44Component() {
        super(PCC_44);
    }

    public Pcc44Component(CamelContext context) {
        super(context, PCC_44);
    }

    @Override
    protected Pcc44Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<FhirQueryAuditDataset> config) {
        return new Pcc44Endpoint(uri, this, config);
    }

    @Override
    protected FhirEndpointConfiguration<FhirQueryAuditDataset> createConfig(String remaining, Map<String, Object> parameters) throws Exception {
        String options = getAndRemoveParameter(parameters, "options", String.class, Pcc44Options.All.name());
        List<? extends FhirTransactionOptions> iti44Options = TransactionOptionUtils.split(options, Pcc44Options.class);
        if (iti44Options.isEmpty()) {
            throw new IllegalArgumentException("Options parameter for qedm-pcc44 is invalid");
        }
        getInteractionId().init(iti44Options);
        return super.createConfig(remaining, parameters);
    }

}
