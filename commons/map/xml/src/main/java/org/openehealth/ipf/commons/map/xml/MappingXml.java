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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

/**
 * JAXB binding for {@code *.mapping.xml} files, mirroring
 * {@code META-INF/ipf/mapping.xsd} one to one.
 * <p>
 * These types exist only between the parser and
 * {@link org.openehealth.ipf.commons.map.Mapping}; the schema, not this binding, is the
 * contract that mapping files are held to.
 *
 * @since 6.0
 */
final class MappingXml {

    static final String SCHEMA_RESOURCE = "/META-INF/ipf/mapping.xsd";
    static final String SCHEMA_LOCATION = "http://openehealth.org/schema/ipf-commons-map.xsd";

    private MappingXml() {
    }

    @XmlRootElement(name = "mappings")
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "Mappings")
    static class Mappings {

        @XmlElement(name = "mapping", required = true)
        List<Mapping> mappings = new ArrayList<>();
    }

    @XmlRootElement(name = "mapping")
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "Mapping", propOrder = {"entries", "unmatched", "reverse"})
    static class Mapping {

        @XmlAttribute(name = "name", required = true)
        String name;

        @XmlAttribute(name = "keySystem")
        String keySystem;

        @XmlAttribute(name = "valueSystem")
        String valueSystem;

        @XmlAttribute(name = "reversible")
        Boolean reversible;

        @XmlAttribute(name = "override")
        Boolean override;

        @XmlElement(name = "entry")
        List<Entry> entries = new ArrayList<>();

        @XmlElement(name = "unmatched")
        Unmatched unmatched;

        @XmlElement(name = "reverse")
        Reverse reverse;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "Entry")
    static class Entry {

        @XmlAttribute(name = "key", required = true)
        String key;

        @XmlAttribute(name = "value", required = true)
        String value;

        @XmlAttribute(name = "equivalence")
        XmlEquivalence equivalence;

        @XmlAttribute(name = "keyDisplay")
        String keyDisplay;

        @XmlAttribute(name = "valueDisplay")
        String valueDisplay;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "Unmatched")
    static class Unmatched {

        @XmlAttribute(name = "mode", required = true)
        XmlUnmatchedMode mode;

        @XmlAttribute(name = "value")
        String value;

        @XmlAttribute(name = "ref")
        String ref;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "Reverse")
    static class Reverse {

        @XmlElement(name = "unmatched", required = true)
        Unmatched unmatched;
    }

    @XmlEnum
    @XmlType(name = "Equivalence")
    enum XmlEquivalence {
        @XmlEnumValue("equal") EQUAL,
        @XmlEnumValue("equivalent") EQUIVALENT,
        @XmlEnumValue("wider") WIDER,
        @XmlEnumValue("narrower") NARROWER,
        @XmlEnumValue("inexact") INEXACT,
        @XmlEnumValue("disjoint") DISJOINT
    }

    @XmlEnum
    @XmlType(name = "UnmatchedMode")
    enum XmlUnmatchedMode {
        @XmlEnumValue("absent") ABSENT,
        @XmlEnumValue("provided") PROVIDED,
        @XmlEnumValue("fixed") FIXED,
        @XmlEnumValue("function") FUNCTION,
        @XmlEnumValue("delegate") DELEGATE,
        @XmlEnumValue("fail") FAIL
    }
}
