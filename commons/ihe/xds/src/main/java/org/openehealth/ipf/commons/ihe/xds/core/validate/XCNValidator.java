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

import ca.uhn.hl7v2.model.v25.datatype.HD;
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Person;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.PERSON_MISSING_NAME_AND_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates a XCN string.
 * @author Jens Riemschneider
 */
public class XCNValidator implements ValueValidator {
    private static final HDValidator HD_VALIDATOR = new HDValidator();

    @Override
    public void validate(String hl7xcn) throws XDSMetaDataException {
        Person person = Hl7v2Based.parse(hl7xcn, Person.class);
        metaDataAssert(person != null, PERSON_MISSING_NAME_AND_ID, hl7xcn);

        XCN xcn = person.getHapiObject();
        metaDataAssert(
                isNotEmpty(xcn.getXcn1_IDNumber().getValue()) ||
                isNotEmpty(xcn.getXcn2_FamilyName().getFn1_Surname().getValue()),
                PERSON_MISSING_NAME_AND_ID, hl7xcn);

//        Spec actually allows the assigning authority to be missing:
//          "If component 1 (ID Number) is specified, component 9 (Assigning Authority) shall be present if available"
//        metaDataAssert(idNumber == null || hl7hd != null, PERSON_HD_MISSING, hl7xcn);


        HD hd = xcn.getXcn9_AssigningAuthority();
        if (! HD_VALIDATOR.isEmpty(hd)) {
            HD_VALIDATOR.validate(hd, hl7xcn);
        }
    }
}
