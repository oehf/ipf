/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CE;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AttributeStatementType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AttributeType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.ObjectFactory;
import org.openehealth.ipf.commons.xml.XmlUtils;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertionUnmarshallingTest {

    private static final ObjectFactory SAML20_OBJECT_FACTORY = new ObjectFactory();

    @Test
    public void test1() throws Exception {
        InputStream inputStream = AssertionUnmarshallingTest.class.getClassLoader().getResourceAsStream("xua-assertion.xml");
        Unmarshaller unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
        JAXBElement<AssertionType> jaxbElement = (JAXBElement) unmarshaller.unmarshal(inputStream);
        AssertionType assertion = jaxbElement.getValue();
        List<AttributeType> attributes = assertion.getStatementOrAuthnStatementOrAuthzDecisionStatement().stream()
            .filter(statement -> statement instanceof AttributeStatementType)
            .map(statement -> ((AttributeStatementType) statement).getAttributeOrEncryptedAttribute())
            .flatMap(Collection::stream)
            .map(x -> (AttributeType) x)
            .toList();

        {
            AttributeType attr = attributes.get(0);
            assertEquals("urn:oasis:names:tc:xspa:1.0:subject:organization", attr.getName());
            assertEquals(2, attr.getAttributeValue().size());
            assertEquals("Org 1 AG", attr.getAttributeValue().get(0).getStringValue());
            assertEquals("Org 2 SA", attr.getAttributeValue().get(1).getStringValue());
        }
        {
            AttributeType attr = attributes.get(1);
            assertEquals("urn:oasis:names:tc:xspa:1.0:subject:organization-id", attr.getName());
            assertEquals(2, attr.getAttributeValue().size());
            assertEquals("urn:oid:1.3.6.1.4.1.21367.2017.2.6.19.100.2", attr.getAttributeValue().get(0).getStringValue());
            assertEquals("urn:oid:1.3.6.1.4.1.21367.2017.2.6.19.100.3", attr.getAttributeValue().get(1).getStringValue());
        }
        {
            AttributeType attr = attributes.get(2);
            assertEquals("urn:oasis:names:tc:xspa:1.0:subject:subject-id", attr.getName());
            assertEquals(1, attr.getAttributeValue().size());
            assertEquals("Rosa Sestak", attr.getAttributeValue().get(0).getStringValue());
        }
        {
            AttributeType attr = attributes.get(3);
            assertEquals("urn:oasis:names:tc:xacml:2.0:subject:role", attr.getName());
            assertEquals(1, attr.getAttributeValue().size());
            CE role = attr.getAttributeValue().get(0).getRole().getValue();
            assertEquals("HCP", role.getCode());
            assertEquals("2.16.756.5.30.1.127.3.10.6", role.getCodeSystem());
        }
        {
            AttributeType attr = attributes.get(4);
            assertEquals("urn:oasis:names:tc:xspa:1.0:subject:purposeofuse", attr.getName());
            assertEquals(1, attr.getAttributeValue().size());
            CE pou = attr.getAttributeValue().get(0).getPurposeOfUse().getValue();
            assertEquals("NORM", pou.getCode());
            assertEquals("2.16.756.5.30.1.127.3.10.5", pou.getCodeSystem());
        }
        {
            AttributeType attr = attributes.get(5);
            assertEquals("urn:oasis:names:tc:xacml:2.0:resource:resource-id", attr.getName());
            assertEquals(1, attr.getAttributeValue().size());
            assertEquals("761337610411353650^^^&2.16.756.5.30.1.127.3.10.3&ISO", attr.getAttributeValue().get(0).getStringValue());
        }
        {
            AttributeType attr = attributes.get(6);
            assertEquals("urn:ihe:iti:xca:2010:homeCommunityId", attr.getName());
            assertEquals(1, attr.getAttributeValue().size());
            assertEquals("urn:oid:1.3.6.1.4.1.21367.2017.2.6.19", attr.getAttributeValue().get(0).getStringValue());
        }

        String rendered = XmlUtils.renderJaxb(Xacml20Utils.JAXB_CONTEXT, SAML20_OBJECT_FACTORY.createAssertion(assertion), true);
        assertEquals(2, StringUtils.countMatches(rendered, "xsi:type=\"hl7:CE\""));
    }

}
