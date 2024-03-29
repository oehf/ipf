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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.NODE_REPRESENTATION_MISSING;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.NODE_REPRESENTATION_PROHIBITED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.NO_CLASSIFICATION_NAME_OBJ;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.NO_CLASSIFIED_OBJ;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.WRONG_CLASSIFIED_OBJ;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.WRONG_NUMBER_OF_CLASSIFICATIONS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.NodeRepresentationUsage;

/**
 * Validates a classification.
 * @author Jens Riemschneider
 * @author Mitko Kolev
 */
public class ClassificationValidation implements RegistryObjectValidator {
    protected final String classScheme;
    private final int min;
    private final int max;
    private final DisplayNameUsage displayNameUsage;
    private final NodeRepresentationUsage nodeRepresentationUsage;
    private final SlotValueValidation[] slotValidations;

    /**
     * Constructs the validation for classifications with the given <code>classScheme</code>.
     *
     * @param classScheme
     *          the class scheme of the classification to check.
     * @param min
     *          the minimum number of classifications allowed for the given scheme.
     * @param max
     *          the maximum number of classifications allowed for the given scheme.
     * @param displayNameUsage
     *          the usage of the display name element.
     * @param slotValidations
     *          validations to apply to the slots of the classification.
     *
     * @see DisplayNameUsage
     */
    public ClassificationValidation(String classScheme, int min, int max, DisplayNameUsage displayNameUsage, SlotValueValidation[] slotValidations) {
        this(classScheme, min, max, displayNameUsage, NodeRepresentationUsage.REQUIRED, slotValidations);
    }

    /**
     * Constructs the validation for classifications with the given <code>classScheme</code>.
     *
     * @param classScheme
     *          the class scheme of the classification to check.
     * @param min
     *          the minimum number of classifications allowed for the given scheme.
     * @param max
     *          the maximum number of classifications allowed for the given scheme.
     * @param displayNameUsage
     *          the usage of the display name element.
     * @param nodeRepresentationUsage
     *          optionality of the attribute {@code nodeRepresentation}.
     * @param slotValidations
     *          validations to apply to the slots of the classification.
     *
     * @see DisplayNameUsage
     */
    public ClassificationValidation(String classScheme, int min, int max, DisplayNameUsage displayNameUsage,
                                    NodeRepresentationUsage nodeRepresentationUsage, SlotValueValidation[] slotValidations)
    {
        this.classScheme = requireNonNull(classScheme);
        this.min = min;
        this.max = max;
        this.displayNameUsage = requireNonNull(displayNameUsage);
        this.nodeRepresentationUsage = nodeRepresentationUsage;
        this.slotValidations = slotValidations;
    }

    @Override
    public void validate(EbXMLRegistryObject obj) throws XDSMetaDataException {
        var classifications = obj.getClassifications(classScheme);
        metaDataAssert(classifications.size() >= min && classifications.size() <= max,
                WRONG_NUMBER_OF_CLASSIFICATIONS, classScheme, min, max, classifications.size());
        for (var classification : classifications) {
            metaDataAssert(classification.getClassifiedObject() != null,
                    NO_CLASSIFIED_OBJ, classScheme);

            metaDataAssert(classification.getClassifiedObject().equals(obj.getId()),
                    WRONG_CLASSIFIED_OBJ, obj.getId(), classification.getClassifiedObject());

            switch (nodeRepresentationUsage) {
                case REQUIRED:
                    metaDataAssert(StringUtils.isNotEmpty(classification.getNodeRepresentation()), NODE_REPRESENTATION_MISSING, classScheme);
                    break;
                case PROHIBITED:
                    metaDataAssert(StringUtils.isEmpty(classification.getNodeRepresentation()), NODE_REPRESENTATION_PROHIBITED, classScheme);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported node representation optionality " + nodeRepresentationUsage);
            }

            switch(displayNameUsage){
                case OPTIONAL:
                    break;
                case REQUIRED:
                    assertDisplayNamePresent(classification, classScheme);
                    break;
                default :
                    throw new IllegalArgumentException("Unsupported display name usage " + displayNameUsage);
            }

            if (slotValidations != null) {
                for (var slotValidation : slotValidations) {
                    slotValidation.validate(classification);
                }
            }
        }
    }

    public static void assertDisplayNamePresent(EbXMLClassification classification, String classificationScheme) {
        metaDataAssert(classification.getName() != null, NO_CLASSIFICATION_NAME_OBJ,
                classificationScheme, classification.getClassifiedObject());
        metaDataAssert(classification.getName().getValue() != null, NO_CLASSIFICATION_NAME_OBJ,
                classificationScheme, classification.getClassifiedObject());
    }

}
