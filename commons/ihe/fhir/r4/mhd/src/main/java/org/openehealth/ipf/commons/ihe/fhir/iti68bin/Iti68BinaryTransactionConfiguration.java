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

package org.openehealth.ipf.commons.ihe.fhir.iti68bin;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdValidator;

/**
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti68BinaryTransactionConfiguration extends FhirTransactionConfiguration {

    public Iti68BinaryTransactionConfiguration() {

        super("mhd-iti68-bin",
                "Retrieve Document",
                false,
                null,
                new Iti68BinaryServerAuditStrategy(),
                FhirVersionEnum.R4,
            new Iti68BinaryResourceProvider(),
            new Iti68BinaryRequestFactory(),
            MhdValidator::new);
    }
}
