package org.openehealth.ipf.commons.core;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OidGeneratorTest {

    @Test
    public void uniqueOidInValidFormat() {
        Oid uniqueOid = OidGenerator.uniqueOid();
        assertTrue(uniqueOid.toString().startsWith(OidGenerator.UUID_ARC));
        assertTrue(uniqueOid.toString().matches("[1-9][0-9]*(\\.(0|([1-9][0-9]*)))+"));
    }

    @Test
    public void ensureUniqueGeneration() {
        Oid uniqueOid1 = OidGenerator.uniqueOid();
        Oid uniqueOid2 = OidGenerator.uniqueOid();
        assertNotEquals(uniqueOid1, uniqueOid2);
    }

    @Test
    public void oidFromUuid() throws GSSException {
        Oid oidFromUuid = OidGenerator.asOid(UUID.fromString("06846597-127e-4a3e-b7ac-4bfac249dde9"));
        assertEquals(new Oid("2.25.8662811652582006399125219556761132521"), oidFromUuid);
    }

}
