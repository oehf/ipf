/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.iti105;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.fhir.iti105.Iti105AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MHD.SimplifiedPublishInteractions.ITI_105;

/**
 * Component for MHD Simplified Publish (ITI-105)
 *
 * @author Boris Stanojevic
 * @since 4.8
 */
public class Iti105Component extends FhirComponent<Iti105AuditDataset> {


    public Iti105Component() {
        super(ITI_105);
    }

    public Iti105Component(CamelContext context) {
        super(context, ITI_105);
    }

    @Override
    protected Iti105Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<Iti105AuditDataset> config) {
        return new Iti105Endpoint(uri, this, config);
    }


}
