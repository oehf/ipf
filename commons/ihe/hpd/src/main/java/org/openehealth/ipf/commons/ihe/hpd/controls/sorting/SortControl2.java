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
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.*;

import javax.naming.ldap.BasicControl;
import javax.naming.ldap.SortControl;
import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class is necessary because:
 * <ol>
 *     <li>JDK does not provide a BER parser for SortControl</li>
 *     <li>SortControl does not expose sorting keys</li>
 * </ol>
 *
 * @author Dmytro Rud
 * @since 3.7.5
 */
public class SortControl2 extends BasicControl {

    private final SortControl internal;

    @Getter
    private final SortKey[] keys;

    public SortControl2(boolean criticality, SortKey... keys) throws IOException {
        super(SortControl.OID, criticality, null);
        this.keys = Objects.requireNonNull(keys);
        this.internal = new SortControl(keys, criticality);
    }

    public SortControl2(byte[] berBytes, boolean criticality) throws IOException {
        this(criticality, decode(berBytes));
    }

    @Override
    public byte[] getEncodedValue() {
        return internal.getEncodedValue();
    }

    private static SortKey[] decode(byte[] berBytes) throws IOException {
        ASN1InputStream inputStream = new ASN1InputStream(berBytes);
        ASN1Sequence sequence = (ASN1Sequence) inputStream.readObject();
        SortKey[] sortKeys = new SortKey[sequence.size()];
        for (int i = 0; i < sequence.size(); ++i) {
            ASN1Sequence item = (ASN1Sequence) sequence.getObjectAt(i);
            String attributeName = decodeString(item.getObjectAt(0));
            String matchingRuleId = null;
            boolean ascendingOrder = true;
            for (int j = 1; j < item.size(); ++j) {
                DLTaggedObject tagged = (DLTaggedObject) item.getObjectAt(j);
                if (tagged.getTagNo() == 0) {
                    matchingRuleId = decodeString(tagged.getBaseObject());
                } else if (tagged.getTagNo() == 1) {
                    ascendingOrder = decodeBoolean(tagged.getBaseObject());
                }
            }
            sortKeys[i] = new SortKey(attributeName, ascendingOrder, matchingRuleId);
        }
        return sortKeys;
    }

    private static String decodeString(ASN1Encodable asn1) {
        return StringUtils.trimToNull(new String(((ASN1OctetString) asn1).getOctets()));
    }

    private static boolean decodeBoolean(ASN1Encodable asn1) {
        return ((ASN1OctetString) asn1).getOctets()[0] == (byte) 0x00;
    }

    @Override
    public String toString() {
        return "Sort(" +
               Arrays.stream(keys).map(key -> "(" + key.getAttributeID() + "; " + key.getMatchingRuleID() + "; " + key.isAscending() + ")").collect(Collectors.joining(", "))
               + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SortControl2 that = (SortControl2) o;
        return toString().equals(that.toString());
    }

}
