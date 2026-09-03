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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti67;

import ca.uhn.fhir.rest.gclient.UriClientParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The targetCommunityIdList search parameter of the MHD 4.2.4 Target Communities Option
 * (CP-ITI-1326-02) must reach the route as part of the ITI-67 search parameters.
 */
public class TestIti67TargetCommunities extends AbstractTestIti67 {

    private static final String CONTEXT_DESCRIPTOR = "iti-67.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testTargetCommunityIdListReachesTheRoute() {
        Iti67TestRouteBuilder.resetLastSearchParameters();

        var result = client.search()
            .forResource(DocumentReference.class)
            .where(referencePatientIdentifierParameter())
            .where(new UriClientParam(MhdProfile.SP_TARGET_COMMUNITY_ID_LIST)
                .matches().values("urn:oid:1.2.3.4", "urn:oid:5.6.7.8"))
            .returnBundle(Bundle.class)
            .encodedXml()
            .execute();

        assertEquals(1, result.getTotal());

        var searchParameters = Iti67TestRouteBuilder.getLastSearchParameters();
        assertNotNull(searchParameters, "the route did not receive any search parameters");
        assertNotNull(searchParameters.getTargetCommunityIdList(), "targetCommunityIdList was not bound");
        assertEquals(
            List.of("urn:oid:1.2.3.4", "urn:oid:5.6.7.8"),
            searchParameters.getTargetCommunityIdList().getValuesAsQueryTokens().stream()
                .map(UriParam::getValue)
                .toList());
    }

    @Test
    public void testTargetCommunityIdListIsOptional() {
        Iti67TestRouteBuilder.resetLastSearchParameters();
        sendManually(referencePatientIdentifierParameter());
        var searchParameters = Iti67TestRouteBuilder.getLastSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(null, searchParameters.getTargetCommunityIdList());
    }
}
