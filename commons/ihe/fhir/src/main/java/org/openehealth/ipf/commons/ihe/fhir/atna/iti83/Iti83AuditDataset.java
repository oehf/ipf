/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.atna.iti83;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.fhir.atna.FhirAuditDataset;

/**
 * @author Christian Ohr
 *
 * @since 3.1
 */
@SuppressWarnings("serial")
public class Iti83AuditDataset extends FhirAuditDataset {

    @Getter @Setter private String queryString;

    public Iti83AuditDataset(boolean serverSide) {
        super(serverSide);
    }
}
