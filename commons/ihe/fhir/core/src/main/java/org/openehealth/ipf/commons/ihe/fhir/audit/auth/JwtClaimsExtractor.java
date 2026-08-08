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
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Getter;
import org.openehealth.ipf.commons.audit.JwtExtractorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JwtClaimsExtractor {

    private static final Logger log = LoggerFactory.getLogger(JwtClaimsExtractor.class);

    public Optional<String> extractId(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getIdPath()));
    }

    public Optional<String> extractClientId(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getClientIdPath()));
    }

    public Optional<String> extractSubject(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getSubjectPath()));
    }

    public Optional<String> extractIssuer(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getIssuerPath()));
    }

    public Optional<String> extractSubjectName(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getSubjectNamePath()));
    }

    public Optional<String> extractSubjectOrganization(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getSubjectOrganizationPath()));
    }

    public Optional<String> extractSubjectOrganizationId(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getSubjectOrganizationIdPath()));
    }

    public Optional<Set<String>> extractSubjectRole(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractListClaimFromJWT(jwt, jwtExtractorProperties.getSubjectRolePath()));
    }

    public Optional<Set<String>> extractPurposeOfUse(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractListClaimFromJWT(jwt, jwtExtractorProperties.getPurposeOfUsePath()));
    }

    public Optional<String> extractHomeCommunityId(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getHomeCommunityIdPath()));
    }

    public Optional<String> extractNationalProviderIdentifier(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getNationalProviderIdPath()));
    }

    public Optional<String> extractPersonId(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getPersonIdPath()));
    }

    public Optional<String> extractBppcPatientId(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getPatientIdPath()));
    }

    public Optional<String> extractBppcDocId(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getDocIdPath()));
    }

    public Optional<String> extractBppcAcp(JWT jwt, JwtExtractorProperties jwtExtractorProperties) {
        return Optional.ofNullable(extractStringClaimFromJWT(jwt, jwtExtractorProperties.getAcpPath()));
    }

    private String extractStringClaimFromJWT(JWT jwt, String[] expressions){
        var finalClaimForExpression = getFinalClaimSet(jwt, expressions);
        if (finalClaimForExpression.isPresent()) {
            var claimsSet = finalClaimForExpression.get().jwtClaimsSet();
            var expression = finalClaimForExpression.get().expression();
            try {
                return claimsSet.getStringClaim(expression);
            } catch (ParseException pe) {
                log.warn("Not string claims present for expression key '{}'", expression, pe);
            }
        }
        return null;
    }

    private Set<String> extractListClaimFromJWT(JWT jwt, String[] expressions){
        var finalClaimForExpression = getFinalClaimSet(jwt, expressions);
        if (finalClaimForExpression.isPresent()) {
            var claimsSet = finalClaimForExpression.get().jwtClaimsSet();
            var expression = finalClaimForExpression.get().expression();
            try {
                var values = claimsSet.getListClaim(expression);
                if (values != null && !values.isEmpty()) {
                    return values.stream().map(Objects::toString).collect(Collectors.toSet());
                }
            } catch (ParseException pe) {
                log.warn("Not list claims present for expression key '{}'", expression, pe);
            }
        }
        return null;
    }

    private Optional<ClaimSetPair> getFinalClaimSet(JWT jwt, String[] expressions) {
        if (expressions == null) {
            return Optional.empty();
        }
        for (var expression: expressions) {
            try {
                if (expression.contains(":")) {
                    var extracted = jwt.getJWTClaimsSet();
                    var structure = List.of(expression.split("\\:"));
                    Iterator<String> structureIterator = structure.listIterator();
                    String subExpression = null;
                    while (structureIterator.hasNext()) {
                        subExpression = structureIterator.next();
                        if (structureIterator.hasNext()) {
                            if (!containsClaim(extracted, subExpression)) {
                                break;
                            }
                            extracted = JWTClaimsSet.parse(extracted.getJSONObjectClaim(subExpression));
                        }
                    }
                    if (extracted != null && isNotBlank(subExpression) &&
                        extracted.getClaims().containsKey(subExpression)) {
                        return Optional.of(new ClaimSetPair(subExpression, extracted));
                    }
                } else {
                    if (jwt.getJWTClaimsSet().getClaims().containsKey(expression)) {
                        return Optional.of(new ClaimSetPair(expression, jwt.getJWTClaimsSet()));
                    }
                }
            } catch (ParseException pe) {
                log.debug("Not claimset present for expression key: {}", pe.getMessage());
            }
        }
        return Optional.empty();
    }

    private boolean containsClaim(JWTClaimsSet claimsSet, String name) {
        return claimsSet.getClaim(name) != null;
    }

    private record ClaimSetPair(@Getter String expression, @Getter JWTClaimsSet jwtClaimsSet) {
    }

}
