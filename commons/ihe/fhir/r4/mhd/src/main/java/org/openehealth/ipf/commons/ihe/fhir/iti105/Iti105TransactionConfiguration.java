/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti105;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;

/**
 * Standard Configuration for Iti105Component.
 *
 * @author Boris Stanojevic
 * @since 4.8
 */
public class Iti105TransactionConfiguration extends FhirTransactionConfiguration<Iti105AuditDataset> {

    public Iti105TransactionConfiguration() {
        super("mhd-iti105",
                "Simplified Publish",
                false,
                new Iti105ClientAuditStrategy(),
                new Iti105ServerAuditStrategy(),
                FhirVersionEnum.R4,
                new Iti105DocumentReferenceResourceProvider(),
                new Iti105RequestFactory(),
                Iti105Validator::new);
    }

}
