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

package org.openehealth.ipf.commons.ihe.fhir.iti78;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.IBundleProvider;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;
import org.openehealth.ipf.commons.ihe.fhir.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Resource Provider for PDQm (ITI-78) for DSTU2
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class Iti78ResourceProvider extends AbstractPlainProvider {

    /**
     * Handles the PDQm Query request
     *
     * @param identifiers             patient identifier search parameter
     * @param family                  family name search parameter(s)
     * @param given                   family name search parameter(s)
     * @param birthDate               birth date search parameter
     * @param address                 address search parameter
     * @param gender                  gender search parameter
     * @param resourceId              _id search parameter
     * @param telecom                 telecom search parameter
     * @param multipleBirthNumber     multiple birth number search parameter (pediatric option)
     * @param mothersMaidenNameGiven  mothers maiden given name search parameter(s) (pediatric option)
     * @param mothersMaidenNameFamily mothers maiden family name search parameter(s) (pediatric option)
     * @param httpServletRequest      servlet request
     * @param httpServletResponse     servlet response
     * @return {@link IBundleProvider} instance that manages retrieving patients
     */
    @SuppressWarnings("unused")
    @Search(type = PdqPatient.class)
    public IBundleProvider pdqmSearch(
            @OptionalParam(name = Patient.SP_IDENTIFIER) TokenAndListParam identifiers,
            @OptionalParam(name = Constants.TARGET_SYSTEM_NAME) UriAndListParam targetSystems,
            @OptionalParam(name = Patient.SP_FAMILY) StringAndListParam family,
            @OptionalParam(name = Patient.SP_GIVEN) StringAndListParam given,
            @OptionalParam(name = Patient.SP_BIRTHDATE) DateParam birthDate,
            @OptionalParam(name = Patient.SP_ADDRESS) StringParam address,
            @OptionalParam(name = Patient.SP_GENDER) TokenParam gender,
            @OptionalParam(name = IAnyResource.SP_RES_ID) TokenParam resourceId,
            // below only relevant for pediatric option
            @OptionalParam(name = Patient.SP_TELECOM) StringParam telecom,
            @OptionalParam(name = Iti78Constants.SP_MULTIPLE_BIRTH_ORDER_NUMBER) NumberParam multipleBirthNumber,
            @OptionalParam(name = Iti78Constants.SP_MOTHERS_MAIDEN_NAME_GIVEN) StringAndListParam mothersMaidenNameGiven,
            @OptionalParam(name = Iti78Constants.SP_MOTHERS_MAIDEN_NAME_FAMILY) StringAndListParam mothersMaidenNameFamily,

            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        Map<String, Object> searchParameters = new HashMap<>();
        addParameter(searchParameters, Patient.SP_IDENTIFIER, identifiers);
        addParameter(searchParameters, Constants.TARGET_SYSTEM_NAME, targetSystems);
        addParameter(searchParameters, Patient.SP_FAMILY, family);
        addParameter(searchParameters, Patient.SP_GIVEN, given);
        addParameter(searchParameters, Patient.SP_BIRTHDATE, birthDate);
        addParameter(searchParameters, Patient.SP_ADDRESS, address);
        addParameter(searchParameters, Patient.SP_GENDER, gender);
        addParameter(searchParameters, IAnyResource.SP_RES_ID, resourceId);
        addParameter(searchParameters, Patient.SP_TELECOM, telecom);
        addParameter(searchParameters, Iti78Constants.SP_MULTIPLE_BIRTH_ORDER_NUMBER, multipleBirthNumber);
        addParameter(searchParameters, Iti78Constants.SP_MOTHERS_MAIDEN_NAME_GIVEN, mothersMaidenNameGiven);
        addParameter(searchParameters, Iti78Constants.SP_MOTHERS_MAIDEN_NAME_FAMILY, mothersMaidenNameFamily);

        // Run down the route
        return requestBundleProvider(null, searchParameters, httpServletRequest, httpServletResponse);
    }


    /**
     * Handles the PDQm Retrieve
     * @param id resource ID
     * @param httpServletRequest servlet request
     * @param httpServletResponse servlet response
     * @return patient resource
     */
    @SuppressWarnings("unused")
    @Read(version = true, type = Patient.class)
    public Patient pdqmRetrieve(
            @IdParam IdType id,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        // Run down the route
        return requestResource(id, Patient.class, httpServletRequest, httpServletResponse);
    }

}
