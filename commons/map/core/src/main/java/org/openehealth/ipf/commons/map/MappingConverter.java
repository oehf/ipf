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
package org.openehealth.ipf.commons.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Converts a mapping source from any format a {@link MappingLoader} on the classpath can read
 * into any format a {@link MappingWriter} on the classpath can produce. In practice that means
 * migrating a legacy Groovy {@code .map} script to {@code *.mapping.xml} or
 * {@code *.mapping.yaml}.
 * <p>
 * The conversion is a starting point, not a finished file, and it says so: every judgement it had
 * to make comes back as a warning naming the mapping.
 * <ul>
 *     <li><b>Fallback closures.</b> A closure has no declarative equivalent, so the converter
 *     probes it: one that returns its argument for every probe becomes
 *     {@link Unmatched.Identity}, one that returns the same constant for every probe becomes
 *     {@link Unmatched.Fixed}, and anything else becomes an {@link Unmatched.Computed} reference
 *     to a function named {@code TODO-<mapping>}, with a warning. Across all mapping files
 *     shipped with IPF that last case occurs once. The probe is a heuristic - a closure that
 *     behaves like the identity on the probes but not on some other input would be simplified
 *     wrongly - which is the reason the output is meant to be read before it is committed.</li>
 *     <li><b>Reverse collisions.</b> Where several keys map onto one value the Groovy service
 *     silently made the last one the inverse. The converter reproduces that, marking the others
 *     {@code narrower}, and warns so that the canonical inverse can be chosen deliberately.</li>
 *     <li><b>Composite values.</b> The {@code ~} convention of the Groovy DSL is gone. Values
 *     containing {@code ~} are written verbatim; the loader reports each such mapping, which
 *     wants splitting by hand.</li>
 *     <li><b>Typed mappings.</b> A script may map objects - enum constants, numbers - where the
 *     model holds strings, so their string form is all that is converted. The loader reports each
 *     such mapping; it cannot be expressed in any of the formats and wants rewriting in Java,
 *     because the calling code relied on getting the objects back.</li>
 *     <li><b>Comments.</b> A {@code .map} file is evaluated, not parsed, so its comments do not
 *     survive into the loaded model. Several of them record the HL7 table a code came from and
 *     are worth copying over by hand.</li>
 * </ul>
 *
 * @since 6.0
 */
public class MappingConverter {

    private static final Logger log = LoggerFactory.getLogger(MappingConverter.class);

    /**
     * Inputs the converter feeds to a fallback closure to work out what it does. Chosen to
     * separate the three cases that matter: {@code ""} and non-numeric codes for identity and
     * constants, digit-leading strings for anything that treats OIDs specially.
     */
    private static final List<String> PROBES =
            List.of("", "A", "Z", "code", "0", "1", "1.2.840.10008", "UNK", "9-x");

    /**
     * The outcome of reading one source document.
     *
     * @param source   where the mappings were read from
     * @param mappings the mappings, normalized so that they can be expressed declaratively
     * @param warnings everything a human should look at before committing the result
     */
    public record Conversion(URI source, List<Mapping> mappings, List<String> warnings) {
    }

    // ------------------------------------------------------------------ API

    /**
     * Reads a mapping source and normalizes it so that it can be expressed declaratively,
     * without writing anything.
     */
    public Conversion read(URL source) {
        return read(source, null);
    }

    /**
     * Reads a mapping source whose format is named explicitly rather than left to its file
     * extension.
     *
     * @param sourceFormat a {@link MappingLoader#format() format id}, or {@code null} to
     *                     dispatch by file extension
     */
    public Conversion read(URL source, String sourceFormat) {
        var uri = toUri(source);
        var functions = new MappingFunctionRegistry();
        var loader = MappingLoaders.resolve(uri, sourceFormat);
        var warnings = new ArrayList<String>();
        List<Mapping> loaded;
        try (var in = source.openStream()) {
            loaded = loader.load(in, uri, functions, warnings::add);
        } catch (IOException e) {
            throw new MappingException(uri, "Could not read mapping source", e);
        }

        var converted = loaded.stream()
                .map(mapping -> convert(mapping, functions, warnings))
                .toList();
        if (!converted.isEmpty()) {
            warnings.add("comments of the source document are not carried over; several of them record"
                    + " the code system a code came from and are worth copying by hand");
        }
        warnings.forEach(warning -> log.warn("{}: {}", uri, warning));
        return new Conversion(uri, converted, List.copyOf(warnings));
    }

    /**
     * Renders mappings in a target format, named either by its
     * {@link MappingWriter#format() format id} or by the file extension it produces.
     *
     * @param mappings the mappings to render
     * @param target   {@code xml} or {@code .mapping.xml} - both select the same writer
     * @throws IllegalArgumentException if no writer on the classpath produces that format
     */
    public String render(List<Mapping> mappings, String target) {
        var writer = MappingWriters.forTarget(target)
                .orElseThrow(() -> new IllegalArgumentException("No mapping writer produces '"
                        + target + "'. Add the module for that format to the classpath;"
                        + " available formats: " + MappingWriters.formats()
                        + ", written as " + MappingWriters.extensions()));
        var out = new StringWriter();
        writer.write(mappings, out);
        return out.toString();
    }

    /**
     * Converts one source file and writes the result next to it, or into
     * {@code targetDirectory} if one is given.
     *
     * @param source          the mapping source to read
     * @param targetDirectory where to write, or {@code null} for next to the source
     * @param targetFormat    the target format, named by its {@link MappingWriter#format() id} or
     *                        by the file extension it produces
     * @return the conversion, whose warnings are worth reading; the written file is the source's
     * name up to its first dot, plus the target format's extension
     */
    public Conversion convert(Path source, Path targetDirectory, String targetFormat) {
        return convert(source, null, targetDirectory, targetFormat);
    }

    /**
     * Converts one source file, reading it in a format named explicitly.
     *
     * @param sourceFormat a {@link MappingLoader#format() format id}, or {@code null} to
     *                     dispatch by file extension
     * @see #convert(Path, Path, String)
     */
    public Conversion convert(Path source, String sourceFormat, Path targetDirectory, String targetFormat) {
        try {
            var conversion = read(source.toUri().toURL(), sourceFormat);
            var content = render(conversion.mappings(), targetFormat);
            var extension = MappingWriters.forTarget(targetFormat).orElseThrow().extension();
            var directory = targetDirectory != null ? targetDirectory : source.toAbsolutePath().getParent();
            Files.createDirectories(directory);
            var target = directory.resolve(baseName(source) + extension);
            Files.writeString(target, content, StandardCharsets.UTF_8);
            log.info("Converted {} to {}", source, target);
            return conversion;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not convert " + source, e);
        }
    }

    // ------------------------------------------------------------------ conversion

    private Mapping convert(Mapping mapping, MappingFunctionRegistry functions, List<String> warnings) {
        var builder = Mapping.builder(mapping.name())
                .keySystem(mapping.keySystem())
                .valueSystem(mapping.valueSystem())
                .reversible(mapping.reversible())
                .override(mapping.override())
                .unmatched(simplify(mapping.unmatched(), mapping, functions, warnings,
                        keys(mapping), "unmatched"))
                .reverseUnmatched(simplify(mapping.reverseUnmatched(), mapping, functions, warnings,
                        values(mapping), "reverse unmatched"));
        mapping.entries().forEach(builder::entry);
        warnAboutResolvedCollisions(mapping, warnings);
        return builder.build();
    }

    /**
     * Replaces a fallback closure with the declarative mode it turns out to implement.
     *
     * @param probes inputs the fallback is actually reachable with, i.e. the mapping's own keys
     *               for the forward direction and its values for the reverse one
     */
    private Unmatched simplify(Unmatched unmatched, Mapping mapping, MappingFunctionRegistry functions,
                               List<String> warnings, List<String> probes, String direction) {
        if (!(unmatched instanceof Unmatched.Computed computed)) {
            return unmatched;
        }
        var function = functions.lookup(computed.ref()).orElse(null);
        if (function == null) {
            // A reference the source declared by name rather than a closure the loader wrapped:
            // it is already declarative, so pass it through untouched.
            return unmatched;
        }

        var inputs = new ArrayList<>(PROBES);
        probes.stream().filter(Objects::nonNull).filter(probe -> !inputs.contains(probe)).forEach(inputs::add);

        var results = new ArrayList<String>(inputs.size());
        for (var input : inputs) {
            try {
                results.add(function.apply(input));
            } catch (RuntimeException e) {
                return unresolved(mapping, computed, warnings, direction,
                        "it fails for the input '" + input + "' (" + e + ")");
            }
        }

        if (isIdentity(inputs, results)) {
            return Unmatched.IDENTITY;
        }
        return constantOf(results).orElseGet(() -> unresolved(mapping, computed, warnings, direction,
                "it computes its result from the input"));
    }

    private static boolean isIdentity(List<String> inputs, List<String> results) {
        for (var i = 0; i < inputs.size(); i++) {
            if (!Objects.equals(inputs.get(i), results.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the declarative fallback if every probe produced the same answer - a fallback that
     * always answers nothing is the absent one - and empty if the answers differ
     */
    private static Optional<Unmatched> constantOf(List<String> results) {
        var first = results.get(0);
        if (!results.stream().allMatch(result -> Objects.equals(result, first))) {
            return Optional.empty();
        }
        return Optional.of(first == null ? Unmatched.ABSENT : Unmatched.fixed(first));
    }

    private Unmatched unresolved(Mapping mapping, Unmatched.Computed computed, List<String> warnings,
                                 String direction, String reason) {
        // Refs the Groovy loader generated for an inline closure are qualified with '#'; a name the
        // source chose itself is kept, because it already refers to a function someone can register.
        var ref = computed.ref().contains("#") ? "TODO-" + mapping.name() : computed.ref();
        warnings.add(mapping.name() + ": the " + direction + " fallback could not be expressed"
                + " declaratively because " + reason + ". It is written as a reference to the"
                + " function '" + ref + "'; implement that function in Java and register it with"
                + " Mappings.builder().function(\"" + ref + "\", ..)");
        return Unmatched.computed(ref);
    }

    /**
     * Reports where a value is reached by several keys and only one of them is its inverse. A
     * format that can state an {@link Equivalence} says which one on purpose; the legacy DSL
     * cannot, so its loader picks the last declaration - the one the Groovy mapping service
     * happened to answer with - and this is where that choice becomes visible.
     */
    private void warnAboutResolvedCollisions(Mapping mapping, List<String> warnings) {
        if (!mapping.reversible()) {
            return;
        }
        var keysByValue = new LinkedHashMap<String, List<String>>();
        var inverseByValue = new LinkedHashMap<String, String>();
        mapping.entries().forEach(entry -> {
            keysByValue.computeIfAbsent(entry.value(), value -> new ArrayList<>()).add(entry.key());
            if (entry.isInvertible()) {
                inverseByValue.put(entry.value(), entry.key());
            }
        });

        keysByValue.forEach((value, keys) -> {
            var inverse = inverseByValue.get(value);
            if (keys.size() > 1 && inverse != null) {
                warnings.add(mapping.name() + ": the keys " + keys + " all map to '" + value + "'"
                        + " and '" + inverse + "' is its inverse, the others being narrower - which is"
                        + " what the Groovy service answered. Confirm that this is the intended"
                        + " inverse");
            }
        });
    }

    private static List<String> keys(Mapping mapping) {
        return mapping.entries().stream().map(Entry::key).toList();
    }

    private static List<String> values(Mapping mapping) {
        return mapping.entries().stream().map(Entry::value).toList();
    }

    /**
     * @return the file name without its extension. Mapping formats use compound extensions -
     * {@code .mapping.xml}, {@code .conceptmap.r4.json} - so everything from the first dot is
     * dropped, which also means a source called {@code gender.v2.map} converts to
     * {@code gender.mapping.xml}
     */
    private static String baseName(Path source) {
        var name = source.getFileName().toString();
        var dot = name.indexOf('.');
        return dot <= 0 ? name : name.substring(0, dot);
    }

    private static URI toUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            return URI.create(url.toString().replace(" ", "%20"));
        }
    }

    // ------------------------------------------------------------------ command line

    /**
     * Converts mapping files from the command line, for the one-off migration of a source tree.
     * <pre>
     * java -cp ... org.openehealth.ipf.commons.map.MappingConverter [-f &lt;format&gt;] [--from &lt;format&gt;] &lt;output-dir|-&gt; &lt;file-or-dir&gt;...
     * </pre>
     * {@code -f} names the target format, by its id ({@code xml}, {@code yaml}) or by the file
     * extension it produces, and defaults to {@code .mapping.xml}; {@code --from} names the format
     * the sources are read in, for files whose extension does not identify one, and defaults to
     * dispatching per file by extension; {@code -} as the output directory writes each result next
     * to its source. A directory argument is searched for anything a loader on the classpath can
     * read - or, with {@code --from}, for every file in it - so which formats can be converted
     * from and to is decided by which modules are on the classpath: {@code ipf-commons-map-groovy}
     * to read {@code .map}, {@code ipf-commons-map-xml} and {@code ipf-commons-map-yaml} to write.
     */
    public static void main(String[] args) throws IOException {
        var arguments = new ArrayList<>(List.of(args));
        var targetFormat = option(arguments, "-f", "--format", ".mapping.xml");
        var sourceFormat = option(arguments, null, "--from", null);
        if (arguments.size() < 2) {
            System.err.println("Usage: MappingConverter [-f <format>] [--from <format>] <output-dir|-> <file-or-dir>...");
            System.err.println("  formats that can be read here:    " + MappingLoaders.formats());
            System.err.println("  formats that can be written here: " + MappingWriters.formats()
                    + ", written as " + MappingWriters.extensions());
            System.exit(2);
        }

        var target = "-".equals(arguments.get(0)) ? null : Path.of(arguments.get(0));
        var converter = new MappingConverter();
        var sources = new ArrayList<Path>();
        for (var argument : arguments.subList(1, arguments.size())) {
            var path = Path.of(argument);
            if (Files.isDirectory(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    // Without a format given, a directory yields whatever a loader claims; with
                    // one, every file in it is meant, since the names are then beside the point.
                    walk.filter(Files::isRegularFile)
                            .filter(candidate -> sourceFormat != null
                                    || MappingLoaders.forSource(candidate.toUri()).isPresent())
                            .forEach(sources::add);
                }
            } else {
                sources.add(path);
            }
        }

        var warnings = 0;
        for (var source : sources) {
            var conversion = converter.convert(source, sourceFormat, target, targetFormat);
            warnings += conversion.warnings().size();
            conversion.warnings().forEach(warning -> System.err.println(source + ": " + warning));
        }
        System.out.printf("Converted %d file(s) to %s with %d warning(s)%n",
                sources.size(), targetFormat, warnings);
    }

    /**
     * Removes an option and its value from the arguments.
     *
     * @return the value given, or {@code fallback} if the option is absent
     */
    private static String option(List<String> arguments, String shortName, String longName, String fallback) {
        var at = shortName == null ? -1 : arguments.indexOf(shortName);
        if (at < 0) {
            at = arguments.indexOf(longName);
        }
        if (at < 0 || at + 1 >= arguments.size()) {
            return fallback;
        }
        var value = arguments.remove(at + 1);
        arguments.remove(at);
        return value;
    }
}
