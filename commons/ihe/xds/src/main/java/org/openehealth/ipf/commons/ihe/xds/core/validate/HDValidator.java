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

import ca.uhn.hl7v2.model.v25.datatype.HD;
import org.apache.commons.lang3.StringUtils;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validator for HL7 v2 HD elements.
 * @author Dmytro Rud
 */
public class HDValidator {
    private static final OIDValidator OID_VALIDATOR = new OIDValidator();

    public boolean isEmpty(HD hd) {
        return StringUtils.isEmpty(hd.getHd1_NamespaceID().getValue()) &&
               StringUtils.isEmpty(hd.getHd2_UniversalID().getValue()) &&
               StringUtils.isEmpty(hd.getHd3_UniversalIDType().getValue());
    }


    /**
     * Validates an HL7 v2 HD element.
     * @param hd
     *      HD element.
     * @param original
     *      original string from XDS message where which contained the given HD element.
     */
    public void validate(HD hd, String original) {
        metaDataAssert(StringUtils.isEmpty(hd.getHd1_NamespaceID().getValue()),
                HD_MUST_NOT_HAVE_NAMESPACE_ID, original);

        metaDataAssert("ISO".equals(hd.getHd3_UniversalIDType().getValue()),
                UNIVERSAL_ID_TYPE_MUST_BE_ISO, original);

        String oid = hd.getHd2_UniversalID().getValue();
        metaDataAssert(StringUtils.isNotEmpty(oid), HD_NEEDS_UNIVERSAL_ID, original);

        OID_VALIDATOR.validate(oid);
    }
}
