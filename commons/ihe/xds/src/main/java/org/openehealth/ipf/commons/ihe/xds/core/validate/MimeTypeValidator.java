/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import static java.util.Objects.requireNonNull;
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
        requireNonNull(mimeType, "mimeType cannot be null");

        metaDataAssert(MIME_PATTERN.matcher(mimeType).matches(), ValidationMessage.MIME_TYPE_FORMAT, mimeType);
    }

}
