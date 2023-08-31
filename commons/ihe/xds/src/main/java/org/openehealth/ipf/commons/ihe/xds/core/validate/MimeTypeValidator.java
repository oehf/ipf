package org.openehealth.ipf.commons.ihe.xds.core.validate;

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import java.util.regex.Pattern;

/**
 * Validate the basic format of a the document mime type.
 * 
 * Mime type must consist of type and subtype, separated by a /
 */
public class MimeTypeValidator implements ValueValidator {
    private static final Pattern MIME_PATTERN = Pattern.compile("\\w+\\/[-+.\\w]+");

    @Override
    public void validate(String mimeType) throws XDSMetaDataException {
        notNull(mimeType, "mimeType cannot be null");

        metaDataAssert(MIME_PATTERN.matcher(mimeType).matches(), ValidationMessage.MIME_TYPE_FORMAT, mimeType);
    }

}
