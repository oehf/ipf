/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import lombok.Getter;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;

import java.util.Collection;
import java.util.Optional;

/**
 * The kinds of identifier MHD distinguishes on {@code DocumentReference.identifier} and
 * {@code List.identifier}, i.e. the codes of the {@code IHE.MHD.MHDIdentifierType} code system.
 * <p>
 * Up to MHD 4.2.3 the two were told apart by {@code Identifier.use} ({@code official} for the
 * entryUUID, {@code usual} for the uniqueId). MHD 4.2.4 replaced that with a fixed
 * {@code Identifier.type} coding and changed the slicing discriminator accordingly
 * (CP-ITI-1328-01). IPF writes both, so that the resources it builds validate against either
 * version of the profiles, and {@link #matches(Identifier)} reads both, so that identifiers
 * received from a peer that still only sets {@code use} are recognized as well.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public enum MhdIdentifierType {

    /** The entryUUID of the referenced XDS registry entry. */
    ENTRY_UUID("entryUUID", "Identifier type for XDS entryUUID", Identifier.IdentifierUse.OFFICIAL),

    /** The uniqueId of the referenced XDS object. */
    UNIQUE_ID("uniqueId", "Identifier type for XDS UniqueId", Identifier.IdentifierUse.USUAL);

    /** Canonical URL of the code system introduced by MHD 4.2.4. */
    public static final String CODE_SYSTEM = MhdProfile.MHD_IDENTIFIER_TYPE_CODE_SYSTEM;

    @Getter
    private final String code;

    @Getter
    private final String display;

    /**
     * The {@code Identifier.use} value that MHD 4.2.3 and earlier used to designate this kind of
     * identifier, and that IPF keeps writing and accepting for backwards compatibility.
     */
    @Getter
    private final Identifier.IdentifierUse legacyUse;

    MhdIdentifierType(String code, String display, Identifier.IdentifierUse legacyUse) {
        this.code = code;
        this.display = display;
        this.legacyUse = legacyUse;
    }

    /**
     * @return the {@code Identifier.type} coding required by MHD 4.2.4 for this kind of identifier
     */
    public CodeableConcept toCodeableConcept() {
        return new CodeableConcept().addCoding(new Coding(CODE_SYSTEM, code, display));
    }

    /**
     * Tells whether an identifier is of this kind. The MHD 4.2.4 {@code Identifier.type} coding
     * takes precedence; an identifier that carries no coding of the MHD code system at all is
     * classified by its {@code Identifier.use}, the way MHD 4.2.3 and earlier did.
     *
     * @param identifier identifier to inspect, may be {@code null}
     * @return {@code true} if the identifier denotes this kind of identifier
     */
    public boolean matches(Identifier identifier) {
        if (identifier == null) {
            return false;
        }
        var mhdCodingPresent = false;
        if (identifier.hasType()) {
            for (var coding : identifier.getType().getCoding()) {
                if (CODE_SYSTEM.equals(coding.getSystem())) {
                    if (code.equals(coding.getCode())) {
                        return true;
                    }
                    mhdCodingPresent = true;
                }
            }
        }
        return !mhdCodingPresent && legacyUse == identifier.getUse();
    }

    /**
     * Picks the identifier of this kind out of a collection of identifiers.
     *
     * @param identifiers identifiers to search, may be {@code null}
     * @return the first matching identifier, if any
     */
    public Optional<Identifier> find(Collection<? extends Identifier> identifiers) {
        if (identifiers == null) {
            return Optional.empty();
        }
        return identifiers.stream()
            .filter(this::matches)
            .map(Identifier.class::cast)
            .findFirst();
    }

    /**
     * Removes all identifiers of this kind from a modifiable collection of identifiers. Used by the
     * setters of the MHD resource types, because since MHD 4.2.4 both the entryUUID and the uniqueId
     * slice are restricted to at most one occurrence.
     *
     * @param identifiers identifiers to purge, may be {@code null}
     */
    public void removeFrom(Collection<? extends Identifier> identifiers) {
        if (identifiers != null) {
            identifiers.removeIf(this::matches);
        }
    }
}
