/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xua;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.opensaml.saml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml.saml2.core.impl.AttributeStatementUnmarshaller;

import java.util.List;

/**
 * A dirty workaround to convert an "unknown XML object" contained in a
 * {@link SubjectConfirmation} into a proper {@link AttributeStatement} instance.
 *
 * @author Dmytro Rud
 */
public class AttributeStatementExtractor extends AttributeStatementUnmarshaller {

    public AttributeStatement extractAttributeStatement(SubjectConfirmation subjectConfirmation) {
        AttributeStatement attributeStatement = new AttributeStatementBuilder().buildObject();
        List<XMLObject> xmlObjectList = subjectConfirmation.getSubjectConfirmationData().getUnknownXMLObjects(AttributeStatement.DEFAULT_ELEMENT_NAME);
        for (XMLObject xmlObject : xmlObjectList) {
            for (XMLObject child : xmlObject.getOrderedChildren()) {
                try {
                    child.detach();
                    processChildElement(attributeStatement, child);
                } catch (UnmarshallingException e) {
                    // do nothing
                }
            }
        }
        return attributeStatement;
    }

}
