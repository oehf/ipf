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
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.openehealth.ipf.commons.map.CompositeMapping;
import org.openehealth.ipf.commons.map.Entry;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.Mapping;
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.MappingFunctionRegistry;
import org.openehealth.ipf.commons.map.MappingLoader;
import org.openehealth.ipf.commons.map.SimpleMapping;
import org.openehealth.ipf.commons.map.Unmatched;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

/**
 * Shared implementation for the two {@link MappingLoader}s that read FHIR R4 {@code ConceptMap}
 * resources: {@link ConceptMapJsonLoader} for {@code *.conceptmap.r4.json} and
 * {@link ConceptMapXmlLoader} for {@code *.conceptmap.r4.xml}. Everything below the parser is the
 * same for both, since the wire format is a matter of encoding, not of content.
 * <p>
 * There is one loader per wire format rather than one that reads both, because a
 * {@link MappingLoader#format() format id} selects exactly one loader and so has to say which
 * encoding to expect. A ConceptMap fetched from a terminology server carries no file name to
 * decide it. The FHIR version is part of both the ids and the extensions so that a loader for
 * another version can sit on the same classpath.
 * <p>
 * Most of IPF's mappings <em>are</em> concept maps, and for consumers who already govern their
 * terminology in ConceptMap - authored in Simplifier or Forge, shipped in an Implementation
 * Guide, validated by the FHIR validator. This loader lets IPF read that artifact directly
 * instead of duplicating it as an internal table. It is offered as a loader, not as IPF's own
 * format: the same mapping is a third longer in ConceptMap JSON than in the XML format, comments
 * have nowhere natural to go, and two of the model's five fallback modes need an extension, so an
 * IPF-flavored ConceptMap is not fully portable anyway.
 * <p>
 * Parsing is HAPI's job. This loader only walks {@code group -> element -> target} into
 * {@link Mapping} records and reads the IPF extensions.
 *
 * <h2>Field alignment</h2>
 * <table border="1">
 *     <caption>How the model maps onto ConceptMap</caption>
 *     <tr><th>Model</th><th>ConceptMap</th></tr>
 *     <tr><td>{@code keySystem}</td><td>{@code group.source}</td></tr>
 *     <tr><td>{@code valueSystem}</td><td>{@code group.target}</td></tr>
 *     <tr><td>{@code entry.key -> entry.value}</td><td>{@code group.element.code -> ...target.code}</td></tr>
 *     <tr><td>{@code entry.equivalence}</td><td>{@code ...target.equivalence}</td></tr>
 *     <tr><td>{@code unmatched} identity / fixed</td><td>{@code group.unmapped.mode} provided / fixed</td></tr>
 *     <tr><td>{@code unmatched} function</td><td>extension {@link ConceptMapExtensions#UNMAPPED_FUNCTION}</td></tr>
 *     <tr><td>{@code reverseUnmatched}</td><td>extension {@link ConceptMapExtensions#REVERSE_UNMAPPED}</td></tr>
 *     <tr><td>{@code reversible}</td><td>extension {@link ConceptMapExtensions#REVERSIBLE}</td></tr>
 *     <tr><td>{@code name}</td><td>extension {@link ConceptMapExtensions#MAPPING_NAME}, else derived from the canonical, see below</td></tr>
 * </table>
 *
 * <h2>Mapping names</h2>
 * A group carrying the {@link ConceptMapExtensions#MAPPING_NAME} extension is named by it. Otherwise
 * the name is derived from the ConceptMap's canonical identity rather than from its {@code name},
 * which is a label publishers change between releases:
 * <ul>
 *     <li>the base is the last segment of {@code url}, falling back to {@code name} and then
 *     {@code id} for a ConceptMap without one;</li>
 *     <li>a {@code version} is appended as {@code |version}, as in a FHIR canonical reference, so
 *     that several versions of one ConceptMap can be loaded side by side -
 *     {@code kdl-ihe-classcode|2024} next to {@code kdl-ihe-classcode|2025};</li>
 *     <li>a ConceptMap with a single group becomes one mapping named by the base;</li>
 *     <li>in a ConceptMap with several groups, every group is named by the base plus {@code #} and
 *     the last segment of its {@code target} - {@code kdl-ihe-classcode|2025#IHEXDSclassCode} and
 *     {@code kdl-ihe-classcode|2025#v3-NullFlavor}. No group is privileged: FHIR gives their order
 *     no meaning, and naming by target keeps the names stable when a release inserts or reorders
 *     groups. The base then names a {@link CompositeMapping composite} of all groups in
 *     declaration order, which answers like FHIR's {@code $translate} without a target system:
 *     with the first group declaring the code, and only if none does, with the first group whose
 *     {@code unmapped} answers. {@link org.openehealth.ipf.commons.map.Mappings#translate(String, String)}
 *     says which group's target system the answer belongs to.</li>
 * </ul>
 * A ConceptMap whose groups all carry the {@link ConceptMapExtensions#MAPPING_NAME} extension -
 * which is how {@link ConceptMapWriter} writes mappings - holds exactly the mappings its groups
 * name, and no composite is added: its author said what the mappings are called.
 * <p>
 * So a caller consuming a third-party ConceptMap needs to know neither which groups it has nor
 * which one holds a code: it asks the base. A caller wanting one target system specifically asks
 * that group. An {@code other-map} fallback refers to a ConceptMap by canonical and resolves to the
 * base the same way, so {@code http://x/ConceptMap/base|2025} delegates to {@code base|2025}, and
 * {@code http://x/ConceptMap/base} to the only version of {@code base} registered.
 * <p>
 * R4 only. If R5 support lands it follows IPF's existing pattern - a parallel module whose loaders
 * claim {@code *.conceptmap.r5.json} and the format id {@code conceptmap-r5-json} - and the only
 * semantic difference to absorb is the rename of {@code target.equivalence} to
 * {@code target.relationship}, which collapses onto the same {@link Equivalence}.
 *
 * @since 6.0
 */
public abstract class ConceptMapLoader implements MappingLoader {

    private static final Logger log = LoggerFactory.getLogger(ConceptMapLoader.class);

    /**
     * File extension for the JSON wire format. The FHIR version is part of it on purpose: a
     * ConceptMap is not version-independent, and a loader for another version has to be able to
     * live on the same classpath without either claiming the other's files.
     */
    public static final String JSON_EXTENSION = ".conceptmap.r4.json";

    /**
     * File extension for the XML wire format.
     *
     * @see #JSON_EXTENSION
     */
    public static final String XML_EXTENSION = ".conceptmap.r4.xml";

    /**
     * Format id of the JSON wire format, under which {@link ConceptMapJsonLoader} can be selected
     * explicitly - which is how a ConceptMap that does not come from a file named after IPF's
     * conventions, one fetched from a terminology server above all, is read.
     */
    public static final String JSON_FORMAT = "conceptmap-r4-json";

    /**
     * Format id of the XML wire format.
     *
     * @see #JSON_FORMAT
     */
    public static final String XML_FORMAT = "conceptmap-r4-xml";

    /**
     * Bytes of leading whitespace tolerated before the character that says JSON or XML.
     */
    private static final int SNIFF_LIMIT = 256;

    protected static final FhirContext CONTEXT = FhirContext.forR4();

    private final String format;
    private final String extension;
    private final char firstCharacter;

    /**
     * @param format         this loader's format id
     * @param extension      the file extension it claims
     * @param firstCharacter the first non-whitespace character of a document in its wire format
     */
    protected ConceptMapLoader(String format, String extension, char firstCharacter) {
        this.format = format;
        this.extension = extension;
        this.firstCharacter = firstCharacter;
    }

    /**
     * @return a parser for this loader's wire format
     */
    protected abstract IParser parser();

    @Override
    public String format() {
        return format;
    }

    @Override
    public boolean supports(URI source) {
        return MappingLoader.hasExtension(source, extension);
    }

    @Override
    public List<Mapping> load(InputStream in, URI source, MappingFunctionRegistry functions) throws IOException {
        return load(in, source, functions, warning -> log.warn("{}: {}", source, warning));
    }

    @Override
    public List<Mapping> load(InputStream in, URI source, MappingFunctionRegistry functions,
                              Consumer<String> warnings) throws IOException {
        var content = new BufferedInputStream(in);
        checkWireFormat(content, source);
        ConceptMap conceptMap;
        try {
            conceptMap = parser().parseResource(ConceptMap.class,
                    new InputStreamReader(content, StandardCharsets.UTF_8));
        } catch (DataFormatException e) {
            throw new MappingException(source, "Not a parsable ConceptMap: " + e.getMessage(), e);
        }
        if (!conceptMap.hasGroup()) {
            throw new MappingException(source, "ConceptMap declares no group, so it holds no mappings");
        }

        var base = baseName(conceptMap, source);
        var groups = conceptMap.getGroup();
        var mappings = new ArrayList<Mapping>(groups.size() + 1);
        var names = new HashSet<String>();
        for (var index = 0; index < groups.size(); index++) {
            var group = groups.get(index);
            var name = groupName(group, base, index, groups.size(), source);
            if (!names.add(name)) {
                throw new MappingException(source, "ConceptMap has two groups named '" + name + "'. Name"
                        + " them apart with the " + ConceptMapExtensions.MAPPING_NAME + " extension");
            }
            mappings.add(toMapping(group, name, source, functions, warnings));
        }
        var allNamed = groups.stream().allMatch(group -> ConceptMapExtensions.mappingName(group).isPresent());
        if (groups.size() > 1 && !allNamed) {
            if (names.contains(base)) {
                throw new MappingException(source, "A group of the ConceptMap is named '" + base + "' by the "
                        + ConceptMapExtensions.MAPPING_NAME + " extension, which is the name of the ConceptMap"
                        + " as a whole. Name the group differently");
            }
            mappings.add(composite(base, mappings));
        }
        return mappings;
    }

    /**
     * Checks that the content really is in the wire format this loader reads, by its first
     * non-whitespace character - {@code &#123;} for JSON, {@code <} for XML. A source named by
     * format id has no file name to be wrong about, so getting the id wrong is the likely
     * mistake; saying which one to use instead beats a parser error about an unexpected token.
     */
    private void checkWireFormat(BufferedInputStream content, URI source) throws IOException {
        content.mark(SNIFF_LIMIT);
        int character;
        var read = 0;
        do {
            character = content.read();
        } while (character != -1 && ++read < SNIFF_LIMIT && Character.isWhitespace(character));
        content.reset();
        if (character == firstCharacter) {
            return;
        }
        if (character == -1) {
            throw new MappingException(source, "Not a ConceptMap: the source is empty");
        }
        var actual = switch (character) {
            case '{' -> "JSON - read it as '" + JSON_FORMAT + "'";
            case '<' -> "XML - read it as '" + XML_FORMAT + "'";
            default -> "neither JSON nor XML";
        };
        throw new MappingException(source, "This source is read as '" + format + "', but its content"
                + " starts with '" + (char) character + "', so it is " + actual);
    }

    // ------------------------------------------------------------------ resource to model

    /**
     * @return the extension's name, the base for the only group, and otherwise the base qualified
     * by the last segment of the group's target
     */
    private static String groupName(ConceptMap.ConceptMapGroupComponent group, String base, int index,
                                    int groups, URI source) {
        var declared = ConceptMapExtensions.mappingName(group);
        if (declared.isPresent()) {
            return declared.get();
        }
        if (groups == 1) {
            return base;
        }
        if (!group.hasTarget()) {
            throw new MappingException(source, "Group " + index + " of the ConceptMap declares no target,"
                    + " so it cannot be named. Add a target, or the " + ConceptMapExtensions.MAPPING_NAME
                    + " extension");
        }
        return base + "#" + lastSegment(group.getTarget());
    }

    /**
     * @return a composite asking the groups in declaration order, translating between the systems
     * they share - or none, where they differ
     */
    private static CompositeMapping composite(String name, List<Mapping> groups) {
        var builder = CompositeMapping.builder(name)
                .keySystem(shared(groups.stream().map(Mapping::keySystem).toList()))
                .valueSystem(shared(groups.stream().map(Mapping::valueSystem).toList()));
        groups.forEach(group -> builder.part(group.name()));
        return builder.build();
    }

    private static String shared(List<String> systems) {
        return systems.stream().distinct().count() == 1 ? systems.get(0) : null;
    }

    private static Mapping toMapping(ConceptMap.ConceptMapGroupComponent group, String name, URI source,
                                     MappingFunctionRegistry functions, Consumer<String> warnings) {
        var builder = SimpleMapping.builder(name)
                .keySystem(group.hasSource() ? group.getSource() : null)
                .valueSystem(group.hasTarget() ? group.getTarget() : null)
                .reversible(ConceptMapExtensions.reversible(group).orElse(true))
                .override(ConceptMapExtensions.override(group));

        for (var element : group.getElement()) {
            if (!element.hasCode()) {
                throw new MappingException(source, "Mapping '" + name + "' has an element without a code");
            }
            entries(element, name, source, warnings).forEach(builder::entry);
        }

        builder.unmatched(group.hasUnmapped()
                ? unmatched(group.getUnmapped(), name, source, functions)
                : Unmatched.ABSENT);
        ConceptMapExtensions.reverseUnmapped(group).ifPresent(reverse ->
                builder.reverseUnmatched(reverseUnmatched(reverse, name, source, functions)));
        return builder.build();
    }

    /**
     * An element may list several targets, and FHIR's {@code $translate} would answer with all of
     * them, while the model answers a key with one value. The entry is the first target asserting
     * an equal or equivalent concept, else the first that translates at all; a disjoint target is
     * kept as the disjoint entry it is, and any other translating target is left out, with a
     * warning. A target that depends on other elements or produces further ones
     * ({@code dependsOn}, {@code product}) holds only under a condition the model cannot state, so
     * it is left out, with a warning, rather than turned into an unconditional translation.
     */
    private static List<Entry> entries(ConceptMap.SourceElementComponent element, String name, URI source,
                                       Consumer<String> warnings) {
        var code = element.getCode();
        var keyDisplay = element.hasDisplay() ? element.getDisplay() : null;
        var candidates = new ArrayList<Entry>();
        for (var target : element.getTarget()) {
            if (!target.hasCode()) {
                // equivalence "unmatched" and mode "no-map" say there is deliberately no target
                continue;
            }
            if (target.hasDependsOn() || target.hasProduct()) {
                warnings.accept(name + ": the target '" + target.getCode() + "' of element '" + code
                        + "' depends on other elements (dependsOn/product), which a mapping cannot"
                        + " express; it is left out");
                continue;
            }
            candidates.add(new Entry(code, target.getCode(), equivalence(target.getEquivalence(), name, code, source),
                    keyDisplay, target.hasDisplay() ? target.getDisplay() : null));
        }
        var chosen = candidates.stream().filter(Entry::isInvertible).findFirst()
                .or(() -> candidates.stream().filter(Entry::isTranslation).findFirst());
        if (chosen.isEmpty()) {
            // nothing translates, so whatever there is are disjoint entries
            return candidates;
        }
        var translation = chosen.get();
        var result = new ArrayList<Entry>();
        result.add(translation);
        for (var candidate : candidates) {
            if (candidate == translation) {
                continue;
            }
            if (!candidate.isTranslation()) {
                result.add(candidate);
            } else {
                warnings.accept(name + ": element '" + code + "' translates to both '" + translation.value()
                        + "' and '" + candidate.value() + "'; a mapping answers a key with one value, so only '"
                        + translation.value() + "' is kept");
            }
        }
        return result;
    }

    /**
     * R4 has a richer vocabulary than the model needs; the extra codes fold onto the ones the
     * model has.
     */
    private static Equivalence equivalence(Enumerations.ConceptMapEquivalence declared, String mapping,
                                           String code, URI source) {
        if (declared == null) {
            return Equivalence.EQUAL;
        }
        return switch (declared) {
            case EQUAL, NULL -> Equivalence.EQUAL;
            case EQUIVALENT -> Equivalence.EQUIVALENT;
            // R4 states the relation from the target's side, the model from the key's: "wider" and
            // "subsumes" say the target is the broader concept, so the key is the narrower one
            case WIDER, SUBSUMES -> Equivalence.NARROWER;
            // "narrower" and "specializes": the target is the narrower concept, the key the wider
            case NARROWER, SPECIALIZES -> Equivalence.WIDER;
            case RELATEDTO, INEXACT -> Equivalence.INEXACT;
            case DISJOINT -> Equivalence.DISJOINT;
            case UNMATCHED -> throw new MappingException(source, "Mapping '" + mapping + "': element '"
                    + code + "' declares equivalence 'unmatched' together with a target code, which"
                    + " contradicts itself");
        };
    }

    private static Unmatched unmatched(ConceptMap.ConceptMapGroupUnmappedComponent unmapped, String mapping,
                                       URI source, MappingFunctionRegistry functions) {
        if (ConceptMapExtensions.unmappedFail(unmapped)) {
            return Unmatched.FAIL;
        }
        var function = ConceptMapExtensions.unmappedFunction(unmapped);
        if (function.isPresent()) {
            return computed(function.get(), mapping, source, functions);
        }
        if (unmapped.getMode() == null) {
            return Unmatched.ABSENT;
        }
        return switch (unmapped.getMode()) {
            case PROVIDED -> Unmatched.IDENTITY;
            case FIXED -> {
                if (!unmapped.hasCode()) {
                    throw new MappingException(source, "Mapping '" + mapping + "': unmapped mode"
                            + " 'fixed' requires a code");
                }
                yield Unmatched.fixed(unmapped.getCode());
            }
            case OTHERMAP -> {
                if (!unmapped.hasUrl()) {
                    throw new MappingException(source, "Mapping '" + mapping + "': unmapped mode"
                            + " 'other-map' requires the url of the ConceptMap to delegate to");
                }
                // The url is the canonical of a ConceptMap, optionally with |version, and its last
                // segment is the base this loader names that ConceptMap as a whole by.
                yield Unmatched.delegate(lastSegment(unmapped.getUrl()));
            }
            case NULL -> Unmatched.ABSENT;
        };
    }

    /**
     * Reads the reverse fallback from its extension, whose nested extensions mirror the fields of
     * {@code group.unmapped}: {@code mode} plus {@code code} for a fixed answer, or
     * {@code function} for a computed one.
     */
    private static Unmatched reverseUnmatched(Extension extension, String mapping, URI source,
                                              MappingFunctionRegistry functions) {
        var function = ConceptMapExtensions.part(extension, ConceptMapExtensions.FUNCTION);
        if (function.isPresent()) {
            return computed(function.get(), mapping, source, functions);
        }
        var delegate = ConceptMapExtensions.part(extension, ConceptMapExtensions.DELEGATE);
        if (delegate.isPresent()) {
            return Unmatched.delegate(delegate.get());
        }
        var mode = ConceptMapExtensions.part(extension, ConceptMapExtensions.MODE)
                .orElseThrow(() -> new MappingException(source, "Mapping '" + mapping + "': the "
                        + ConceptMapExtensions.REVERSE_UNMAPPED + " extension requires a 'mode'"));
        return switch (mode) {
            case "provided" -> Unmatched.IDENTITY;
            case "fixed" -> Unmatched.fixed(ConceptMapExtensions.part(extension, ConceptMapExtensions.CODE)
                    .orElseThrow(() -> new MappingException(source, "Mapping '" + mapping + "': reverse"
                            + " unmapped mode 'fixed' requires a 'code'")));
            case "fail" -> Unmatched.FAIL;
            default -> throw new MappingException(source, "Mapping '" + mapping + "': unknown reverse"
                    + " unmapped mode '" + mode + "', expected provided, fixed or fail");
        };
    }

    private static Unmatched computed(String ref, String mapping, URI source,
                                      MappingFunctionRegistry functions) {
        if (!functions.contains(ref)) {
            throw new MappingException(source, "Mapping '" + mapping + "' refers to the mapping function"
                    + " '" + ref + "', which is not registered. Register it with"
                    + " Mappings.builder().function(..) before loading this file");
        }
        return Unmatched.computed(ref);
    }

    private static String lastSegment(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    /**
     * @return the canonical's last segment - else the name, else the id - qualified by
     * {@code |version} if the ConceptMap declares one
     */
    private static String baseName(ConceptMap conceptMap, URI source) {
        String base;
        if (conceptMap.hasUrl()) {
            base = lastSegment(conceptMap.getUrl());
        } else if (conceptMap.hasName()) {
            base = conceptMap.getName();
        } else if (conceptMap.hasIdElement() && conceptMap.getIdElement().hasIdPart()) {
            base = conceptMap.getIdElement().getIdPart();
        } else {
            throw new MappingException(source, "ConceptMap has neither url, name nor id, so its mappings"
                    + " cannot be named. Add one, or the " + ConceptMapExtensions.MAPPING_NAME
                    + " extension on the group");
        }
        return conceptMap.hasVersion() ? base + "|" + conceptMap.getVersion() : base;
    }
}
