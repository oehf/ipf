/*
 * Copyright 2012 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.AUTHOR_INCOMPLETE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * @author Dmytro Rud
 */
public class AuthorClassificationValidation extends ClassificationValidation {
    private static final Set<String> SLOT_NAMES;
    static {
        SLOT_NAMES = new HashSet<String>();
        SLOT_NAMES.add(Vocabulary.SLOT_NAME_AUTHOR_PERSON);
        SLOT_NAMES.add(Vocabulary.SLOT_NAME_AUTHOR_TELECOM);
        SLOT_NAMES.add(Vocabulary.SLOT_NAME_AUTHOR_INSTITUTION);
    }

    public AuthorClassificationValidation(String classScheme, SlotValueValidation[] slotValidations) {
        super(classScheme, 0, Integer.MAX_VALUE, Vocabulary.DisplayNameUsage.OPTIONAL, slotValidations);
    }

    @Override
    public void validate(EbXMLRegistryObject obj) throws XDSMetaDataException {
        super.validate(obj);

        List<EbXMLClassification> classifications = obj.getClassifications(classScheme);
        for (EbXMLClassification classification : classifications) {
            // check whether at least an authorPerson, authorTelecommunication,
            // or authorInstitution sub-attribute is present
            boolean authorComplete = false;
            for (EbXMLSlot slot : classification.getSlots()) {
                if (SLOT_NAMES.contains(slot.getName())) {
                    authorComplete = true;
                    break;
                }
            }
            metaDataAssert(authorComplete, AUTHOR_INCOMPLETE, classScheme);
        }

    }
}
