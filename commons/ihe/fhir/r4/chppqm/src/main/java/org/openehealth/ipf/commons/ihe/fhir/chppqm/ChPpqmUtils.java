/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.chppqm;

import ca.uhn.fhir.context.FhirContext;
import lombok.experimental.UtilityClass;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.r4.model.*;
import org.openehealth.ipf.commons.ihe.fhir.IgBasedFhirContextSupplier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dmytro Rud
 * @since
 */
@UtilityClass
public class ChPpqmUtils {

    public static final FhirContext FHIR_CONTEXT;

    static {
        try {
            FHIR_CONTEXT = IgBasedFhirContextSupplier.getContext(
                    FhirContext.forR4(),
                    "classpath:/igs/ch-ppqm-2.0.0.tgz",
                    "classpath:/igs/ch-epr-term-2.0.9.tgz");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static FhirContext getFhirContext() {
        return FHIR_CONTEXT;
    }

    public static class Profiles {
        public static final String CONSENT         = "http://fhir.ch/ig/ch-epr-ppqm/StructureDefinition/PpqmConsent";
        public static final String REQUEST_BUNDLE  = "http://fhir.ch/ig/ch-epr-ppqm/StructureDefinition/PpqmRequestBundle";
        public static final String RESPONSE_BUNDLE = "http://fhir.ch/ig/ch-epr-ppqm/StructureDefinition/PpqmResponseBundle";
    }

    public static class CodingSystems {
        public static String CONSENT_IDENTIFIER_TYPE = "http://fhir.ch/ig/ch-epr-ppqm/CodeSystem/PpqmConsentIdentifierType";
    }

    public static class ConsentIdTypes {
        public static String POLICY_SET_ID = "policySetId";
        public static String TEMPLATE_ID = "templateId";
    }

    public static String extractConsentId(Consent consent, String idType) {
        for (Identifier identifier : consent.getIdentifier()) {
            for (Coding coding : identifier.getType().getCoding()) {
                if (ChPpqmUtils.CodingSystems.CONSENT_IDENTIFIER_TYPE.equalsIgnoreCase(coding.getSystem()) &&
                        idType.equals(coding.getCode()))
                {
                    return identifier.getValue();
                }
            }
        }
        return null;
    }

    public static String extractResourceIdForDelete(Object requestData) {
        if (requestData instanceof Consent) {
            Consent consent = (Consent) requestData;
            return extractConsentId(consent, ConsentIdTypes.POLICY_SET_ID);
        } else if (requestData instanceof String) {
            return (String) requestData;
        }
        return null;
    }

    /**
     * Extracts identifiers of consents from URLs in a bundle's entries
     * (primarily for PPQ-4 operation DELETE).
     */
    public static Set<String> extractConsentIdsFromEntryUrls(Bundle bundle) {
        Set<String> result = new HashSet<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            List<NameValuePair> params = URLEncodedUtils.parse(entry.getRequest().getUrl(), StandardCharsets.UTF_8);
            for (NameValuePair param : params) {
                if ("identifier".equals(param.getName())) {
                    result.add(param.getValue());
                }
            }
        }
        return result;
    }

}
