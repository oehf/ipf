/*
 * Copyright 2026 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.JwtDataSet;
import org.openehealth.ipf.commons.audit.JwtExtractorProperties;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * An access token the audit source cannot look into is still worth recording: that a token was presented
 * is part of what happened. What must not be recorded is the token itself, which is a credential.
 *
 * @author Christian Ohr
 */
public class OpaqueAccessTokenTest {

    private static final String OPAQUE_TOKEN = "8f1e2d3c4b5a69788f1e2d3c4b5a6978";

    private final JwtExtractorProperties properties = new JwtExtractorProperties();

    @Test
    public void testAReferenceTokenIsRecordedAsOpaque() {
        var dataSet = parse("Bearer " + OPAQUE_TOKEN);

        assertTrue(dataSet.isOpaque());
        assertNull(dataSet.getSubject());
        assertNull(dataSet.getJwtId());
    }

    /**
     * An encrypted JWT parses, but its claims stay sealed without the key -- which the audit source does
     * not have. Reading them used to throw, and since auditing runs in the {@code finally} of the
     * interceptors, that turned a served request into a server error.
     */
    @Test
    public void testAnEncryptedJwtIsRecordedAsOpaque() {
        var dataSet = assertDoesNotThrow(() -> parse("Bearer " + encryptedJwt()));

        assertTrue(dataSet.isOpaque());
        assertNull(dataSet.getSubject());
        assertNull(dataSet.getJwtId());
    }

    @Test
    public void testAReadableJwtIsNotOpaque() {
        var dataSet = parse("Bearer " + new JwtGenerator().next());

        assertFalse(dataSet.isOpaque());
    }

    @Test
    public void testNoTokenAtAllYieldsNothing() {
        assertTrue(JwtParser.parseAuthorizationToJwtDataSet(null, properties).isEmpty());
        assertTrue(JwtParser.parseAuthorizationToJwtDataSet("", properties).isEmpty());
        assertTrue(JwtParser.parseAuthorizationToJwtDataSet("Bearer ", properties).isEmpty());
        assertTrue(JwtParser.parseAuthorizationToJwtDataSet("Basic dXNlcjpwdw==", properties).isEmpty());
    }

    private JwtDataSet parse(String authorization) {
        return JwtParser.parseAuthorizationToJwtDataSet(authorization, properties).orElseThrow();
    }

    private String encryptedJwt() throws Exception {
        var jwk = new RSAKeyGenerator(2048)
            .keyUse(KeyUse.ENCRYPTION)
            .keyID(UUID.randomUUID().toString())
            .generate();
        var jwe = new EncryptedJWT(
            new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM),
            new JWTClaimsSet.Builder()
                .subject("urn:oid:1.2.3.4|user")
                .jwtID(UUID.randomUUID().toString())
                .build());
        jwe.encrypt(new RSAEncrypter(jwk.toRSAPublicKey()));
        return jwe.serialize();
    }

}
