package org.openehealth.ipf.commons.ihe.fhir.audit.auth;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.openehealth.ipf.commons.audit.BalpJwtExtractorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class BalpJwtParser {

    private static final BalpJwtClaimsExtractor claimsExtractor = new BalpJwtClaimsExtractor();

    private static final Logger log = LoggerFactory.getLogger(BalpJwtParser.class);

    public static Optional<BalpJwtDataSet> parseAuthorizationToBalpDataSet(String authenticationHeader,
                                                                           BalpJwtExtractorProperties balpJwtExtractorProperties) {
        var jwt = parseAuthenticationToJWT(authenticationHeader);
        return jwt.map(value -> {
            var balpJwtDataSet = parseJwtToBalpDataSet(value, claimsExtractor, balpJwtExtractorProperties);
            balpJwtDataSet.setOpaqueJwt(authenticationHeader.substring(authenticationHeader.length() - 32));
            return balpJwtDataSet;
        });
    }

    public static Optional<JWT> parseAuthenticationToJWT(String authenticationHeader) {
        if (isBlank(authenticationHeader) ||
            !authenticationHeader.toLowerCase().startsWith("bearer ")) return Optional.empty();

        var bearer = authenticationHeader.replaceAll("^[Bb][Ee][Aa][Rr][Ee][Rr][ ]+", "");
        try {
            return Optional.of(JWTParser.parse(bearer));
        } catch (ParseException pe) {
            log.debug("Invalid JWT token", pe);
            return Optional.empty();
        }
    }

    public static BalpJwtDataSet parseJwtToBalpDataSet(JWT jwt,
                                                       BalpJwtClaimsExtractor claimsExtractor,
                                                       BalpJwtExtractorProperties balpJwtExtractorProperties) {
        var balpJwtDataSet = new BalpJwtDataSet();

        claimsExtractor.extractIssuer(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIssuer);
        claimsExtractor.extractId(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setJwtId);
        claimsExtractor.extractClientId(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setClientId);
        claimsExtractor.extractSubject(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setSubject);

        claimsExtractor.extractBppcAcp(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheBppcAcp);
        claimsExtractor.extractBppcDocId(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheBppcDocId);
        claimsExtractor.extractBppcPatientId(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheBppcPatientId);

        claimsExtractor.extractSubjectName(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheIuaSubjectName);
        claimsExtractor.extractSubjectOrganization(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheIuaSubjectOrganization);
        claimsExtractor.extractSubjectOrganizationId(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheIuaSubjectOrganizationId);
        claimsExtractor.extractSubjectRole(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheIuaSubjectRole);
        claimsExtractor.extractHomeCommunityId(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheIuaHomeCommunityId);
        claimsExtractor.extractPurposeOfUse(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheIuaPurposeOfUse);
        claimsExtractor.extractNationalProviderIdentifier(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheIuaNationalProviderIdentifier);
        claimsExtractor.extractPersonId(jwt, balpJwtExtractorProperties).ifPresent(balpJwtDataSet::setIheIuaPersonId);

        return balpJwtDataSet;
    }
}
