/*
 * Copyright 2015 the original author or authors.
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

import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_UUID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validator for UUIDs
 * @author Boris Stanojevic
 */
public class UUIDValidator implements ValueValidator {

    private static final Pattern UUID_PATTERN =
            Pattern.compile(".*[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");

    @Override
    public void validate(String uuid) throws XDSMetaDataException {
        notNull(uuid, "uuid cannot be null");
        metaDataAssert(UUID_PATTERN.matcher(uuid).matches(), INVALID_UUID, uuid);
    }
}
