package org.openehealth.ipf.commons.ihe.fhir.audit.auth;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.BalpJwtExtractorProperties;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BalpJwtClaimsExtractorTest {

    private final BalpJwtClaimsExtractor balpJwtClaimsExtractor = new BalpJwtClaimsExtractor();
    private final BalpJwtExtractorProperties balpJwtExtractorProperties = new BalpJwtExtractorProperties();

    private final BalpJwtGenerator balpJwtGenerator = new BalpJwtGenerator();

    @Test
    void testExtractor() throws Exception {
        String generatedJwt = balpJwtGenerator.next();

        JWT jwt = parseJWT(generatedJwt);
        assertNotNull(jwt);

        assertTrue(balpJwtClaimsExtractor.extractIssuer(jwt, balpJwtExtractorProperties).isPresent());
        assertEquals("https://localhost:8443/auth/realms/master", balpJwtClaimsExtractor.extractIssuer(jwt, balpJwtExtractorProperties).get());

        assertTrue(balpJwtClaimsExtractor.extractId(jwt, balpJwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractSubject(jwt, balpJwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractClientId(jwt, balpJwtExtractorProperties).isPresent());

        assertTrue(balpJwtClaimsExtractor.extractPersonId(jwt, balpJwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractHomeCommunityId(jwt, balpJwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractNationalProviderIdentifier(jwt, balpJwtExtractorProperties).isPresent());

        assertTrue(balpJwtClaimsExtractor.extractSubjectName(jwt, balpJwtExtractorProperties).isPresent());
        assertEquals("Dr. John Smith", balpJwtClaimsExtractor.extractSubjectName(jwt, balpJwtExtractorProperties).get());
        assertTrue(balpJwtClaimsExtractor.extractSubjectOrganization(jwt, balpJwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractSubjectOrganizationId(jwt, balpJwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractSubjectRole(jwt, balpJwtExtractorProperties).isPresent());
        assertEquals(2, balpJwtClaimsExtractor.extractSubjectRole(jwt, balpJwtExtractorProperties).get().size());
        assertTrue(balpJwtClaimsExtractor.extractSubjectRole(jwt, balpJwtExtractorProperties).get().contains("my-role-1"));
        assertTrue(balpJwtClaimsExtractor.extractSubjectRole(jwt, balpJwtExtractorProperties).get().contains("my-role-2"));
        assertTrue(balpJwtClaimsExtractor.extractPurposeOfUse(jwt, balpJwtExtractorProperties).isPresent());
        assertEquals(2, balpJwtClaimsExtractor.extractPurposeOfUse(jwt, balpJwtExtractorProperties).get().size());

        assertTrue(balpJwtClaimsExtractor.extractBppcAcp(jwt, balpJwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractBppcDocId(jwt, balpJwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractBppcPatientId(jwt, balpJwtExtractorProperties).isPresent());

        balpJwtExtractorProperties.setIssuerPath(new String[]{"blah"});
        balpJwtExtractorProperties.setAcpPath(new String[]{"extensions:ihe_blah"});
        assertTrue(balpJwtClaimsExtractor.extractIssuer(jwt, balpJwtExtractorProperties).isEmpty());
        assertTrue(balpJwtClaimsExtractor.extractBppcAcp(jwt, balpJwtExtractorProperties).isEmpty());

        balpJwtExtractorProperties.setAcpPath(null);
        assertTrue(balpJwtClaimsExtractor.extractBppcAcp(jwt, balpJwtExtractorProperties).isEmpty());

        balpJwtExtractorProperties.setAcpPath(new String[]{""});
        assertTrue(balpJwtClaimsExtractor.extractBppcAcp(jwt, balpJwtExtractorProperties).isEmpty());

    }

    private JWT parseJWT(String jwt) {
        try {
            return JWTParser.parse(jwt);
        } catch (ParseException pe) {
            return null;
        }
    }

    private static final String jwtAsString = "{\n" +
        "  \"aud\": \"master-realm\",\n" +
        "  \"sub\": \"f7fc9091-7b8a-42e0-a829-6c4ba22d38b2\",\n" +
        "  \"extensions\": {\n" +
        "    \"ihe_iua\": {\n" +
        "      \"subject_organization_id\": \"urn:oid:1.2.3.19161\",\n" +
        "      \"home_community_id\": \"urn:oid:1.2.3.43740\",\n" +
        "      \"national_provider_identifier\": \"urn:oid:1.2.3.48200\",\n" +
        "      \"subject_role\": [\n" +
        "        \"my-role-1\",\n" +
        "        \"my-role-2\"\n" +
        "      ],\n" +
        "      \"purpose_of_use\": [\n" +
        "        \"1.0.14265.1\",\n" +
        "        \"1.0.14265.2\"\n" +
        "      ],\n" +
        "      \"subject_name\": \"Dr. John Smith\",\n" +
        "      \"subject_organization\": \"Central Hospital\",\n" +
        "      \"person_id\": \"ABC9586\"\n" +
        "    },\n" +
        "    \"ihe_bppc\": {\n" +
        "      \"patient_id\": \"31494^^^&amp;1.2.840.113619.6.197&amp;ISO\",\n" +
        "      \"doc_id\": \"urn:oid:1.2.3.29380\",\n" +
        "      \"acp\": \"urn:oid:1.2.3.32574\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"nbf\": 1706531233,\n" +
        "  \"iss\": \"https://localhost:8443/auth/realms/master\",\n" +
        "  \"typ\": \"Bearer\",\n" +
        "  \"exp\": 1706531353,\n" +
        "  \"jti\": \"e2093a98-9dcd-4947-b5cb-ee5b47c089c5\",\n" +
        "  \"client_id\": \"pbrBkyXksp\"\n" +
        "}";
}
