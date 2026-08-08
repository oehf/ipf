/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.audit.auth;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.openehealth.ipf.commons.audit.JwtDataSet;
import org.openehealth.ipf.commons.audit.JwtExtractorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class JwtParser {

    private static final JwtClaimsExtractor claimsExtractor = new JwtClaimsExtractor();

    private static final Logger log = LoggerFactory.getLogger(JwtParser.class);

    /**
     * Reads what the audit record is to say about the access token the request carried.
     * <p>
     * A token that cannot be looked into is not the same as no token at all: it yields
     * {@link JwtDataSet#opaque()}, so that the record can state that a token was presented even though
     * its contents were not visible here. Only a request without a bearer token yields nothing.
     *
     * @param authenticationHeader contents of the Authorization header, may be null
     * @param jwtExtractorProperties where to find the claims
     * @return the claims, an opaque marker, or empty if the request carried no bearer token
     */
    public static Optional<JwtDataSet> parseAuthorizationToJwtDataSet(String authenticationHeader,
                                                                           JwtExtractorProperties jwtExtractorProperties) {
        if (bearerToken(authenticationHeader).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(parseAuthenticationToJWT(authenticationHeader)
            .map(jwt -> parseJwtToBalpDataSet(jwt, claimsExtractor, jwtExtractorProperties))
            .orElseGet(JwtDataSet::opaque));
    }

    /**
     * @param authenticationHeader contents of the Authorization header, may be null
     * @return the bearer token it carries, if it carries one
     */
    public static Optional<String> bearerToken(String authenticationHeader) {
        if (isBlank(authenticationHeader) ||
            !authenticationHeader.toLowerCase().startsWith("bearer ")) return Optional.empty();

        var bearer = authenticationHeader.replaceAll("^[Bb][Ee][Aa][Rr][Ee][Rr][ ]+", "");
        return isBlank(bearer) ? Optional.empty() : Optional.of(bearer);
    }

    /**
     * @param authenticationHeader contents of the Authorization header, may be null
     * @return the token as a JWT whose claims can be read, or empty when it is not one. An encrypted
     *      JWT counts as not one: it parses, but its claims stay sealed without the key, which the
     *      audit source does not have -- so it is as opaque here as a reference token.
     */
    public static Optional<JWT> parseAuthenticationToJWT(String authenticationHeader) {
        var bearer = bearerToken(authenticationHeader);
        if (bearer.isEmpty()) return Optional.empty();
        try {
            var jwt = JWTParser.parse(bearer.get());
            if (jwt.getJWTClaimsSet() == null) {
                log.debug("Access token is a JWT whose claims cannot be read without decrypting it");
                return Optional.empty();
            }
            return Optional.of(jwt);
        } catch (ParseException pe) {
            log.debug("Access token is not a JWT", pe);
            return Optional.empty();
        }
    }

    public static JwtDataSet parseJwtToBalpDataSet(JWT jwt,
                                                       JwtClaimsExtractor claimsExtractor,
                                                       JwtExtractorProperties jwtExtractorProperties) {
        var jwtDataSet = new JwtDataSet();

        claimsExtractor.extractIssuer(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIssuer);
        claimsExtractor.extractId(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setJwtId);
        claimsExtractor.extractClientId(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setClientId);
        claimsExtractor.extractSubject(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setSubject);

        claimsExtractor.extractBppcAcp(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheBppcAcp);
        claimsExtractor.extractBppcDocId(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheBppcDocId);
        claimsExtractor.extractBppcPatientId(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheBppcPatientId);

        claimsExtractor.extractSubjectName(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheIuaSubjectName);
        claimsExtractor.extractSubjectOrganization(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheIuaSubjectOrganization);
        claimsExtractor.extractSubjectOrganizationId(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheIuaSubjectOrganizationId);
        claimsExtractor.extractSubjectRole(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheIuaSubjectRole);
        claimsExtractor.extractHomeCommunityId(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheIuaHomeCommunityId);
        claimsExtractor.extractPurposeOfUse(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheIuaPurposeOfUse);
        claimsExtractor.extractNationalProviderIdentifier(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheIuaNationalProviderIdentifier);
        claimsExtractor.extractPersonId(jwt, jwtExtractorProperties).ifPresent(jwtDataSet::setIheIuaPersonId);

        return jwtDataSet;
    }
}
