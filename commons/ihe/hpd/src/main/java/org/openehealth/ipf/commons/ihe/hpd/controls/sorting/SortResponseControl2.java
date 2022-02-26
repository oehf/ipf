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

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.*;

import javax.naming.ldap.BasicControl;
import javax.naming.ldap.SortResponseControl;
import java.io.IOException;

/**
 * @author Dmytro Rud
 * @since 3.7.5
 */
public class SortResponseControl2 extends BasicControl {

    private final SortResponseControl internal;

    public SortResponseControl2(byte[] berBytes, boolean criticality) throws IOException {
        super(SortResponseControl.OID, criticality, null);
        this.internal = new SortResponseControl(SortResponseControl.OID, criticality, berBytes);
    }

    public SortResponseControl2(int resultCode, String failedAttributeName, boolean criticality) throws IOException {
        this(encode(resultCode, failedAttributeName), criticality);
    }

    @Override
    public byte[] getEncodedValue() {
        return internal.getEncodedValue();
    }

    public int getResultCode() {
        return internal.getResultCode();
    }

    public String getFailedAttributeName() {
        return internal.getAttributeID();
    }

    private static byte[] encode(int resultCode, String failedAttributeName) throws IOException {
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(new ASN1Enumerated(resultCode));
        if (failedAttributeName != null) {
            vector.add(new DERTaggedObject(false, 0, new DERUTF8String(failedAttributeName)));
        }
        return new DERSequence(vector).getEncoded();
    }

    @Override
    public String toString() {
        return "SortResponse(" + getResultCode() + "; " + getFailedAttributeName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SortResponseControl2 that = (SortResponseControl2) o;
        return (getResultCode() == that.getResultCode()) &&
               StringUtils.equals(getFailedAttributeName(), that.getFailedAttributeName());
    }

}
