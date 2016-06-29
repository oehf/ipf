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
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.IBundleProvider;
import org.hl7.fhir.instance.model.DocumentReference;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.Practitioner;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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
            @OptionalParam(name = DocumentReference.SP_INDEXED) DateOrListParam indexed,
            @OptionalParam(name = DocumentReference.SP_AUTHOR, chainWhitelist = {Practitioner.SP_FAMILY, Practitioner.SP_GIVEN}) StringAndListParam authorName,
            @OptionalParam(name = DocumentReference.SP_STATUS) TokenOrListParam status,
            @OptionalParam(name = DocumentReference.SP_CLASS) TokenOrListParam class_,
            @OptionalParam(name = DocumentReference.SP_TYPE) TokenOrListParam type,
            @OptionalParam(name = DocumentReference.SP_SETTING) TokenOrListParam setting,
            @OptionalParam(name = DocumentReference.SP_PERIOD) DateOrListParam period,
            @OptionalParam(name = DocumentReference.SP_FACILITY) TokenOrListParam facility,
            @OptionalParam(name = DocumentReference.SP_EVENT) TokenAndListParam event,
            @OptionalParam(name = DocumentReference.SP_SECURITYLABEL) TokenAndListParam securityLabel,
            @OptionalParam(name = DocumentReference.SP_FORMAT) TokenAndListParam format,
            @OptionalParam(name = DocumentReference.SP_RELATEDID) TokenAndListParam relatedId,

            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        Map<String, Object> searchParameters = new HashMap<>();

        String chain = patient.getChain();
        if (Patient.SP_IDENTIFIER.equals(chain)) {
            addParameter(searchParameters, DocumentReference.SP_SUBJECT + "." + Patient.SP_IDENTIFIER, patient.toTokenParam(getFhirContext()));
        } else if ("".equals(chain)) {
            addParameter(searchParameters, DocumentReference.SP_SUBJECT, patient);
        }

        addParameter(searchParameters, DocumentReference.SP_INDEXED, indexed);
        addParameter(searchParameters, DocumentReference.SP_AUTHOR, authorName);
        addParameter(searchParameters, DocumentReference.SP_STATUS, status);
        addParameter(searchParameters, DocumentReference.SP_CLASS, class_);
        addParameter(searchParameters, DocumentReference.SP_TYPE, type);
        addParameter(searchParameters, DocumentReference.SP_SETTING, setting);
        addParameter(searchParameters, DocumentReference.SP_PERIOD, period);
        addParameter(searchParameters, DocumentReference.SP_FACILITY, facility);
        addParameter(searchParameters, DocumentReference.SP_EVENT, event);
        addParameter(searchParameters, DocumentReference.SP_SECURITYLABEL, securityLabel);
        addParameter(searchParameters, DocumentReference.SP_FORMAT, format);
        addParameter(searchParameters, DocumentReference.SP_RELATEDID, relatedId);
        // Run down the route
        return requestBundleProvider(null, searchParameters, httpServletRequest, httpServletResponse);
    }

}
