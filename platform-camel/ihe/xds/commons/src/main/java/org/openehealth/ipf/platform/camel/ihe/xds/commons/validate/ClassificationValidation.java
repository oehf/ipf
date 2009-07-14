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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.validate;

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidatorAssertions.metaDataAssert;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidationMessage.*;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLClassification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRegistryObject;

/**
 * Validates a classification.
 * @author Jens Riemschneider
 */
public class ClassificationValidation implements RegistryObjectValidator {
    private final String classScheme;
    private final int min;
    private final int max;
    private final SlotValueValidation[] slotValidations;

    /**
     * Constructs the validation.
     * @param classScheme
     *          the class scheme of the classification to check.
     * @param min
     *          the minimum number of classifications allowed for the given scheme.
     * @param max
     *          the maximum number of classifications allowed for the given scheme.
     * @param slotValidations
     *          validations to apply to the slots of the classification.
     */
    public ClassificationValidation(String classScheme, int min, int max, SlotValueValidation[] slotValidations) {
        this.classScheme = classScheme;
        this.min = min;
        this.max = max;
        this.slotValidations = slotValidations;
    }

    /**
     * Constructs the validation.
     * <p>
     * Expects that there is exactly one classification of the given scheme.
     * @param classScheme
     *          the class scheme of the classification to check.
     * @param slotValidations
     *          validations to apply to the slots of the classification.
     */
    public ClassificationValidation(String classScheme, SlotValueValidation[] slotValidations) {
        this.classScheme = classScheme;
        this.min = 1;
        this.max = 1;
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
            
            if (slotValidations != null) {
                for (SlotValueValidation slotValidation : slotValidations) {
                    slotValidation.validate(classification);
                }
            }
        }
    }
}
