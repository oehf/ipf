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

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.Mapping;
import org.openehealth.ipf.commons.map.MappingWriter;
import org.openehealth.ipf.commons.map.Unmatched;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.List;

/**
 * Writes {@link Mapping}s as a FHIR R4 {@code ConceptMap}, so that a mapping maintained in any of
 * IPF's formats can be published as the standard resource for the job - authored on from there in
 * Simplifier or Forge, shipped in an Implementation Guide, or served by a terminology server.
 * <p>
 * One document holds one {@code ConceptMap} with one group per mapping, each carrying the
 * {@link ConceptMapExtensions#MAPPING_NAME} extension so that names survive the round trip
 * whatever the resource is called.
 *
 * @since 6.0
 */
public class ConceptMapWriter implements MappingWriter {

    private static final FhirContext CONTEXT = FhirContext.forR4();

    private static final String URL_BASE = "http://openehealth.org/ipf/ConceptMap/";

    @Override
    public String format() {
        return ConceptMapLoader.JSON_FORMAT;
    }

    @Override
    public String extension() {
        return ConceptMapLoader.JSON_EXTENSION;
    }

    @Override
    public void write(List<? extends Mapping> mappings, Writer out) {
        if (mappings.isEmpty()) {
            throw new IllegalArgumentException("A ConceptMap needs at least one group");
        }
        var conceptMap = new ConceptMap();
        conceptMap.setId(id(mappings.get(0).name()));
        conceptMap.setName(mappings.get(0).name());
        conceptMap.setUrl(URL_BASE + id(mappings.get(0).name()));
        conceptMap.setStatus(Enumerations.PublicationStatus.ACTIVE);
        mappings.forEach(mapping -> conceptMap.addGroup(toGroup(mapping)));

        try {
            CONTEXT.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(conceptMap, out);
            out.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write mappings as a ConceptMap", e);
        }
    }

    public String toConceptMap(List<? extends Mapping> mappings) {
        var out = new java.io.StringWriter();
        write(mappings, out);
        return out.toString();
    }

    private static ConceptMap.ConceptMapGroupComponent toGroup(Mapping mapping) {
        var group = new ConceptMap.ConceptMapGroupComponent();
        ConceptMapExtensions.setMappingName(group, mapping.name());
        if (!mapping.reversible()) {
            ConceptMapExtensions.setReversible(group, false);
        }
        if (mapping.override()) {
            ConceptMapExtensions.setOverride(group);
        }
        if (mapping.keySystem() != null) {
            group.setSource(mapping.keySystem());
        }
        if (mapping.valueSystem() != null) {
            group.setTarget(mapping.valueSystem());
        }

        mapping.entries().forEach(entry -> {
            requireCode(mapping, entry.key(), "key");
            requireCode(mapping, entry.value(), "value");
            var element = group.addElement()
                    .setCode(entry.key())
                    .setDisplay(entry.keyDisplay());
            element.addTarget()
                    .setCode(entry.value())
                    .setDisplay(entry.valueDisplay())
                    .setEquivalence(equivalence(entry.equivalence()));
        });

        unmapped(mapping, group, mapping.unmatched());
        reverseUnmapped(mapping, group, mapping.reverseUnmatched());
        return group;
    }

    private static Enumerations.ConceptMapEquivalence equivalence(Equivalence equivalence) {
        return switch (equivalence) {
            case EQUAL -> Enumerations.ConceptMapEquivalence.EQUAL;
            case EQUIVALENT -> Enumerations.ConceptMapEquivalence.EQUIVALENT;
            // the model says how the key relates to the value, R4 how the target relates to the source
            case WIDER -> Enumerations.ConceptMapEquivalence.NARROWER;
            case NARROWER -> Enumerations.ConceptMapEquivalence.WIDER;
            case INEXACT -> Enumerations.ConceptMapEquivalence.INEXACT;
            case DISJOINT -> Enumerations.ConceptMapEquivalence.DISJOINT;
        };
    }

    private static void unmapped(Mapping mapping, ConceptMap.ConceptMapGroupComponent group,
                                 Unmatched unmatched) {
        if (unmatched instanceof Unmatched.Absent) {
            return;
        }
        if (unmatched instanceof Unmatched.Identity) {
            group.getUnmapped().setMode(ConceptMap.ConceptMapGroupUnmappedMode.PROVIDED);
        } else if (unmatched instanceof Unmatched.Fixed fixed) {
            requireCode(mapping, fixed.value(), "unmatched value");
            group.getUnmapped()
                    .setMode(ConceptMap.ConceptMapGroupUnmappedMode.FIXED)
                    .setCode(fixed.value());
        } else if (unmatched instanceof Unmatched.Computed computed) {
            ConceptMapExtensions.setUnmappedFunction(group.getUnmapped(), computed.ref());
        } else if (unmatched instanceof Unmatched.Fail) {
            ConceptMapExtensions.setUnmappedFail(group.getUnmapped());
        } else if (unmatched instanceof Unmatched.Delegate delegate) {
            if (delegate.mapping().contains("/")) {
                throw new IllegalArgumentException("A ConceptMap cannot delegate mapping '" + mapping.name()
                        + "' to '" + delegate.mapping() + "': the name of the mapping delegated to is the"
                        + " last path segment of a url, so it cannot contain '/'");
            }
            group.getUnmapped()
                    .setMode(ConceptMap.ConceptMapGroupUnmappedMode.OTHERMAP)
                    // not id(..): the delegate is a mapping name, and the last path segment of
                    // this url is what reads it back, so it must survive verbatim
                    .setUrl(URL_BASE + delegate.mapping());
        } else {
            throw new IllegalArgumentException("A ConceptMap cannot express the unmatched behavior "
                    + unmatched);
        }
    }

    private static void reverseUnmapped(Mapping mapping, ConceptMap.ConceptMapGroupComponent group,
                                        Unmatched unmatched) {
        if (unmatched instanceof Unmatched.Absent) {
            return;
        }
        if (unmatched instanceof Unmatched.Identity) {
            ConceptMapExtensions.setReverseUnmappedMode(group, "provided");
        } else if (unmatched instanceof Unmatched.Fixed fixed) {
            requireCode(mapping, fixed.value(), "reverse unmatched value");
            ConceptMapExtensions.setReverseUnmappedMode(group, "fixed");
            ConceptMapExtensions.setReverseUnmappedCode(group, fixed.value());
        } else if (unmatched instanceof Unmatched.Computed computed) {
            ConceptMapExtensions.setReverseUnmappedFunction(group, computed.ref());
        } else if (unmatched instanceof Unmatched.Fail) {
            ConceptMapExtensions.setReverseUnmappedMode(group, "fail");
        } else if (unmatched instanceof Unmatched.Delegate delegate) {
            ConceptMapExtensions.setReverseUnmappedDelegate(group, delegate.mapping());
        }
    }

    /**
     * A FHIR primitive cannot hold the empty string - an empty {@code code} is dropped on
     * serialization - so a mapping that uses it as a code has no ConceptMap representation. Two
     * of the mappings IPF ships do; they stay in the XML format.
     */
    private static void requireCode(Mapping mapping, String code, String what) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("A ConceptMap cannot express the missing or empty " + what
                    + " of mapping '" + mapping.name() + "': a FHIR code is never empty. Keep this"
                    + " mapping in a format that has no such restriction");
        }
        if (!code.equals(code.strip())) {
            throw new IllegalArgumentException("A ConceptMap cannot express the " + what + " '" + code
                    + "' of mapping '" + mapping.name() + "': FHIR trims leading and trailing whitespace"
                    + " from a code. Keep this mapping in a format that has no such restriction");
        }
    }

    /**
     * @return the name reduced to the characters a FHIR id allows
     */
    private static String id(String name) {
        var id = name.replaceAll("[^A-Za-z0-9.-]", "-");
        return id.length() <= 64 ? id : id.substring(0, 64);
    }
}
