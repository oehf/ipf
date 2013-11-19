/*
 * Copyright 2013 the original author or authors.
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

import ca.uhn.hl7v2.model.v25.datatype.CWE;
import ca.uhn.hl7v2.model.v25.datatype.HD;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author Dmytro Rud
 */
public class HL7ValidationUtils {

    public static boolean isEmptyField(HD hd) {
        return isEmpty(hd.getHd1_NamespaceID().getValue()) &&
               isEmpty(hd.getHd2_UniversalID().getValue()) &&
               isEmpty(hd.getHd3_UniversalIDType().getValue());
    }

    public static boolean isNotEmptyField(HD hd) {
        return !isEmptyField(hd);
    }

    public static boolean isEmptyField(CWE cwe) {
        return isEmpty(cwe.getCwe1_Identifier().getValue()) &&
               isEmpty(cwe.getCwe2_Text().getValue()) &&
               isEmpty(cwe.getCwe3_NameOfCodingSystem().getValue()) &&
               isEmpty(cwe.getCwe4_AlternateIdentifier().getValue()) &&
               isEmpty(cwe.getCwe5_AlternateText().getValue()) &&
               isEmpty(cwe.getCwe6_NameOfAlternateCodingSystem().getValue()) &&
               isEmpty(cwe.getCwe7_CodingSystemVersionID().getValue()) &&
               isEmpty(cwe.getCwe8_AlternateCodingSystemVersionID().getValue()) &&
               isEmpty(cwe.getCwe9_OriginalText().getValue());
    }

}
