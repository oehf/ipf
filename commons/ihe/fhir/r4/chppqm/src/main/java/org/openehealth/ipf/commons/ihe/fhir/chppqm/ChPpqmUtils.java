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
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Identifier;
import org.openehealth.ipf.commons.ihe.fhir.IgBasedFhirContextSupplier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
                    "classpath:/igs/ch-epr-fhir-4.0.1-ballot.tgz",
                    "classpath:/igs/ch-epr-term-2.0.9.tgz",
                    "classpath:/igs/ch-core-4.0.1.tgz");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static FhirContext getFhirContext() {
        return FHIR_CONTEXT;
    }

    public static final Set<String> TEMPLATE_IDS = Set.of("201", "202", "203", "301", "302", "303", "304");

    public static String getTemplateProfileUri(String templateId) {
        return Profiles.BASE_URL + "/PpqmConsentTemplate" + templateId;
    }

    public static final Set<String> TEMPLATE_PROFILE_URIS = TEMPLATE_IDS.stream()
        .map(ChPpqmUtils::getTemplateProfileUri)
        .collect(Collectors.toSet());

    public static class Profiles {
        public static final String BASE_URL = "http://fhir.ch/ig/ch-epr-fhir/StructureDefinition";
        public static final String FEED_REQUEST_BUNDLE      = BASE_URL + "/PpqmFeedRequestBundle";
        public static final String RETRIEVE_RESPONSE_BUNDLE = BASE_URL + "/PpqmRetrieveResponseBundle";
    }

    public static class CodingSystems {
        public static String CONSENT_IDENTIFIER_TYPE = "http://fhir.ch/ig/ch-epr-fhir/CodeSystem/PpqmConsentIdentifierType";
        public static String GLN = "urn:oid:2.51.1.3";
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

    public static String extractConsentIdFromUrl(String url) {
        if (url.contains("?")) {
            url = url.substring(url.indexOf('?') + 1);
        }
        if (url.contains("#")) {
            url = url.substring(0, url.indexOf('#'));
        }
        List<NameValuePair> params = URLEncodedUtils.parse(url, StandardCharsets.UTF_8);
        for (NameValuePair param : params) {
            if (Consent.SP_IDENTIFIER.equals(param.getName())) {
                return param.getValue();
            }
        }
        return null;
    }

    /**
     * Extracts identifiers of consents from URLs in a bundle's entries
     * (primarily for PPQ-4 operation DELETE).
     */
    public static Set<String> extractConsentIdsFromEntryUrls(Bundle bundle) {
        return bundle.getEntry().stream()
                .map(entry -> extractConsentIdFromUrl(entry.getRequest().getUrl()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static String createUrl(Consent consent) {
        return consent.fhirType() + '?' + Consent.SP_IDENTIFIER + '=' + extractConsentId(consent, ConsentIdTypes.POLICY_SET_ID);
    }

    public static String createUrl(String consentId) {
        return "Consent?" + Consent.SP_IDENTIFIER + '=' + consentId;
    }

    public static Bundle createPpq4SubmitRequestBundle(Collection<Consent> consents, Bundle.HTTPVerb httpMethod) {
        Bundle bundle = new Bundle();
        bundle.setId(UUID.randomUUID().toString());
        bundle.setType(Bundle.BundleType.TRANSACTION);
        bundle.getMeta().addProfile(Profiles.FEED_REQUEST_BUNDLE);
        for (Consent consent : consents) {
            Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
            entry.getRequest().setMethod(httpMethod);
            entry.setResource(consent);
            switch (httpMethod) {
                case POST:
                    entry.getRequest().setUrl(consent.fhirType());
                    break;
                case PUT:
                    entry.setResource(consent);
                    entry.getRequest().setUrl(createUrl(consent));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported HTTP method " + httpMethod);
            }
            bundle.getEntry().add(entry);
        }
        return bundle;
    }

    public static Bundle createPpq4DeleteRequestBundle(Collection<String> consentIds) {
        Bundle bundle = new Bundle();
        bundle.setId(UUID.randomUUID().toString());
        bundle.setType(Bundle.BundleType.TRANSACTION);
        bundle.getMeta().addProfile(Profiles.FEED_REQUEST_BUNDLE);
        for (String consentId : consentIds) {
            Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
            entry.getRequest().setMethod(Bundle.HTTPVerb.DELETE);
            entry.getRequest().setUrl(createUrl(consentId));
            bundle.getEntry().add(entry);
        }
        return bundle;
    }

}
