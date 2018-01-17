package org.openehealth.ipf.commons.core;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class URNTest {

    @Test
    public void testCorrectOID() throws URISyntaxException {
        String nss = "1.2.3.4";
        String text = "urn:OID:" + nss;
        assertTrue(URN.isURN(text));
        URN urn = URN.create(text);
        assertEquals("OID", urn.getNamespaceId());
        assertTrue(urn.isNamespace(URN.OID));
        assertEquals(nss, urn.getNamespaceSpecificString());
        assertEquals(text, urn.toString());
    }

    @Test
    public void testCorrectUUID() throws URISyntaxException {
        UUID uuid = UUID.randomUUID();
        URN urn = new URN(uuid);
        assertEquals("uuid", urn.getNamespaceId());
        assertTrue(urn.isNamespace(URN.UUID));
        assertEquals(uuid.toString(), urn.getNamespaceSpecificString());
    }

    @Test
    public void testInvalidURN() {
        assertFalse(URN.isURN("blorg:oid:1234"));
        assertFalse(URN.isURN("urn:oid"));
        assertFalse(URN.isURN("1234"));
        assertFalse(URN.isURN("http://hl7.org/fhir"));
    }

    @Test
    public void testPartConstructor() throws URISyntaxException {
        URN urn = new URN("isbn", "2384576256");
        assertEquals("urn:isbn:2384576256", urn.toString());
    }

    @Test
    public void testUri() throws URISyntaxException {
        String text = "urn:isbn:2384576256";
        URI uri = new URI(text);
        URN urn = new URN(uri);
        assertEquals(text, urn.toString());
        assertEquals(uri, urn.toURI());
    }

    @Test
    public void testEquals() throws URISyntaxException {
        assertEquals(URN.create("urn:oid:1.2.3.4"), URN.create("urn:OID:1.2.3.4"));
    }

    @Test
    public void testHashCode() throws URISyntaxException {
        Set<URN> set = new HashSet<>();
        set.add(URN.create("urn:oid:1.2.3.4"));
        assertTrue(set.contains(URN.create("urn:OID:1.2.3.4")));
    }
}
