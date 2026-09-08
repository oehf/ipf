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

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.MappingLoaders;
import org.openehealth.ipf.commons.map.Mappings;
import org.openehealth.ipf.commons.map.Unmatched;

import java.net.URI;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasNoValue;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslateBack;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasEquivalence;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasReverseUnmatched;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasUnmatched;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.isReversible;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

public class ConceptMapLoaderTest {

    private static final String GENDER = "hl7v2fhir-patient-administrativeGender";
    private static final String MARITAL_STATUS = "hl7v2v3-patient-maritalStatus";
    private static final String SECOND_GROUP = "genders#1";
    private static final String SPECIALISING = "specialising";

    private static Mappings.Builder builder() {
        return Mappings.builder().function("oidUri", key -> key != null && !key.isEmpty()
                && Character.isDigit(key.charAt(0)) ? "urn:oid:" + key : key);
    }

    @Test
    public void loaderIsDiscoveredForBothWireFormats() {
        assertThat(MappingLoaders.forSource(URI.create("file:/x/a.conceptmap.r4.json")),
                hasValue(instanceOf(ConceptMapJsonLoader.class)));
        assertThat(MappingLoaders.forSource(URI.create("file:/x/a.conceptmap.r4.xml")),
                hasValue(instanceOf(ConceptMapXmlLoader.class)));
        assertThat(MappingLoaders.forSource(URI.create("jar:file:/y.jar!/a.conceptmap.r4.json")),
                hasValue(instanceOf(ConceptMapJsonLoader.class)));
        // a plain FHIR resource file is not necessarily a ConceptMap
        assertThat(MappingLoaders.forSource(URI.create("file:/x/a.json")), hasNoValue());
    }

    /**
     * A ConceptMap rarely arrives named the way IPF's extensions expect - it is fetched from a
     * terminology server, or shipped by an Implementation Guide under its own name. Naming the
     * format is what makes it readable anyway, and there is one format id per wire format because
     * a name that says nothing leaves no other way to know which encoding to expect.
     */
    @Test
    public void readsASourceWhoseNameSaysNothingWhenTheFormatIsNamed() {
        var json = builder()
                .load("classpath:/extensionless-conceptmap", ConceptMapLoader.JSON_FORMAT)
                .build();
        assertThat(json, translates(GENDER, "M").to("male"));

        // a file whose '.xml' says nothing about which XML format it is in
        assertThat(MappingLoaders.forSource(URI.create("file:/x/gender-conceptmap.xml")), hasNoValue());
        var xml = builder()
                .load("classpath:/gender-conceptmap.xml", ConceptMapLoader.XML_FORMAT)
                .build();
        assertThat(xml, translates(MARITAL_STATUS, "D").to("D"));
    }

    /**
     * Getting the wire format wrong is the likely mistake once the file name no longer decides it,
     * so the content is checked against the format that was named and the answer says which id to
     * use instead.
     */
    @Test
    public void contentInTheOtherWireFormatNamesTheFormatIdToUse() {
        var builder = builder();
        var e = assertThrows(MappingException.class,
                () -> builder.load("classpath:/gender-conceptmap.xml", ConceptMapLoader.JSON_FORMAT));

        assertThat(e.getMessage(), allOf(
                containsString("read as 'conceptmap-r4-json'"),
                containsString("read it as 'conceptmap-r4-xml'")));
    }

    @Test
    public void contentThatIsNeitherJsonNorXmlIsRejected() {
        var builder = builder();
        var e = assertThrows(MappingException.class,
                () -> builder.load("classpath:/not-a-conceptmap", ConceptMapLoader.JSON_FORMAT));

        assertThat(e.getMessage(), containsString("neither JSON nor XML"));
    }

    /**
     * ConceptMap already carries the equivalence that the mapping model needs and the Groovy DSL
     * lacked, so the reverse direction can be built from the resource as authored.
     */
    @Test
    public void readsGroupElementsAndEquivalences() {
        var mappings = builder().load("classpath:/example.conceptmap.r4.json").build();
        var mapping = mappings.mapping(GENDER).orElseThrow();

        assertThat(mapping.keySystem(), is("http://terminology.hl7.org/CodeSystem/v2-0001"));
        assertThat(mapping.valueSystem(), is("http://hl7.org/fhir/administrative-gender"));
        assertThat(mappings.keys(GENDER),
                contains("M", "F", "A", "O", "U"));
        assertThat(mapping.entries().get(2), hasEquivalence(Equivalence.NARROWER));

        assertThat(mappings, translates(GENDER, "M").to("male"));
        assertThat(mappings, translates(GENDER, "A").to("other"));
        assertThat(mappings, translatesBack(GENDER, "other").to("O"));
    }

    /**
     * An element whose only target declares equivalence "unmatched" says there is deliberately no
     * mapping for that code, so it becomes no entry rather than a broken one.
     */
    @Test
    public void elementWithoutACodedTargetIsSkipped() {
        var mappings = builder().load("classpath:/example.conceptmap.r4.json").build();
        assertThat(mappings.keys(GENDER), not(contains("X")));
        assertThat(mappings, translates(GENDER, "X").to("other"));
    }

    @Test
    public void readsUnmappedAndTheReverseUnmappedExtension() {
        var mappings = builder().load("classpath:/example.conceptmap.r4.json").build();
        var mapping = mappings.mapping(GENDER).orElseThrow();

        assertThat(mapping, hasUnmatched(Unmatched.fixed("other")));
        assertThat(mapping, hasReverseUnmatched(Unmatched.fixed("O")));
        assertThat(mappings, translatesBack(GENDER, "ZZZ").to("O"));
    }

    /**
     * The XML wire format, several groups in one resource, the mapping-name and reversible
     * extensions, and a computed fallback - all of it through HAPI.
     */
    @Test
    public void readsTheXmlWireFormatAndMultipleGroups() {
        var mappings = builder().load("classpath:/multi-group.conceptmap.r4.xml").build();

        assertThat(mappings.mappingNames(),
                containsInAnyOrder(MARITAL_STATUS, SECOND_GROUP));

        var named = mappings.mapping(MARITAL_STATUS).orElseThrow();
        assertThat(named, hasUnmatched(Unmatched.computed("oidUri")));
        assertThat(mappings, translates(MARITAL_STATUS, "D").to("D"));
        assertThat(mappings, translates(MARITAL_STATUS, "1.2.3").to("urn:oid:1.2.3"));

        // reversible=false, so two keys may share a value and there is no reverse index
        var second = mappings.mapping(SECOND_GROUP).orElseThrow();
        assertThat(second, isReversible(false));
        assertThat(mappings, translates(SECOND_GROUP, "L").to("home"));
        assertThat(mappings, doesNotTranslateBack(SECOND_GROUP, "home"));
    }

    @Test
    public void functionModeRequiresARegisteredFunction() {
        var builder = Mappings.builder();
        var e = assertThrows(MappingException.class,
                () -> builder.load("classpath:/multi-group.conceptmap.r4.xml"));
        assertThat(e.getMessage(), containsString("'oidUri', which is not registered"));
    }

    /**
     * {@code unmapped.mode = other-map} is FHIR's way of saying "another concept map answers the
     * rest", which is the model's delegating fallback. The mapping it names is the last segment of
     * the url, the same way this loader names a resource that carries no name of its own.
     */
    @Test
    public void otherMapBecomesADelegatingFallback() {
        var mappings = Mappings.builder()
                .mapping(org.openehealth.ipf.commons.map.Mapping.builder("publishableBase")
                        .entry("M", "male")
                        .unmatched(Unmatched.fixed("unknown"))
                        .build())
                .load("classpath:/other-map.conceptmap.r4.json")
                .build();

        assertThat(mappings.mapping(SPECIALISING).orElseThrow(), hasUnmatched(Unmatched.delegate("publishableBase")));
        assertThat(mappings, translates(SPECIALISING, "CUSTOM").to("other"));
        assertThat(mappings, translates(SPECIALISING, "M").to("male"));
        // the delegate's own fallback decides the outcome
        assertThat(mappings, translates(SPECIALISING, "anything").to("unknown"));
    }

    /**
     * The mapping a resource delegates to has to be loaded first, which is what turns a dangling
     * reference into a startup error rather than a surprise at runtime.
     */
    @Test
    public void aDanglingDelegationIsRejected() {
        var builder = Mappings.builder();
        var e = assertThrows(MappingException.class,
                () -> builder.load("classpath:/other-map.conceptmap.r4.json"));
        assertThat(e.getMessage(), containsString("delegates to 'publishableBase'"));
    }

    /**
     * A ConceptMap and an IPF XML mapping in one Mappings instance, which is the case the loader
     * exists for: a consumer governing part of their terminology in ConceptMap and keeping the
     * rest as plain tables.
     */
    @Test
    public void formatsMixInOneMappingsInstance() {
        var mappings = builder()
                .load("classpath:/example.conceptmap.r4.json")
                .load("classpath:/META-INF/map/hl7-v2-v3-translation.mapping.xml")
                .build();
        assertThat(mappings, translates(GENDER, "M").to("male"));
        assertThat(mappings, translates("hl7v2v3-interactionId-triggerEvent", "PRPA_IN201301UV02").to("A01"));
    }
}
