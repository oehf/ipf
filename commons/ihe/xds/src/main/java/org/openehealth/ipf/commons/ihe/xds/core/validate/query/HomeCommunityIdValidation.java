/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.validate.query;

import org.openehealth.ipf.commons.ihe.xds.XdsIntegrationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HomeCommunityIdValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.Arrays;

/**
 * Validator for home community ID attribute in stored queries.
 * <p>
 * See Section 3.18.4.1.2.3.8 in IHE ITI TF Vol. 2a.
 * @author Dmytro Rud
 */
public class HomeCommunityIdValidation implements QueryParameterValidation {

    private static final QueryParameter[] PATIENT_ID_PARAMETERS = new QueryParameter[]{
            QueryParameter.DOC_ENTRY_PATIENT_ID,
            QueryParameter.SUBMISSION_SET_PATIENT_ID,
            QueryParameter.FOLDER_PATIENT_ID,
            QueryParameter.PATIENT_ID};

    private final XdsIntegrationProfile.HomeCommunityIdOptionality optionality;

    public HomeCommunityIdValidation(XdsIntegrationProfile.HomeCommunityIdOptionality optionality) {
        this.optionality = optionality;
    }

    @Override
    public void validate(EbXMLAdhocQueryRequest<AdhocQueryRequest> request) throws XDSMetaDataException {
        final boolean homeCommunityRequired = switch (optionality) {
            case NEVER -> false;
            case ALWAYS -> true;
            case ON_MISSING_PATIENT_ID -> patientIdMissing(request);
        };

        var validator = new HomeCommunityIdValidator(homeCommunityRequired);
        validator.validate(request.getHome());
    }

    private static boolean patientIdMissing(EbXMLAdhocQueryRequest<AdhocQueryRequest> request) {
        return Arrays.stream(PATIENT_ID_PARAMETERS).allMatch(parameter ->
                request.getSlotValues(parameter.getSlotName()).isEmpty());
    }

}
