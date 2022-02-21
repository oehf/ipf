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
import org.bouncycastle.asn1.*;
import org.openehealth.ipf.commons.ihe.hpd.controls.AbstractControl;
import org.openehealth.ipf.commons.ihe.hpd.controls.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * See <a href="https://www.ietf.org/rfc/rfc2891.txt">RFC 2891</a>
 * "LDAP Control Extension for Server Side Sorting of Search Results".
 *
 * @author Dmytro Rud
 * @since 3.7.5
 */
public class Sorting extends AbstractControl {

    public static final String TYPE = "1.2.840.113556.1.4.473";

    static {
        Utils.getMAP().put(TYPE, Sorting.class);
    }

    @Setter
    private List<SortingCriterion> sortingCriteria;

    public List<SortingCriterion> getSortingCriteria() {
        if (sortingCriteria == null) {
            sortingCriteria = new ArrayList<>();
        }
        return sortingCriteria;
    }

    public Sorting(ASN1Sequence asn1Sequence, boolean criticality) {
        super(TYPE, criticality);
        this.sortingCriteria = new ArrayList<>();
        while(asn1Sequence.iterator().hasNext()) {
            addSortingCriterion((ASN1Sequence) asn1Sequence.iterator().next());
        }
    }

    private void addSortingCriterion(ASN1Sequence asn1Sequence) {
        ASN1UTF8String attributeObject = (ASN1UTF8String) asn1Sequence.getObjectAt(0);
        ASN1UTF8String orderingRule1Object = (ASN1UTF8String) asn1Sequence.getObjectAt(1);
        ASN1Boolean reverseOrderObject = (ASN1Boolean) asn1Sequence.getObjectAt(2);
        this.sortingCriteria.add(new SortingCriterion(
                attributeObject.getString(),
                orderingRule1Object.getString(),
                reverseOrderObject.isTrue()));
    }

    @Override
    protected ASN1Encodable[] getASN1SequenceElements() throws IOException {
        ASN1Encodable[] result = new ASN1Encodable[sortingCriteria.size()];
        for (int i = 0; i < sortingCriteria.size(); ++i) {
            SortingCriterion criterion = sortingCriteria.get(i);
            result[i] = new DLSequence(new ASN1Encodable[]{
                    ASN1UTF8String.getInstance(criterion.attribute),
                    ASN1UTF8String.getInstance(criterion.orderingRuleId),
                    ASN1Boolean.getInstance(criterion.reverseOrder)
            });
        }
        return result;
    }

    public static class SortingCriterion {

        @Getter
        @Setter
        private String attribute;

        @Getter
        @Setter
        private String orderingRuleId;

        @Getter
        @Setter
        private boolean reverseOrder;

        public SortingCriterion(String attribute, String orderingRuleId, boolean reverseOrder) {
            this.attribute = attribute;
            this.orderingRuleId = orderingRuleId;
            this.reverseOrder = reverseOrder;
        }
    }

}
