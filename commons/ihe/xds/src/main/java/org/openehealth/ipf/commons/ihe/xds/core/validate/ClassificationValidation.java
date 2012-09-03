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

import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage;

import java.util.List;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

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
    private final SlotValueValidation[] slotValidations;

    
    /**
     * Constructs the validation with exactly one classification with the given <code>classScheme</code>
     *  no checks for name.
     * @param classScheme
     *          the class scheme of the classification to check.
     *          the maximum number of classifications allowed for the given scheme.
     * @param displayNameUsage
     *          the usage of the display name element.  
     * @param slotValidations
     *          validations to apply to the slots of the classification.
     * @see DisplayNameUsage
     */
    public ClassificationValidation(String classScheme, DisplayNameUsage displayNameUsage, SlotValueValidation[] slotValidations) {
        this (classScheme, 1, 1, displayNameUsage, slotValidations);
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
     * @param slotValidations
     *          validations to apply to the slots of the classification.
     *          
     * @see DisplayNameUsage
     */
    public ClassificationValidation(String classScheme, int min, int max, DisplayNameUsage displayNameUsage, SlotValueValidation[] slotValidations) {
        this.classScheme = Validate.notNull(classScheme);
        this.min = min;
        this.max = max;
        this.displayNameUsage = Validate.notNull(displayNameUsage);
        this.slotValidations = slotValidations;
    }

    @Override
    public void validate(EbXMLRegistryObject obj) throws XDSMetaDataException {
        List<EbXMLClassification> classifications = obj.getClassifications(classScheme);
        metaDataAssert(classifications.size() >= min && classifications.size() <= max,
                WRONG_NUMBER_OF_CLASSIFICATIONS, classScheme, min, max, classifications.size());
        for (EbXMLClassification classification : classifications) {
            metaDataAssert(classification.getClassifiedObject() != null, 
                    NO_CLASSIFIED_OBJ, classScheme);

            metaDataAssert(classification.getClassifiedObject().equals(obj.getId()), 
                    WRONG_CLASSIFIED_OBJ, obj.getId(), classification.getClassifiedObject());
            
            metaDataAssert(classification.getNodeRepresentation() != null,
                    WRONG_NODE_REPRESENTATION, classScheme);
            
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
                for (SlotValueValidation slotValidation : slotValidations) {
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
