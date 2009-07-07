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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLSlot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7Delimiter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryList;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;

/**
 * Wrapper class for ebXML query request to simplify access to slots.
 * <p>
 * This class ensures that the various encoding rules of query parameter
 * values are met. 
 * @author Jens Riemschneider
 */
class QuerySlotHelper {
    private EbXMLAdhocQueryRequest ebXML;

    /**
     * Constructs the wrapper.
     * @param ebXML
     *          the wrapped object.
     */
    QuerySlotHelper(EbXMLAdhocQueryRequest ebXML) {
        notNull(ebXML, "ebXML cannot be null");
        this.ebXML = ebXML;
    }

    /**
     * Retrieves a string-valued parameter from a slot.
     * @param param
     *          the parameter.
     * @return the string value.
     */
    String toString(QueryParameter param) {
        String value = ebXML.getSingleSlotValue(param.getSlotName());
        return decodeString(value);
    }
    
    /**
     * Stores a string-valued parameter into a slot.
     * @param param
     *          the parameter.
     * @param value
     *          the string value.
     */
    void fromString(QueryParameter param, String value) {
        if (value != null) {
            ebXML.addSlot(param.getSlotName(), encodeAsString(value));
        }
    }

    /**
     * Stores a list of codes into a slot. 
     * @param param
     *          the parameter.
     * @param codes
     *          the list of codes.
     */
    void fromCode(QueryParameter param, List<Code> codes) {
        List<String> slotValues = new ArrayList<String>();
        for (Code code : codes) {
            String hl7CE = fromCodeToHL7CE(code);
            slotValues.add(encodeAsStringList(hl7CE));
        }
        ebXML.addSlot(param.getSlotName(), slotValues.toArray(new String[0]));
    }

    /**
     * Retrieves a list of codes from a slot.
     * @param param
     *          the parameter.
     * @param codes
     *          the codes to be filled.
     */
    void toCode(QueryParameter param, List<Code> codes) {
        toCode(ebXML.getSlotValues(param.getSlotName()), codes);
    }

    /**
     * Stores a list of codes into a slot.
     * @param param
     *          the parameter.
     * @param queryList
     *          the list of codes.
     */
    void fromCode(QueryParameter param, QueryList<Code> queryList) {
        for (List<Code> codes : queryList.getOuterList()) {
            fromCode(param, codes);
        }
    }
    
    /**
     * Retrieves a list of codes from a slot.
     * @param param
     *          the parameter.
     * @param codes
     *          the codes to be filled.
     */
    void toCode(QueryParameter param, QueryList<Code> queryList) {
        queryList.getOuterList().clear();

        List<EbXMLSlot> slots = ebXML.getSlots(param.getSlotName());
        for (EbXMLSlot slot : slots) {
            List<Code> innerList = new ArrayList<Code>();
            toCode(slot.getValueList(), innerList);
            queryList.getOuterList().add(innerList);
        }
    }
    
    /**
     * Stores a list of strings into a slot.
     * @param param
     *          the parameter.
     * @param values
     *          the string list.
     */
    void fromStringList(QueryParameter param, List<String> values) {
        List<String> slotValues = new ArrayList<String>();
        for (String value : values) {
            slotValues.add(encodeAsStringList(value));
        }
        ebXML.addSlot(param.getSlotName(), slotValues.toArray(new String[0]));
    }
    
    /**
     * Retrieves a list of strings from a slot.
     * @param param
     *          the parameter.
     * @param values
     *          the string list to be filled.
     */
    void toStringList(QueryParameter param, List<String> values) {
        values.clear();
        List<String> slotValues = ebXML.getSlotValues(param.getSlotName());
        for (String slotValue : slotValues) {            
            values.addAll(decodeStringList(slotValue));
        }
    }

    /**
     * Stores a numbered parameter into a slot.
     * @param param
     *          the parameter.
     * @param value
     *          the value.
     */
    void fromNumber(QueryParameter param, String value) {
        ebXML.addSlot(param.getSlotName(), value);
    }
    
    /**
     * Retrieves a numbered parameter from a slot.
     * @param param
     *          the parameter.
     * @return the value.
     */
    String toNumber(QueryParameter param) {
        return ebXML.getSingleSlotValue(param.getSlotName());
    }

    /**
     * Stores a status parameter into a slot.
     * @param param
     *          the parameter.
     * @param status
     *          the list of status values.
     */
    void fromStatus(QueryParameter param, List<AvailabilityStatus> status) {
        List<String> opcodes = new ArrayList<String>(status.size());
        for (AvailabilityStatus statusValue : status) {
            opcodes.add(AvailabilityStatus.toQueryOpcode(statusValue));
        }
        fromStringList(param, opcodes);
    }

    /**
     * Retrieves a status parameter from a slot.
     * @param param
     *          the parameter.
     * @param list
     *          the list of status values to be filled.
     */
    void toStatus(QueryParameter param, List<AvailabilityStatus> list) {
        List<String> opcodes = new ArrayList<String>(); 
        toStringList(param, opcodes);

        list.clear();
        for (String opcode : opcodes) {
            list.add(AvailabilityStatus.valueOfOpcode(opcode));
        }
    }

    /**
     * Stores an association parameter into a slot.
     * @param param
     *          the parameter.
     * @param associationTypes
     *          the list of association types.
     */
    void fromAssociationType(QueryParameter param, List<AssociationType> associationTypes) {
        List<String> opcodes = new ArrayList<String>(associationTypes.size());
        for (AssociationType type : associationTypes) {
            opcodes.add(AssociationType.getOpcode30(type));
        }
        fromStringList(param, opcodes);
    }
    
    /**
     * Retrieves an association parameter from a slot. 
     * @param param
     *          the parameter.
     * @param list
     *          the list of association types to be filled
     */
    void toAssociationType(QueryParameter param, List<AssociationType> associationTypes) {
        List<String> opcodes = new ArrayList<String>(); 
        toStringList(param, opcodes);

        associationTypes.clear();
        for (String opcode : opcodes) {
            associationTypes.add(AssociationType.valueOfOpcode(opcode));
        }
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

    private void toCode(List<String> slotValues, List<Code> codes) {
        codes.clear();
        for (String slotValue : slotValues) {
            for (String hl7CE : decodeStringList(slotValue)) {
                codes.add(toCodeFromHL7CE(hl7CE));
            }
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
}
