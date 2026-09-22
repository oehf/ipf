/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.support.map;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Element;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.StringType;

import java.util.Optional;

/**
 * The IPF extensions on a {@code ConceptMap}, in one place, so that {@link ConceptMapLoader} and
 * {@link ConceptMapWriter} agree on their URLs and shapes.
 * <p>
 * They are plain extensions rather than a HAPI custom resource class on purpose: all of them sit
 * on backbone elements - {@code group} and {@code group.unmapped} - and typing those would mean
 * redeclaring {@code ConceptMap}'s own {@code group} child with a different component type, which
 * fights the base structure for no gain. Typed accessors over the standard model give the same
 * safety at the only two call sites that need it.
 * <p>
 * They exist because the mapping model has four things ConceptMap has no field for. Everything
 * else - the codes, the code systems and the equivalences - is native.
 *
 * @since 6.0
 */
public final class ConceptMapExtensions {

    private static final String BASE = "http://openehealth.org/ipf/StructureDefinition/";

    /**
     * On {@code group}, {@code valueString}: the name to register the mapping under. Without it a
     * mapping is named after the resource, which cannot distinguish several groups.
     */
    public static final String MAPPING_NAME = BASE + "mapping-name";

    /**
     * On {@code group}, {@code valueBoolean}: whether a reverse index is built at all. A group
     * that is not reversible may map several codes onto one target without declaring how they
     * differ.
     */
    public static final String REVERSIBLE = BASE + "reversible";

    /**
     * On {@code group}, with the nested extensions below: what the reverse direction answers for
     * a target code that no element maps onto. ConceptMap only describes the forward direction.
     */
    public static final String REVERSE_UNMAPPED = BASE + "reverse-unmapped";

    /**
     * On {@code group.unmapped}, {@code valueString}: name of the function that computes the
     * answer for an unmapped code, for a fallback that is neither a constant nor the code itself.
     */
    public static final String UNMAPPED_FUNCTION = BASE + "unmapped-function";

    /** Nested in {@link #REVERSE_UNMAPPED}, {@code valueCode}: {@code provided}, {@code fixed} or {@code fail}. */
    public static final String MODE = "mode";

    /** Nested in {@link #REVERSE_UNMAPPED}, {@code valueCode}: the constant for {@code mode = fixed}. */
    public static final String CODE = "code";

    /** Nested in {@link #REVERSE_UNMAPPED}, {@code valueString}: a function name, instead of a mode. */
    public static final String FUNCTION = "function";

    /**
     * Nested in {@link #REVERSE_UNMAPPED}, {@code valueString}: the name of the mapping to delegate
     * the reverse direction to, instead of a mode. The forward direction says the same thing with
     * {@code unmapped.mode = other-map}, which is native.
     */
    public static final String DELEGATE = "delegate";

    private ConceptMapExtensions() {
    }

    // ------------------------------------------------------------------ reading

    public static Optional<String> mappingName(ConceptMap.ConceptMapGroupComponent group) {
        return stringValue(group.getExtensionByUrl(MAPPING_NAME));
    }

    public static Optional<Boolean> reversible(ConceptMap.ConceptMapGroupComponent group) {
        return stringValue(group.getExtensionByUrl(REVERSIBLE)).map(Boolean::parseBoolean);
    }

    public static Optional<Extension> reverseUnmapped(ConceptMap.ConceptMapGroupComponent group) {
        return Optional.ofNullable(group.getExtensionByUrl(REVERSE_UNMAPPED));
    }

    public static Optional<String> unmappedFunction(ConceptMap.ConceptMapGroupUnmappedComponent unmapped) {
        return stringValue(unmapped.getExtensionByUrl(UNMAPPED_FUNCTION));
    }

    /**
     * @param reverseUnmapped the {@link #REVERSE_UNMAPPED} extension
     * @param name            {@link #MODE}, {@link #CODE} or {@link #FUNCTION}
     */
    public static Optional<String> part(Extension reverseUnmapped, String name) {
        return stringValue(reverseUnmapped.getExtensionByUrl(name));
    }

    // ------------------------------------------------------------------ writing

    public static void setMappingName(ConceptMap.ConceptMapGroupComponent group, String name) {
        group.addExtension(new Extension(MAPPING_NAME, new StringType(name)));
    }

    public static void setReversible(ConceptMap.ConceptMapGroupComponent group, boolean reversible) {
        group.addExtension(new Extension(REVERSIBLE, new BooleanType(reversible)));
    }

    public static void setUnmappedFunction(ConceptMap.ConceptMapGroupUnmappedComponent unmapped, String ref) {
        unmapped.addExtension(new Extension(UNMAPPED_FUNCTION, new StringType(ref)));
    }

    public static void setReverseUnmappedMode(ConceptMap.ConceptMapGroupComponent group, String mode) {
        reverseUnmappedExtension(group).addExtension(new Extension(MODE, new CodeType(mode)));
    }

    public static void setReverseUnmappedCode(ConceptMap.ConceptMapGroupComponent group, String code) {
        reverseUnmappedExtension(group).addExtension(new Extension(CODE, new CodeType(code)));
    }

    public static void setReverseUnmappedFunction(ConceptMap.ConceptMapGroupComponent group, String ref) {
        reverseUnmappedExtension(group).addExtension(new Extension(FUNCTION, new StringType(ref)));
    }

    public static void setReverseUnmappedDelegate(ConceptMap.ConceptMapGroupComponent group, String mapping) {
        reverseUnmappedExtension(group).addExtension(new Extension(DELEGATE, new StringType(mapping)));
    }

    private static Extension reverseUnmappedExtension(ConceptMap.ConceptMapGroupComponent group) {
        return reverseUnmapped(group).orElseGet(() -> {
            var extension = new Extension(REVERSE_UNMAPPED);
            group.addExtension(extension);
            return extension;
        });
    }

    private static Optional<String> stringValue(Extension extension) {
        return extension == null || !extension.hasValue()
                ? Optional.empty()
                : Optional.ofNullable(((Element) extension.getValue()).primitiveValue());
    }
}
