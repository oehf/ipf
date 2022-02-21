/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.controls.sorting;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1UTF8String;
import org.openehealth.ipf.commons.ihe.hpd.controls.AbstractControl;
import org.openehealth.ipf.commons.ihe.hpd.controls.Utils;

import java.io.IOException;

/**
 * See <a href="https://www.ietf.org/rfc/rfc2891.txt">RFC 2891</a>
 * "LDAP Control Extension for Server Side Sorting of Search Results".
 *
 * @author Dmytro Rud
 * @since 3.7.5
 */
public class SortingResponse extends AbstractControl {

    public static final String TYPE = "1.2.840.113556.1.4.474";

    static {
        Utils.getMAP().put(TYPE, SortingResponse.class);
    }

    @Getter
    @Setter
    private int resultCode;

    @Getter
    @Setter
    private String failedAttribute;

    public SortingResponse(int resultCode, String failedAttribute, boolean criticality) {
        super(TYPE, criticality);
        this.resultCode = resultCode;
        this.failedAttribute = failedAttribute;
    }

    public SortingResponse(ASN1Sequence asn1Sequence, boolean criticality) {
        super(TYPE, criticality);
        ASN1Integer resultCodeObject = (ASN1Integer) asn1Sequence.getObjectAt(0);
        ASN1UTF8String failedAttributeObject = (ASN1UTF8String) asn1Sequence.getObjectAt(1);
        this.resultCode = resultCodeObject.intValueExact();
        this.failedAttribute = failedAttributeObject.getString();
    }

    @Override
    protected ASN1Encodable[] getASN1SequenceElements() throws IOException {
        return new ASN1Encodable[]{
                ASN1Integer.getInstance(resultCode),
                ASN1UTF8String.getInstance(failedAttribute)
        };
    }

}
