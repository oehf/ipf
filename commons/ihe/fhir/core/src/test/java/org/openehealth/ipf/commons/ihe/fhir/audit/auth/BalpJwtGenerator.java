package org.openehealth.ipf.commons.ihe.fhir.audit.auth;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BalpJwtGenerator implements Generator<String> {

    private static final Generator<String> strings = PrimitiveGenerators.letterStrings(10, 10);

    private static final Generator<Integer> integers = PrimitiveGenerators.integers(1000, 99999);

    @Override
    public String next() {
        try {
            RSAKey jwk = new RSAKeyGenerator(2048)
                .keyUse(KeyUse.SIGNATURE)
                .keyID(UUID.randomUUID().toString())
                .generate();

            SignedJWT signedJWT = new SignedJWT(jwsHeader(jwk), jwsClaimsSet());
            signedJWT.sign(new RSASSASigner(jwk.toPrivateKey()));
            return signedJWT.serialize();
        } catch (Exception e) {
            return null;
        }
    }

    public static String anyValidJwtWithRSAKey(RSAKey rsaKey){
        try {
            SignedJWT signedJWT = new SignedJWT(jwsHeader(rsaKey), jwsClaimsSet());
            signedJWT.sign(new RSASSASigner(rsaKey.toPrivateKey()));
            return signedJWT.serialize();
        } catch (Exception e) {
            return null;
        }
    }

    private static JWSHeader jwsHeader(RSAKey rsaKey) {
        return new JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .keyID(rsaKey.getKeyID())
            .build();
    }

    private static JWTClaimsSet jwsClaimsSet(){
        return new JWTClaimsSet.Builder()
            .issuer("https://localhost:8443/auth/realms/master")
            .audience("master-realm")
            .subject(UUID.randomUUID().toString())
            .jwtID(UUID.randomUUID().toString())
            .claim("client_id", strings.next())
            .claim("typ", "Bearer")
            .claim("extensions", jwsIheExtensions())
            .notBeforeTime(Date.from(Instant.now()))
            .expirationTime(Date.from(Instant.now().plusSeconds(120)))
            .build();
    }

    private static Map<String, Map<String, Object>> jwsIheExtensions() {
        Map<String, Map<String, Object>> extensions = new HashMap<>();
        extensions.put("ihe_iua", jwsIheIuaExtensions());
        extensions.put("ihe_bppc", jwsIheBppcExtensions());

        return extensions;
    }

    private static Map<String, Object> jwsIheIuaExtensions() {
        Map<String, Object> iheIuaMap = new HashMap<>();
        iheIuaMap.put("subject_name", "Dr. John Smith");
        iheIuaMap.put("subject_organization", "Central Hospital");
        iheIuaMap.put("subject_organization_id", "urn:oid:1.2.3." + integers.next());
        iheIuaMap.put("subject_role", List.of("my-role-1", "my-role-2"));
        iheIuaMap.put("purpose_of_use", List.of("1.0.14265.1", "1.0.14265.2"));
        iheIuaMap.put("home_community_id", "urn:oid:1.2.3." + integers.next());
        iheIuaMap.put("national_provider_identifier", "urn:oid:1.2.3." + integers.next());
        iheIuaMap.put("person_id", "ABC" + integers.next());
        return iheIuaMap;
    }

    private static Map<String, Object> jwsIheBppcExtensions() {
        Map<String, Object> bppcMap = new HashMap<>();
        bppcMap.put("patient_id", integers.next() + "^^^&amp;1.2.840.113619.6.197&amp;ISO");
        bppcMap.put("doc_id", "urn:oid:1.2.3." + integers.next());
        bppcMap.put("acp", "urn:oid:1.2.3." + integers.next());
        return bppcMap;
    }

}

