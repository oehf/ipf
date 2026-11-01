/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core.responses;

import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.MatchedPatient;

import java.util.List;

/**
 * Response-specific properties of the simplified models of a patient demographics query response
 * (PRPA_IN201306UV02), shared by PDQv3 (ITI-47, {@link PdqV3QueryResponse}) and XCPD (ITI-55,
 * {@link XcpdQueryResponse}).
 *
 * @author Christian Ohr
 * @since 6.1
 */
public interface PatientDemographicsQueryResponse extends Hl7v3QueryResponse {

    /**
     * @return the matched patients. Only sent if the query response code is "OK". Never {@code null}.
     */
    List<MatchedPatient> getPatients();
}
