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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30;

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary.*;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;

/**
 * Transforms between {@link Code} and its ebXML 3.0 representation.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class CodeTransformer {
    public ClassificationType toEbXML30(Code code) {
        if (code == null) {
            return null;
        }
        
        ClassificationType classification = Ebrs30.createClassification(null);
        classification.setNodeRepresentation(code.getCode());
        classification.setName(Ebrs30.createInternationalString(code.getDisplayName()));
        
        if (code.getSchemeName() != null) {
            Ebrs30.addSlot(classification.getSlot(), SLOT_NAME_CODING_SCHEME, code.getSchemeName());
        }
        
        return classification;
    }
    
    public Code fromEbXML30(ClassificationType classification) {
        if (classification == null) {
            return null;
        }
        
        Code code = new Code();
        code.setCode(classification.getNodeRepresentation());
        code.setDisplayName(Ebrs30.getLocalizedString(classification.getName(), 0));

        List<String> slotValues = Ebrs30.getSlotValues(classification, SLOT_NAME_CODING_SCHEME);
        if (slotValues.size() > 0) {
            code.setSchemeName(slotValues.get(0));
        }
        
        return code;
    }
}
