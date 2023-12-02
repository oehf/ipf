/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import lombok.experimental.Delegate;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ClassificationType;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Encapsulation of {@link ClassificationType}.
 * @author Jens Riemschneider
 */
public class EbXMLClassification30 implements EbXMLClassification {
    private final ClassificationType classification;

    /**
     * Constructs a classification by wrapping the given ebXML 3.0 object.
     * @param classification
     *          the object to wrap.
     */
    public EbXMLClassification30(ClassificationType classification) {
        requireNonNull(classification, "classification cannot be null");
        classification.setObjectType(CLASSIFICATION_OBJECT_TYPE);
        this.classification = classification;
    }

    @Override
    public String getClassificationScheme() {
        return classification.getClassificationScheme();
    }

    @Override
    public String getClassifiedObject() {
        return classification.getClassifiedObject();
    }

    @Override
    public void setClassificationScheme(String classificationScheme) {
        classification.setClassificationScheme(classificationScheme);
    }

    @Override
    public void setClassifiedObject(String classifiedObject) {
        classification.setClassifiedObject(classifiedObject);
    }

    @Override
    public String getNodeRepresentation() {
        return classification.getNodeRepresentation();
    }

    @Override
    public void setNodeRepresentation(String nodeRepresentation) {
        classification.setNodeRepresentation(nodeRepresentation);
    }

    @Override
    public LocalizedString getName() {
        return getNameAsInternationalString().getSingleLocalizedString();
    }

    @Override
    public EbXMLInternationalString30 getNameAsInternationalString() {
        return new EbXMLInternationalString30(classification.getName());
    }

    @Override
    public void setName(LocalizedString name) {
        classification.setName(new EbXMLInternationalString30(name).getInternal());
    }

    @Override
    public void setClassificationNode(String classificationNode) {
        classification.setClassificationNode(classificationNode);
    }

    @Override
    public String getClassificationNode() {
        return classification.getClassificationNode();
    }

    /**
     * @return the ebXML 3.0 object being wrapped by this class.
     */
    @Override
    public ClassificationType getInternal() {
        return classification;
    }

    /**
     * Implements the {@link org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlotList} interface
     * by delegating the calls to a "proper" slot list.
     */
    @Delegate
    private EbXMLSlotList30 getSlotList() {
        return new EbXMLSlotList30(classification.getSlot());
    }

    @Override
    public void assignUniqueId() {
        classification.setId("urn:uuid:" + UUID.randomUUID());
    }
}
