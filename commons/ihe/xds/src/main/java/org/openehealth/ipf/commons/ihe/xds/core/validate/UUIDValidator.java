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

import org.openehealth.ipf.commons.core.URN;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;
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
        requireNonNull(uuid, "uuid cannot be null");
        metaDataAssert(UUID_PATTERN.matcher(uuid).matches(), INVALID_UUID, uuid);
    }

    /**
     * @param id identifier
     * @return identifier if the id is not an UUID, Optional.empty otherwise
     */
    public Optional<String> getSymbolicId(String id) {
        return !isUUID(id) ? Optional.of(id) : Optional.empty();
    }

    /**
     * @param id identifier
     * @return true if identifier is a valid UUID
     */
    public boolean isUUID(String id) {
        return getAsUUID(id).isPresent();
    }

    /**
     * @param urn urn
     * @return UUID if uri is a valid UUID, Optional.empty otherwise
     */
    public Optional<UUID> getAsUUID(String urn) {
        try {
            return Optional.of(UUID.fromString(urn.substring("urn:uuid:".length())));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    /**
     * @param uri uri
     * @return UUID if uri is a valid UUID, Optional.empty otherwise
     */
    public Optional<UUID> getAsUUID(URI uri) {
        return getAsUUID(uri.toString());
    }

    /**
     * @param urn urn
     * @return UUID if uri is a valid UUID, Optional.empty otherwise
     */
    public Optional<UUID> getAsUUID(URN urn) {
        return getAsUUID(urn.toString());
    }
}
