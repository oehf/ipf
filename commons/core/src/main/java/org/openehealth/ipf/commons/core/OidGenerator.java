package org.openehealth.ipf.commons.core;

import lombok.experimental.UtilityClass;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Generate unique object identifiers without formal registration as 
 * mentioned in IHE_ITI_TF_Vol2x B.6 Representing UUIDs as OIDs
 * and specified by ITU-T X.667 (http://itu.int/ITU-T/X.667)
 *
 */
@UtilityClass
public class OidGenerator {
    public static final String UUID_ARC = "2.25";

    /**
     * 
     * @return A new unique OID, based on a random UUID.
     * 
     */
    public static Oid uniqueOid() {
        return asOid(UUID.randomUUID());
    }

    /**
     * 
     * @return The given UUID as an OID under the 2.25 arc.
     * 
     */
    public static Oid asOid(final UUID uuid) {
        try {
            return new Oid(UUID_ARC + "." + asBigInteger(uuid).toString());
        } catch (final GSSException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private static BigInteger asBigInteger(final UUID uuid) {
        return asUnsignedBigInteger(uuid.getMostSignificantBits()).shiftLeft(64)
                .or(asUnsignedBigInteger(uuid.getLeastSignificantBits()));
    }

    private static BigInteger asUnsignedBigInteger(final long longValue) {
        if (longValue >= 0) {
            return BigInteger.valueOf(longValue);
        } else {
            return BigInteger.valueOf(longValue & Long.MAX_VALUE).setBit(63);
        }
    }
}
