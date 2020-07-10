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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import static org.apache.commons.lang3.StringUtils.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.HL7ValidationUtils.isEmptyField;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.CX_NEEDS_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.CX_TOO_MANY_COMPONENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates a CX value.
 * @author Jens Riemschneider
 */
public class CXValidator implements ValueValidator {
    private static final HDValidator HD_VALIDATOR = new HDValidator();

    private final boolean assigningAuthorityRequired;

    public CXValidator(boolean assigningAuthorityRequired) {
        this.assigningAuthorityRequired = assigningAuthorityRequired;
    }


    @Override
    public void validate(String hl7CX) throws XDSMetaDataException {
        var identifiable = Hl7v2Based.parse(hl7CX, Identifiable.class);
        metaDataAssert(identifiable != null, CX_NEEDS_ID);

        var cx = identifiable.getHapiObject();

        // prohibited fields
        metaDataAssert(isEmpty(cx.getCx2_CheckDigit().getValue()), CX_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(cx.getCx3_CheckDigitScheme().getValue()), CX_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(cx.getCx5_IdentifierTypeCode().getValue()), CX_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmptyField(cx.getCx6_AssigningFacility()), CX_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(cx.getCx7_EffectiveDate().getValue()), CX_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(cx.getCx8_ExpirationDate().getValue()), CX_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmptyField(cx.getCx9_AssigningJurisdiction()), CX_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmptyField(cx.getCx10_AssigningAgencyOrDepartment()), CX_TOO_MANY_COMPONENTS);

        // required and optional fields
        metaDataAssert(isNotEmpty(cx.getCx1_IDNumber().getValue()), CX_NEEDS_ID, hl7CX);

        var assigningAuthority = cx.getCx4_AssigningAuthority();
        if (assigningAuthorityRequired || (! isEmptyField(assigningAuthority))) {
            HD_VALIDATOR.validate(assigningAuthority, hl7CX);
        }
    }
}
