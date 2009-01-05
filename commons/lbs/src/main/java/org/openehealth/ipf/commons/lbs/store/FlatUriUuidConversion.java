package org.openehealth.ipf.commons.lbs.store;

import static org.apache.commons.lang.Validate.notNull;

import java.net.URI;
import java.util.UUID;

/**
 * Conversion strategy that maps a {@code UUID} to a flat {@code URI}.
 * @author Jens Riemschneider
 */
public class FlatUriUuidConversion extends UuidUriConversionStrategy {
    /**
     * Constructs the strategy via a base {@code URI}.
     * @param baseUri   
     *          see {@link UuidUriConversionStrategy#UuidUriConversionStrategy(URI)}
     *          for details
     */
    public FlatUriUuidConversion(URI baseUri) {
        super(baseUri);
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.UuidUriConversionStrategy#toUri(java.util.UUID)
     */
    @Override
    public URI toUri(UUID uuid) {
        notNull(uuid, "uuid cannot be null");        
        return getBaseUri().resolve(uuid.toString());
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.UuidUriConversionStrategy#toUuid(java.net.URI)
     */
    @Override
    public UUID toUuid(URI uri) {
        notNull(uri, "uri cannot be null");
        
        URI relativeUri = getBaseUri().relativize(uri);
        if (relativeUri == uri) {
            return null;
        }
        
        String path = relativeUri.getPath();
        try {
            return UUID.fromString(path);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
}