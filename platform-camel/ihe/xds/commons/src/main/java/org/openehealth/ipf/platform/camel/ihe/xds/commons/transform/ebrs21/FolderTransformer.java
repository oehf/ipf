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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs21;

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary.*;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs21.Ebrs21.*;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryPackageType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;

/**
 * Transforms between a {@link Folder} and its ebXML 2.1 representation.
 * @author Jens Riemschneider
 */
public class FolderTransformer extends XDSMetaClassTransformer<RegistryPackageType, Folder> {
    private final CodeTransformer codeTransformer = new CodeTransformer();

    /**
     * Constructs the transformer.
     */
    public FolderTransformer() {
        super(FOLDER_PATIENT_ID_EXTERNAL_ID, 
                FOLDER_LOCALIZED_STRING_PATIENT_ID, 
                FOLDER_UNIQUE_ID_EXTERNAL_ID,
                FOLDER_LOCALIZED_STRING_UNIQUE_ID);
    }
    
    @Override
    protected RegistryPackageType createEbXMLInstance() {
        return createRegistryPackage();
    }
    
    @Override
    protected Folder createMetaClassInstance() {
        return new Folder();
    }

    @Override
    protected void addSlotsFromEbXML(Folder folder, RegistryPackageType regPackage) {
        super.addSlotsFromEbXML(folder, regPackage);        
        
        List<SlotType1> slots = regPackage.getSlot();
        folder.setLastUpdateTime(getSingleSlotValue(slots, SLOT_NAME_LAST_UPDATE_TIME));
    }

    @Override
    protected void addSlots(Folder folder, RegistryPackageType regPackage) {
        super.addSlots(folder, regPackage);
        
        List<SlotType1> slots = regPackage.getSlot();
        addSlot(slots, SLOT_NAME_LAST_UPDATE_TIME, folder.getLastUpdateTime());        
    }

    @Override
    protected void addClassificationsFromEbXML(Folder folder, RegistryPackageType regPackage) {
        super.addClassificationsFromEbXML(folder, regPackage);
        
        List<ClassificationType> classifications = regPackage.getClassification();
        List<Code> codes = folder.getCodeList();
        for (ClassificationType code : getClassifications(classifications, FOLDER_CODE_LIST_CLASS_SCHEME)) {
            codes.add(codeTransformer.fromEbXML21(code));
        }
    }

    @Override
    protected void addClassifications(Folder folder, RegistryPackageType regPackage) {
        super.addClassifications(folder, regPackage);
        
        List<ClassificationType> classifications = regPackage.getClassification();        
        for (Code codeListElem : folder.getCodeList()) {
            ClassificationType code = codeTransformer.toEbXML21(codeListElem);
            addClassification(classifications, code, regPackage, FOLDER_CODE_LIST_CLASS_SCHEME);
        }
    }
}
