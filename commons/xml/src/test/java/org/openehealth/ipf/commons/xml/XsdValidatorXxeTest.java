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
package org.openehealth.ipf.commons.xml;

import org.junit.jupiter.api.Test;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the XXE countermeasures applied to XSD validation. Inbound messages reach
 * {@link XsdValidator} as an unparsed {@code StreamSource} (see {@code XmlUtils.source(String)}),
 * so the validator itself parses untrusted XML and must refuse to dereference external entities.
 */
class XsdValidatorXxeTest {

    private static final String SCHEMA = """
            <?xml version="1.0"?>
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
              <xs:element name="r" type="xs:string"/>
            </xs:schema>
            """;

    /** Validates with a hardened validator, mirroring XsdValidator.doValidate(). */
    private static String validate(String xml) throws Exception {
        var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        XmlSecurity.harden(factory);
        var schema = factory.newSchema(new StreamSource(new StringReader(SCHEMA)));
        var validator = schema.newValidator();
        XmlSecurity.harden(validator);
        try {
            validator.validate(new StreamSource(new StringReader(xml)));
            return null;
        } catch (Exception e) {
            return e.getMessage() == null ? e.toString() : e.getMessage();
        }
    }

    @Test
    void externalEntityIsNotResolved() throws Exception {
        var secret = Files.createTempFile("ipf-xxe-test", ".txt");
        try {
            Files.writeString(secret, "CANARY");
            var payload = """
                    <?xml version="1.0"?>
                    <!DOCTYPE r [<!ENTITY x SYSTEM "%s">]>
                    <r>&x;</r>
                    """.formatted(secret.toUri());

            var error = validate(payload);
            // Before hardening this validated cleanly, having substituted the file's contents.
            assertTrue(error != null && error.contains("access is not allowed"),
                    "external entity must be refused, but got: " + error);
        } finally {
            Files.deleteIfExists(secret);
        }
    }

    @Test
    void externalDtdIsNotFetched() throws Exception {
        var payload = """
                <?xml version="1.0"?>
                <!DOCTYPE r SYSTEM "file:///nonexistent-ipf-xxe-test.dtd">
                <r>x</r>
                """;

        var error = validate(payload);
        assertTrue(error != null && error.contains("access is not allowed"),
                "external DTD must be refused, but got: " + error);
        // A FileNotFoundException would mean the parser tried to dereference the URI.
        assertFalse(error.contains("FileNotFound"), "parser must not attempt the fetch: " + error);
    }

    @Test
    void ordinaryDocumentStillValidates() throws Exception {
        var error = validate("<?xml version=\"1.0\"?><r>plain content</r>");
        assertTrue(error == null, "hardening must not reject legitimate documents, but got: " + error);
    }
}
