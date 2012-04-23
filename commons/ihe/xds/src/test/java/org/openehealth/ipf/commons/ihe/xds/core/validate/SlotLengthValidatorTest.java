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

import static org.junit.Assert.*;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectContainer;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;

/**
 * Validates slot lengths.
 * @author Jens Riemschneider
 */
public class SlotLengthValidatorTest {
    private static final EbXMLFactory21 factory21 = new EbXMLFactory21();
    private static final EbXMLFactory30 factory30 = new EbXMLFactory30();
    
    private static final String SLOT_VALUE_30 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456";
    private static final String SLOT_VALUE_21 = "1234567890123456789012345678901234567890123456789012345678901234";

    @Test
    public void testValidateGoodCase21() throws XDSMetaDataException {
        new SlotLengthAndNameUniquenessValidator().validateContainer(createContainer(factory21, SLOT_VALUE_21, -1));
    }

    @Test
    public void testValidateTooLong21() throws XDSMetaDataException {
        for (int idx = 0; idx < 7; ++idx) {
            try {
                new SlotLengthAndNameUniquenessValidator().validateContainer(createContainer(factory21, SLOT_VALUE_21, idx));
                fail("Expected exception: " + XDSMetaDataException.class + ", index=" + idx);
            }
            catch (XDSMetaDataException e) {
                // expected
            }
        }
    }

    @Test
    public void testValidateGoodCase30() throws XDSMetaDataException {
        new SlotLengthAndNameUniquenessValidator().validateContainer(createContainer(factory30, SLOT_VALUE_30, -1));
    }

    @Test
    public void testValidateTooLong30() throws XDSMetaDataException {
        for (int idx = 0; idx < 7; ++idx) {
            try {
                new SlotLengthAndNameUniquenessValidator().validateContainer(createContainer(factory30, SLOT_VALUE_30, idx));
                fail("Expected exception: " + XDSMetaDataException.class + ", index=" + idx);
            }
            catch (XDSMetaDataException e) {
                // expected
            }
        }
    }

    private EbXMLObjectContainer createContainer(EbXMLFactory factory, String slotValue, int incorrectIdx) {
        String[] values = new String[7];
        for (int idx = 0; idx < 7; ++idx) {
            values[idx] = idx == incorrectIdx ? slotValue + "!" : slotValue;
        }
        
        EbXMLObjectContainer container = factory.createSubmitObjectsRequest();
        EbXMLObjectLibrary objectLibrary = container.getObjectLibrary();
        
        EbXMLClassification classification1 = factory.createClassification(objectLibrary);
        classification1.addSlot("slot", values[0]);

        EbXMLClassification classification2 = factory.createClassification(objectLibrary);
        classification2.addSlot("slot", values[1]);

        EbXMLClassification classification3 = factory.createClassification(objectLibrary);
        classification3.addSlot("slot1", values[2]);

        EbXMLClassification classification4 = factory.createClassification(objectLibrary);
        classification3.addSlot("slot2", values[3]);
        
        EbXMLAssociation association = factory.createAssociation("assoc", objectLibrary);
        association.addSlot("slot", values[4]);
        association.addClassification(classification1, "scheme");
        
        EbXMLExtrinsicObject extrinsic = factory.createExtrinsic("ex", objectLibrary);
        extrinsic.addSlot("slot", values[5]);
        extrinsic.addClassification(classification2, "scheme");
        
        EbXMLRegistryPackage regPackage = factory.createRegistryPackage("reg", objectLibrary);
        regPackage.addSlot("slot", values[6]);
        regPackage.addClassification(classification3, "scheme");
        
        container.addAssociation(association);
        container.addClassification(classification4);
        container.addExtrinsicObject(extrinsic);
        container.addRegistryPackage(regPackage);
        return container;
    }
}
