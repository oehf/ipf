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

package org.openehealth.ipf.commons.ihe.fhir.audit;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Map;

/**
 * Audit dataset that supports generic FHIR transactions (general REST API) for which there
 * is no explicit ATNA record format defined. This may include all operation types.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class GenericFhirAuditDataset extends FhirAuditDataset {

    @Getter
    @Setter
    private String affectedResourceType;

    @Getter
    @Setter
    private RestOperationTypeEnum operation;

    @Getter
    @Setter
    private IIdType resourceId;

    @Getter
    @Setter
    private String securityLabel;

    @Getter
    @Setter
    private String queryString;

    @Getter
    @Setter
    private String operationName;

    @Getter
    @Setter
    private Map<String, Object> operationParameters;

    public GenericFhirAuditDataset(boolean serverSide) {
        super(serverSide);
    }
}
