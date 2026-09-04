/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Christian Ohr
 */
public class SimplifiedPublishDocumentReferenceTest {

    private static final FhirContext FHIR_CONTEXT = FhirContext.forR4();

    static {
        MhdProfile.registerDefaultTypes(FHIR_CONTEXT);
    }

    /**
     * FHIR renders the attachment data as base64 (CP-ITI-1325-02), and HAPI does that encoding when
     * the resource is serialized. The data handed to the model must therefore be the plain document,
     * or it ends up encoded twice on the wire.
     */
    @Test
    public void testContentIsEncodedExactlyOnce() throws Exception {
        var content = "Hello IHE World".getBytes(StandardCharsets.UTF_8);
        var reference = new SimplifiedPublishDocumentReference().setContent("text/plain", content);

        var parsed = FHIR_CONTEXT.newJsonParser().parseResource(
            SimplifiedPublishDocumentReference.class,
            FHIR_CONTEXT.newJsonParser().encodeResourceToString(reference));

        var attachment = parsed.getContentFirstRep().getAttachment();
        assertArrayEquals(content, attachment.getData(),
            "the attachment data does not survive a serialization round trip");
        assertArrayEquals(MessageDigest.getInstance("SHA-1").digest(content), attachment.getHash(),
            "the attachment hash is not the SHA-1 digest of the document");
    }
}
