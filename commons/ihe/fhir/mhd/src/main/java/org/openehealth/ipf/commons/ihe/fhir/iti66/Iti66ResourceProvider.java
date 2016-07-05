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

package org.openehealth.ipf.commons.ihe.fhir.iti66;

import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import org.hl7.fhir.instance.model.DocumentManifest;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.Practitioner;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Resource Provider for MHD (ITI-66)
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class Iti66ResourceProvider extends AbstractPlainProvider {

    @SuppressWarnings("unused")
    @Search(type = DocumentManifest.class)
    public IBundleProvider documentManifestSearch(
            @RequiredParam(name = DocumentManifest.SP_PATIENT, chainWhitelist = {"", Patient.SP_IDENTIFIER}) ReferenceParam patient,
            @OptionalParam(name = DocumentManifest.SP_CREATED) DateRangeParam created,
            @OptionalParam(name = DocumentManifest.SP_AUTHOR + "." + Practitioner.SP_FAMILY) StringParam authorFamilyName,
            @OptionalParam(name = DocumentManifest.SP_AUTHOR + "." + Practitioner.SP_GIVEN) StringParam authorGivenName,
            @OptionalParam(name = DocumentManifest.SP_TYPE) TokenOrListParam type,
            @OptionalParam(name = DocumentManifest.SP_STATUS) TokenOrListParam status,

            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {


        Iti66SearchParameters parameters = Iti66SearchParameters.builder()
                .created(created)
                .authorFamilyName(authorFamilyName)
                .authorGivenName(authorGivenName)
                .type(type)
                .status(status).build();

        String chain = patient.getChain();
        if (Patient.SP_IDENTIFIER.equals(chain)) {
            parameters.setPatientIdentifier(patient.toTokenParam(getFhirContext()));
        } else if ("".equals(chain)) {
            parameters.setPatientReference(patient);
        }

        // Run down the route
        return requestBundleProvider(null, parameters, httpServletRequest, httpServletResponse);
    }

}
