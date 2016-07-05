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

package org.openehealth.ipf.commons.ihe.fhir.iti67;

import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import org.hl7.fhir.instance.model.DocumentReference;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.Practitioner;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Resource Provider for MHD (ITI-67)
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class Iti67ResourceProvider extends AbstractPlainProvider {

    @SuppressWarnings("unused")
    @Search(type = DocumentReference.class)
    public IBundleProvider documentManifestSearch(
            @RequiredParam(name = DocumentReference.SP_PATIENT, chainWhitelist = {"", Patient.SP_IDENTIFIER}) ReferenceParam patient,
            @OptionalParam(name = DocumentReference.SP_INDEXED) DateRangeParam indexed,
            @OptionalParam(name = DocumentReference.SP_AUTHOR + "." +Practitioner.SP_FAMILY) StringParam authorFamilyName,
            @OptionalParam(name = DocumentReference.SP_AUTHOR + "." +Practitioner.SP_GIVEN) StringParam authorGivenName,
            @OptionalParam(name = DocumentReference.SP_STATUS) TokenOrListParam status,
            @OptionalParam(name = DocumentReference.SP_CLASS) TokenOrListParam class_,
            @OptionalParam(name = DocumentReference.SP_TYPE) TokenOrListParam type,
            @OptionalParam(name = DocumentReference.SP_SETTING) TokenOrListParam setting,
            @OptionalParam(name = DocumentReference.SP_PERIOD) DateRangeParam period,
            @OptionalParam(name = DocumentReference.SP_FACILITY) TokenOrListParam facility,
            @OptionalParam(name = DocumentReference.SP_EVENT) TokenOrListParam event,
            @OptionalParam(name = DocumentReference.SP_SECURITYLABEL) TokenOrListParam securityLabel,
            @OptionalParam(name = DocumentReference.SP_FORMAT) TokenOrListParam format,
            @OptionalParam(name = DocumentReference.SP_RELATEDID) TokenOrListParam relatedId,

            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        Iti67SearchParameters searchParameters = Iti67SearchParameters.builder()
                .indexed(indexed)
                .authorFamilyName(authorFamilyName)
                .authorGivenName(authorGivenName)
                .status(status)
                .class_(class_)
                .type(type)
                .setting(setting)
                .period(period)
                .facility(facility)
                .event(event)
                .securityLabel(securityLabel)
                .format(format)
                .relatedId(relatedId).build();

        String chain = patient.getChain();
        if (Patient.SP_IDENTIFIER.equals(chain)) {
            searchParameters.setPatientIdentifier(patient.toTokenParam(getFhirContext()));
        } else if ("".equals(chain)) {
            searchParameters.setPatientReference(patient);
        }

        // Run down the route
        return requestBundleProvider(null, searchParameters, httpServletRequest, httpServletResponse);
    }

}
