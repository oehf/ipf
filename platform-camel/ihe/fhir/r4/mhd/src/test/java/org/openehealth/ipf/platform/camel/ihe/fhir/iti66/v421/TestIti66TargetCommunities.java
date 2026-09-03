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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti66.v421;

import ca.uhn.fhir.rest.gclient.UriClientParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The targetCommunityIdList search parameter of the MHD 4.2.4 Target Communities Option
 * (CP-ITI-1326-02) must reach the route as part of the ITI-66 search parameters.
 */
public class TestIti66TargetCommunities extends AbstractTestIti66 {

    private static final String CONTEXT_DESCRIPTOR = "v421/iti-66.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testTargetCommunityIdListReachesTheRoute() {
        Iti66TestRouteBuilder.resetLastSearchParameters();

        sendManually(
            listPatientIdentifierParameter(),
            new UriClientParam(MhdProfile.SP_TARGET_COMMUNITY_ID_LIST)
                .matches().values("urn:oid:1.2.3.4", "urn:oid:5.6.7.8"));

        var searchParameters = Iti66TestRouteBuilder.getLastSearchParameters();
        assertNotNull(searchParameters, "the route did not receive any List search parameters");
        assertNotNull(searchParameters.getTargetCommunityIdList(), "targetCommunityIdList was not bound");
        assertEquals(
            List.of("urn:oid:1.2.3.4", "urn:oid:5.6.7.8"),
            searchParameters.getTargetCommunityIdList().getValuesAsQueryTokens().stream()
                .map(UriParam::getValue)
                .toList());
    }

    @Test
    public void testTargetCommunityIdListIsOptional() {
        Iti66TestRouteBuilder.resetLastSearchParameters();
        sendManually(listPatientIdentifierParameter());
        var searchParameters = Iti66TestRouteBuilder.getLastSearchParameters();
        assertNotNull(searchParameters);
        assertNull(searchParameters.getTargetCommunityIdList());
    }
}
