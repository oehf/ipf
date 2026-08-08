package org.openehealth.ipf.commons.ihe.fhir.audit.auth;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.JwtExtractorProperties;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtClaimsExtractorTest {

    private final JwtClaimsExtractor balpJwtClaimsExtractor = new JwtClaimsExtractor();
    private final JwtExtractorProperties jwtExtractorProperties = new JwtExtractorProperties();

    private final JwtGenerator balpJwtGenerator = new JwtGenerator();

    @Test
    void testExtractor() {
        var generatedJwt = balpJwtGenerator.next();

        var jwt = parseJWT(generatedJwt);
        assertNotNull(jwt);

        assertTrue(balpJwtClaimsExtractor.extractIssuer(jwt, jwtExtractorProperties).isPresent());
        assertEquals("https://localhost:8443/auth/realms/master", balpJwtClaimsExtractor.extractIssuer(jwt, jwtExtractorProperties).get());

        assertTrue(balpJwtClaimsExtractor.extractId(jwt, jwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractSubject(jwt, jwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractClientId(jwt, jwtExtractorProperties).isPresent());

        assertTrue(balpJwtClaimsExtractor.extractPersonId(jwt, jwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractHomeCommunityId(jwt, jwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractNationalProviderIdentifier(jwt, jwtExtractorProperties).isPresent());

        assertTrue(balpJwtClaimsExtractor.extractSubjectName(jwt, jwtExtractorProperties).isPresent());
        assertEquals("Dr. John Smith", balpJwtClaimsExtractor.extractSubjectName(jwt, jwtExtractorProperties).get());
        assertTrue(balpJwtClaimsExtractor.extractSubjectOrganization(jwt, jwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractSubjectOrganizationId(jwt, jwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractSubjectRole(jwt, jwtExtractorProperties).isPresent());
        assertEquals(2, balpJwtClaimsExtractor.extractSubjectRole(jwt, jwtExtractorProperties).get().size());
        assertTrue(balpJwtClaimsExtractor.extractSubjectRole(jwt, jwtExtractorProperties).get().contains("my-role-1"));
        assertTrue(balpJwtClaimsExtractor.extractSubjectRole(jwt, jwtExtractorProperties).get().contains("my-role-2"));
        assertTrue(balpJwtClaimsExtractor.extractPurposeOfUse(jwt, jwtExtractorProperties).isPresent());
        assertEquals(2, balpJwtClaimsExtractor.extractPurposeOfUse(jwt, jwtExtractorProperties).get().size());

        assertTrue(balpJwtClaimsExtractor.extractBppcAcp(jwt, jwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractBppcDocId(jwt, jwtExtractorProperties).isPresent());
        assertTrue(balpJwtClaimsExtractor.extractBppcPatientId(jwt, jwtExtractorProperties).isPresent());

        jwtExtractorProperties.setIssuerPath(new String[]{"blah"});
        jwtExtractorProperties.setAcpPath(new String[]{"extensions:ihe_blah"});
        assertTrue(balpJwtClaimsExtractor.extractIssuer(jwt, jwtExtractorProperties).isEmpty());
        assertTrue(balpJwtClaimsExtractor.extractBppcAcp(jwt, jwtExtractorProperties).isEmpty());

        jwtExtractorProperties.setAcpPath(null);
        assertTrue(balpJwtClaimsExtractor.extractBppcAcp(jwt, jwtExtractorProperties).isEmpty());

        jwtExtractorProperties.setAcpPath(new String[]{""});
        assertTrue(balpJwtClaimsExtractor.extractBppcAcp(jwt, jwtExtractorProperties).isEmpty());

    }

    private JWT parseJWT(String jwt) {
        try {
            return JWTParser.parse(jwt);
        } catch (ParseException pe) {
            return null;
        }
    }

}
