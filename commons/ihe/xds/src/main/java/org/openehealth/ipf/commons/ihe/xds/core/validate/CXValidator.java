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

import ca.uhn.hl7v2.model.v25.datatype.CX;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import static org.apache.commons.lang3.StringUtils.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.CX_NEEDS_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.CX_TOO_MANY_COMPONENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates a CX value.
 * @author Jens Riemschneider
 */
public class CXValidator implements ValueValidator {
    private final HDValidator HD_VALIDATOR = new HDValidator();

    @Override
    public void validate(String hl7CX) throws XDSMetaDataException {
        Identifiable identifiable = Hl7v2Based.parse(hl7CX, Identifiable.class);
        metaDataAssert(identifiable != null, CX_NEEDS_ID);

        CX cx = identifiable.getHapiObject();

        metaDataAssert(countMatches(hl7CX, "^") <= 3, CX_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(cx.getCx2_CheckDigit().getValue()), CX_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(cx.getCx3_CheckDigitScheme().getValue()), CX_TOO_MANY_COMPONENTS);

        metaDataAssert(isNotEmpty(cx.getCx1_IDNumber().getValue()), CX_NEEDS_ID);

        HD_VALIDATOR.validate(cx.getCx4_AssigningAuthority(), hl7CX);
    }
}
