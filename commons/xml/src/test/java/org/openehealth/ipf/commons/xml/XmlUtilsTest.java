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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openehealth.ipf.commons.xml.svrl.SchematronOutput;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XmlUtilsTest {

    private static final String SVRL_NS = "http://purl.oclc.org/dsdl/svrl";

    private static JAXBContext jaxbContext;

    @BeforeAll
    public static void setUp() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(SchematronOutput.class);
    }

    @Test
    public void testMarshalAndUnmarshal() throws Exception {
        var output = new SchematronOutput();
        output.setTitle("Test");

        var xml = XmlUtils.marshal(jaxbContext, output, false);
        assertFalse(xml.startsWith("<?xml"));

        var parsed = XmlUtils.unmarshal(jaxbContext, xml, SchematronOutput.class);
        assertEquals("Test", parsed.getTitle());
    }

    @Test
    public void testUnmarshalRejectsDoctypeWithInternalEntities() {
        var xml = "<!DOCTYPE schematron-output [<!ENTITY a \"aaaaaaaaaa\"><!ENTITY b \"&a;&a;&a;&a;&a;\">]>" +
                "<schematron-output xmlns=\"" + SVRL_NS + "\" title=\"&b;\"/>";
        assertDoctypeRejected(xml);
    }

    @Test
    public void testUnmarshalRejectsExternalEntity(@TempDir Path tempDir) throws Exception {
        var secret = tempDir.resolve("secret.txt");
        Files.writeString(secret, "SECRET", StandardCharsets.UTF_8);
        var xml = "<!DOCTYPE schematron-output [<!ENTITY xxe SYSTEM \"" + secret.toUri() + "\">]>" +
                "<schematron-output xmlns=\"" + SVRL_NS + "\"><text>&xxe;</text></schematron-output>";
        assertDoctypeRejected(xml);
    }

    @Test
    public void testMarshalNode() throws Exception {
        var document = DomBuildersPool.use(builder -> builder.newDocument());
        document.appendChild(document.createElementNS(SVRL_NS, "schematron-output"));

        var xml = new String(XmlUtils.marshal(document), StandardCharsets.UTF_8);
        assertTrue(xml.contains("schematron-output"));
        assertFalse(xml.startsWith("<?xml"));
    }

    private static void assertDoctypeRejected(String xml) {
        var exception = assertThrows(JAXBException.class, () -> XmlUtils.unmarshal(jaxbContext, xml));
        var cause = exception.getLinkedException() != null ? exception.getLinkedException() : exception.getCause();
        assertTrue(String.valueOf(cause).contains("DOCTYPE"), () -> "Unexpected cause: " + cause);
    }
}
