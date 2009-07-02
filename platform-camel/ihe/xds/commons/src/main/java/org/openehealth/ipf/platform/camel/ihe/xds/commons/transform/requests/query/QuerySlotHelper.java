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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.query;

import static org.apache.commons.lang.Validate.notNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7Delimiter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryList;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;

/**
 * Base class of transformers for all queries.
 * @author Jens Riemschneider
 */
class QuerySlotHelper {
    private AdhocQueryRequest ebXML;

    public QuerySlotHelper(AdhocQueryRequest ebXML) {
        notNull(ebXML, "ebXML cannot be null");
        this.ebXML = ebXML;
    }

    String toString(QueryParameter param) {
        String value = ebXML.getSingleSlotValue(param.getSlotName());
        return decodeString(value);
    }
    
    void fromString(QueryParameter param, String value) {
        if (value != null) {
            ebXML.addSlot(param.getSlotName(), encodeAsString(value));
        }
    }

    void fromCode(QueryParameter param, List<Code> codes) {
        List<String> slotValues = new ArrayList<String>();
        for (Code code : codes) {
            String hl7CE = fromCodeToHL7CE(code);
            slotValues.add(encodeAsStringList(hl7CE));
        }
        ebXML.addSlot(param.getSlotName(), slotValues.toArray(new String[0]));
    }

    private String fromCodeToHL7CE(Code code) {
        return HL7.render(HL7Delimiter.COMPONENT, 
                HL7.escape(code.getCode()),
                null,
                HL7.escape(code.getSchemeName()));
    }
    
    private Code toCodeFromHL7CE(String hl7CE) {
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7CE);
        return new Code(
                HL7.get(parts, 1, true), 
                null, 
                HL7.get(parts, 3, true));
    }

    void toCode(QueryParameter param, List<Code> codes) {
        toCode(ebXML.getSlotValues(param.getSlotName()), codes);
    }

    private void toCode(List<String> slotValues, List<Code> codes) {
        codes.clear();
        for (String slotValue : slotValues) {
            for (String hl7CE : decodeStringList(slotValue)) {
                codes.add(toCodeFromHL7CE(hl7CE));
            }
        }
    }
    
    void fromStringList(QueryParameter param, List<String> values) {
        List<String> slotValues = new ArrayList<String>();
        for (String value : values) {
            slotValues.add(encodeAsStringList(value));
        }
        ebXML.addSlot(param.getSlotName(), slotValues.toArray(new String[0]));
    }
    
    void toStringList(QueryParameter param, List<String> values) {
        values.clear();
        List<String> slotValues = ebXML.getSlotValues(param.getSlotName());
        for (String slotValue : slotValues) {            
            values.addAll(decodeStringList(slotValue));
        }
    }

    void fromNumber(QueryParameter param, String value) {
        ebXML.addSlot(param.getSlotName(), value);
    }
    
    String toNumber(QueryParameter param) {
        return ebXML.getSingleSlotValue(param.getSlotName());
    }

    void fromStatus(QueryParameter param, List<AvailabilityStatus> status) {
        List<String> opcodes = new ArrayList<String>(status.size());
        for (AvailabilityStatus statusValue : status) {
            opcodes.add(AvailabilityStatus.toQueryOpcode(statusValue));
        }
        fromStringList(param, opcodes);
    }
    
    void fromAssociationType(QueryParameter param, List<AssociationType> associationTypes) {
        List<String> opcodes = new ArrayList<String>(associationTypes.size());
        for (AssociationType type : associationTypes) {
            opcodes.add(AssociationType.getOpcode30(type));
        }
        fromStringList(param, opcodes);
    }
    
    void toStatus(QueryParameter param, List<AvailabilityStatus> list) {
        List<String> opcodes = new ArrayList<String>(); 
        toStringList(param, opcodes);

        list.clear();
        for (String opcode : opcodes) {
            list.add(AvailabilityStatus.valueOfOpcode(opcode));
        }
    }

    void toAssociationType(QueryParameter param, List<AssociationType> list) {
        List<String> opcodes = new ArrayList<String>(); 
        toStringList(param, opcodes);

        list.clear();
        for (String opcode : opcodes) {
            list.add(AssociationType.valueOfOpcode(opcode));
        }
    }

    void fromCode(QueryParameter param, QueryList<Code> queryList) {
        for (List<Code> codes : queryList.getOuterList()) {
            fromCode(param, codes);
        }
    }
    
    private String encodeAsString(String value) {
        if (value == null) {
            return null;
        }
        return "'" + value.replace("'", "''") + "'";
    }

    private String decodeString(String value) {
        if (value == null) {
            return null;
        }
        
        if (value.startsWith("'") && value.endsWith("'")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replaceAll("''", "'");
    }

    private String encodeAsStringList(String value) {
        if (value == null) {
            return null;
        }
        return "('" + value.replace("'", "''") + "')";
    }

    private List<String> decodeStringList(String list) {
        if (list == null) {
            return Collections.emptyList();
        }
        
        if (list.startsWith("(") && list.endsWith(")")) {
            list = list.substring(1, list.length() - 1);
        }
        
        List<String> values = new ArrayList<String>();

        Pattern pattern = Pattern.compile("\\s*,{0,1}\\s*'((?:[^']*(?:'')*[^']*)*)'(.*)");
        Matcher matcher = pattern.matcher(list);
        while (matcher.matches() && matcher.groupCount() == 2) {
            String value = matcher.group(1);
            value = value.replaceAll("''", "'");
            values.add(value);
            matcher = pattern.matcher(matcher.group(2));            
        }
        
        return values;
    }

    public void toCode(QueryParameter param, QueryList<Code> queryList) {
        queryList.getOuterList().clear();

        List<Slot> slots = ebXML.getSlots(param.getSlotName());
        for (Slot slot : slots) {
            List<Code> innerList = new ArrayList<Code>();
            toCode(slot.getValueList(), innerList);
            queryList.getOuterList().add(innerList);
        }
    }

}
