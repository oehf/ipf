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
package org.openehealth.ipf.platform.camel.ihe.xds.iti18;

import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.SlotType1;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmytro Rud
 */
public class CodeSlotsNormalizer {

    private static List<String> getSlotValues(SlotType1 slot) {
        var result = new ArrayList<String>();
        if ((slot != null) && (slot.getValueList() != null)) {
            for (var values : slot.getValueList().getValue()) {
                result.addAll(QuerySlotHelper.decodeStringList(values));
            }
        }
        return result;
    }


    /**
     * Converts pairs of code and code scheme values (IHE ITI TF v.5)
     * to HL7v2-based code values (IHE ITI TF v.8).
     *
     * @param adhocQueryRequest
     *      original request, will be modified in-place.
     * @param codeSlotName
     *      name of the slot containing codes.  The scheme slot name will be
     *      created by adding a suffix "Scheme" to this value.
     */
    public static void normalizeCodeSlots(AdhocQueryRequest adhocQueryRequest, String codeSlotName) {
        final var schemeSlotName = codeSlotName + "Scheme";

        var codeSlots = new ArrayList<SlotType1>();
        var schemeSlots = new ArrayList<SlotType1>();

        // collect code and code scheme slots, remove the latter from the query
        var iterator = adhocQueryRequest.getAdhocQuery().getSlot().iterator();
        while (iterator.hasNext()) {
            var slot = iterator.next();
            if (codeSlotName.equals(slot.getName())) {
                codeSlots.add(slot);
            }
            else if (schemeSlotName.equals(slot.getName())) {
                schemeSlots.add(slot);
                iterator.remove();
            }
        }

        // it's OK to have only code slots, but when scheme slots are present,
        // their count must match with the count of code slots
        if (schemeSlots.isEmpty()) {
            return;
        }
        if (codeSlots.size() != schemeSlots.size()) {
            throw new RuntimeException("Counts of " + codeSlotName +
                    " slots and corresponding code scheme slots are not equal");
        }

        // combine slots pairwise
        for (var i = 0; i < codeSlots.size(); ++i) {
            var codeSlot = codeSlots.get(i);
            var schemeSlot = schemeSlots.get(i);

            var codeValues = getSlotValues(codeSlot);
            var schemeValues = getSlotValues(schemeSlot);

            // check equality of value counts
            if (codeValues.size() != schemeValues.size()) {
                throw new RuntimeException(
                        "Counts of code and code scheme values are not equal in the " +
                        codeSlotName + " slot pair No. " + i);
            }

            if (codeValues.isEmpty()) {
                continue;
            }

            // create lists of HL7 v2 code representations
            var sb = new StringBuilder("(");
            for (var j = 0; j < codeValues.size(); ++j) {
                sb.append('\'')
                  .append(codeValues.get(j).replace("'", "''"))
                  .append("^^")
                  .append(schemeValues.get(j).replace("'", "''"))
                  .append("',");
            }
            sb.deleteCharAt(sb.length() - 1).append(')');

            // replace code slot contents with the new value
            codeSlot.getValueList().getValue().clear();
            codeSlot.getValueList().getValue().add(sb.toString());
        }
    }
}
