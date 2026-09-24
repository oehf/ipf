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
package org.openehealth.ipf.commons.map.xml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openehealth.ipf.commons.map.Entry;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.MappingConverter;
import org.openehealth.ipf.commons.map.Mappings;
import org.openehealth.ipf.commons.map.SimpleMapping;
import org.openehealth.ipf.commons.map.Unmatched;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasReverseUnmatched;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasUnmatched;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

public class MappingConverterTest {

    private static final String ATNA_CODING_SYSTEM = "atnaCodingSystem";
    private static final String BIDI_GENDER = "hl7v2v3-bidi-administrativeGender-administrativeGender";
    private static final String CONVERTED = "m1";
    private static final String DEVICE_TYPE = "deviceType";
    private static final String FHIR_MARITAL_STATUS = "hl7v2fhir-patient-maritalStatus";
    private static final String MARITAL_STATUS = "hl7v2v3-patient-maritalStatus";

    private static MappingConverter.Conversion result;
    private static String xml;

    @BeforeAll
    public static void convert() {
        var converter = new MappingConverter();
        result = converter.read(MappingConverterTest.class.getResource("/legacy.map"));
        xml = converter.render(result.mappings(), XmlMappingLoader.EXTENSION);
    }

    private static SimpleMapping mapping(String name) {
        return result.mappings().stream()
                .filter(mapping -> mapping.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("no mapping " + name));
    }

    private static Optional<String> warningAbout(String name) {
        return result.warnings().stream()
            .filter(warning -> warning.startsWith(name + ":"))
            .findFirst();
    }

    /**
     * {@code (ELSE) : { it }} is the identity, and says so declaratively.
     */
    @Test
    public void identityClosureBecomesModeProvided() {
        assertThat(mapping(MARITAL_STATUS), hasUnmatched(Unmatched.IDENTITY));
        assertThat(xml, containsString("<unmatched mode=\"provided\"/>"));
    }

    /**
     * {@code (ELSE) : { 'O' }} and {@code ({'UN'}) : (ELSE)} are constants. The second is the
     * closure-as-a-map-key idiom, which becomes four self-describing words.
     */
    @Test
    public void constantClosureBecomesModeFixed() {
        var mapping = mapping(BIDI_GENDER);
        assertThat(mapping, hasUnmatched(Unmatched.fixed("O")));
        assertThat(mapping, hasReverseUnmatched(Unmatched.fixed("UN")));
        assertThat(xml, containsString("<reverse>"));
    }

    /**
     * A constant that was already written as a constant stays one.
     */
    @Test
    public void constantValueIsCarriedOver() {
        assertThat(mapping(FHIR_MARITAL_STATUS), hasUnmatched(Unmatched.fixed("UNK")));
    }

    /**
     * The one genuinely computed fallback in the IPF tree cannot be expressed declaratively, so
     * it becomes a named function to implement, and the converter says which one.
     */
    @Test
    public void computedClosureBecomesATodoFunction() {
        assertThat(mapping(ATNA_CODING_SYSTEM), hasUnmatched(Unmatched.computed("TODO-atnaCodingSystem")));
        assertThat(xml, containsString("ref=\"TODO-atnaCodingSystem\""));

        assertThat(warningAbout(ATNA_CODING_SYSTEM).orElseThrow(), allOf(
                containsString("computes its result from the input"),
                containsString("TODO-atnaCodingSystem")));
    }

    @Test
    public void identityInBothDirectionsIsRecognized() {
        var mapping = mapping("hl7v2fhir-patient-citizenship");
        assertThat(mapping, hasUnmatched(Unmatched.IDENTITY));
        assertThat(mapping, hasReverseUnmatched(Unmatched.IDENTITY));
    }

    /**
     * Three keys mapping onto UNK is the collision of design note section 04. The converter keeps
     * what the Groovy service happened to do - last one wins - and says so.
     */
    @Test
    public void reverseCollisionsArePreservedAndFlagged() {
        var mapping = mapping(FHIR_MARITAL_STATUS);
        assertThat(mapping.entries().stream()
                .map(Entry::equivalence)
                .toList(),
                contains(Equivalence.WIDER, Equivalence.WIDER, Equivalence.EQUAL));

        assertThat(warningAbout(FHIR_MARITAL_STATUS).orElseThrow(), allOf(
                containsString("[O, T, U] all map to 'UNK'"),
                containsString("'U' is its inverse")));
    }

    @Test
    public void compositeValuesAreFlaggedRatherThanInterpreted() {
        var mapping = mapping("hl7v2v3-interactionId-eventStructure");
        assertThat(mapping.entries().get(0).value(), is("A01~ADT_A01"));

        assertThat(warningAbout("hl7v2v3-interactionId-eventStructure").orElseThrow(), allOf(
                containsString("composite separator"),
                containsString("split the mapping")));
    }

    @Test
    public void commentLossIsReported() {
        assertThat(result.warnings(), hasItem(containsString("comments")));
    }

    /**
     * The whole point: what comes out loads, and answers what the script answered.
     */
    @Test
    public void outputLoadsAndBehavesLikeTheSource(@TempDir Path directory) throws IOException {
        var target = directory.resolve("legacy.mapping.xml");
        Files.writeString(target, xml);

        var mappings = Mappings.builder()
                .function("TODO-atnaCodingSystem", key -> key != null && !key.isEmpty()
                        && Character.isDigit(key.charAt(0)) ? "urn:oid:" + key : key)
                .load(target.toUri())
                .build();

        assertThat(mappings, translates(BIDI_GENDER, "UN").to("O"));
        assertThat(mappings, translatesBack(BIDI_GENDER, "X").to("UN"));
        assertThat(mappings, translates(BIDI_GENDER, "X").to("O"));
        assertThat(mappings, translates(MARITAL_STATUS, "L").to("A"));
        assertThat(mappings, translates(MARITAL_STATUS, "X").to("X"));
        assertThat(mappings, translates(ATNA_CODING_SYSTEM, "1.2.3").to("urn:oid:1.2.3"));
        assertThat(mappings, translates(FHIR_MARITAL_STATUS, "anything").to("UNK"));
        // the collision resolved the way the Groovy service resolved it
        assertThat(mappings, translatesBack(FHIR_MARITAL_STATUS, "UNK").to("U"));
    }

    /**
     * The migration path for a genuinely dynamic mapping: a closure deriving the value from the
     * key cannot be expressed declaratively, so the converter turns it into a named function to
     * implement, and the converted file behaves like the script once that function is registered.
     */
    @Test
    public void dynamicFallbackBecomesAFunctionToImplement(@TempDir Path directory) throws IOException {
        var dynamic = new MappingConverter().read(
                MappingConverterTest.class.getResource("/dynamic.map"));

        assertThat(dynamic.mappings().get(0), hasUnmatched(Unmatched.computed("TODO-deviceType")));
        assertThat(dynamic.warnings(), hasItem(startsWith("deviceType:")));

        var target = directory.resolve("dynamic.mapping.xml");
        Files.writeString(target, new MappingConverter().render(dynamic.mappings(), XmlMappingLoader.EXTENSION));
        var mappings = Mappings.builder()
                .function("TODO-deviceType",
                        key -> key == null || key.length() <= 4 ? key : key.substring(0, 4))
                .load(target.toUri())
                .build();

        assertThat(mappings, translates(DEVICE_TYPE, "LEGACY").to("legacy-device"));
        assertThat(mappings, translates(DEVICE_TYPE, "ABCDEFGH").to("ABCD"));
    }

    /**
     * A typed mapping converts to its string form, which no format can turn back into the objects
     * the script declared, so the conversion says that the mapping needs rewriting in Java.
     */
    @Test
    public void typedMappingsAreFlaggedForRewriting() {
        var typed = new MappingConverter().read(MappingConverterTest.class.getResource("/typed.map"));

        assertThat(typed.warnings(), hasItem(allOf(startsWith("timeUnit:"),
                containsString("java.util.concurrent.TimeUnit"), containsString("Rewrite the mapping in Java"))));
        assertThat(typed.warnings(), hasItem(allOf(startsWith("religion:"), containsString("java.lang.Integer"))));
        assertThat(typed.warnings(), hasItem(allOf(startsWith("unitName:"),
                containsString("fallback computes values of type java.time.temporal.ChronoUnit"))));
        assertThat(typed.warnings().stream().anyMatch(warning -> warning.startsWith("plain:")), is(false));
    }

    @Test
    public void writesAFileNextToTheSource(@TempDir Path directory) throws IOException {
        var source = directory.resolve("some-mappings.map");
        Files.writeString(source, "mappings = {\n  m1('a1' : 'b1')\n}\n");

        var written = new MappingConverter().convert(source, null, XmlMappingLoader.EXTENSION);
        var target = directory.resolve("some-mappings.mapping.xml");

        assertThat(Files.exists(target), is(true));
        assertThat(Files.readString(target),
                is(new MappingConverter().render(written.mappings(), XmlMappingLoader.EXTENSION)));
        assertThat(Mappings.builder().load(target.toUri()).build().map(CONVERTED, "a1"), hasValue("b1"));
    }

    /**
     * The target format may be named by its id as well as by the extension it produces, which is
     * what {@code -f xml} on the command line relies on.
     */
    @Test
    public void targetFormatMayBeNamedByIdOrByExtension(@TempDir Path directory) throws IOException {
        var source = directory.resolve("some-mappings.map");
        Files.writeString(source, "mappings = {\n  m1('a1' : 'b1')\n}\n");

        var converter = new MappingConverter();
        var conversion = converter.read(source.toUri().toURL());

        assertThat(converter.render(conversion.mappings(), XmlMappingLoader.FORMAT),
                is(converter.render(conversion.mappings(), XmlMappingLoader.EXTENSION)));

        // and a conversion named by id still writes the extension of that format
        converter.convert(source, null, XmlMappingLoader.FORMAT);
        assertThat(Files.exists(directory.resolve("some-mappings.mapping.xml")), is(true));
    }

    /**
     * A source whose name does not identify its format is converted by naming the format it is
     * read in - here a Groovy script that is not called {@code .map}.
     */
    @Test
    public void sourceFormatMayBeNamedExplicitly(@TempDir Path directory) throws IOException {
        var source = directory.resolve("some-mappings.groovy");
        Files.writeString(source, "mappings = {\n  m1('a1' : 'b1')\n}\n");

        var conversion = new MappingConverter().convert(source, "groovy", directory,
                XmlMappingLoader.FORMAT);

        assertThat(conversion.mappings(), hasSize(1));
        var target = directory.resolve("some-mappings.mapping.xml");
        assertThat(Files.exists(target), is(true));
        assertThat(Mappings.builder().load(target.toUri()).build().map(CONVERTED, "a1"), hasValue("b1"));
    }

    /**
     * A source naming a function by reference already says what it means declaratively, so it is
     * converted as it is - neither rejected for a function the converter does not know, nor
     * probed and replaced by what the function happens to answer.
     */
    @Test
    public void aNamedFunctionIsConvertedAsItIs() {
        var conversion = new MappingConverter().read(MappingConverterTest.class.getResource("/dynamic.mapping.xml"));

        assertThat(conversion.mappings().get(0), hasUnmatched(Unmatched.computed("first4")));
    }

    /**
     * A Groovy script may map a null key, which no format can write: the converter leaves the
     * entry out and says so, and a writer handed one rejects it rather than producing a file its
     * own loader refuses.
     */
    @Test
    public void entriesWithoutKeyAreLeftOutRatherThanWrittenUnreadably(@TempDir Path directory) throws IOException {
        var source = directory.resolve("nulls.map");
        Files.writeString(source, "mappings = { m((null): 'x', 'a': 'b') }");

        var conversion = new MappingConverter().read(source.toUri().toURL());

        assertThat(conversion.mappings().get(0).entries(), contains(new Entry("a", "b")));
        assertThat(conversion.warnings(), hasItem(allOf(startsWith("m:"), containsString("has no key"))));

        var unwritable = SimpleMapping.builder("m").entry(null, "x").build();
        var e = assertThrows(IllegalArgumentException.class,
                () -> new XmlMappingWriter().toXml(List.of(unwritable)));
        assertThat(e.getMessage(), containsString("an entry without a key"));
        assertThrows(IllegalArgumentException.class, () -> Unmatched.fixed(null));
    }
}
