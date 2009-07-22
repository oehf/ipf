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
 * <p>
 * Note that this class is only used for ebXML 3.0! 
 * @author Jens Riemschneider
 */
public class QuerySlotHelper {
    private EbXMLAdhocQueryRequest ebXML;

    /**
     * Constructs the wrapper.
     * @param ebXML
     *          the wrapped object.
     */
    public QuerySlotHelper(EbXMLAdhocQueryRequest ebXML) {
        notNull(ebXML, "ebXML cannot be null");
        this.ebXML = ebXML;
    }

    /**
     * Retrieves a string-valued parameter from a slot.
     * @param param
     *          the parameter.
     * @return the string value.
     */
    public String toString(QueryParameter param) {
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
    public void fromString(QueryParameter param, String value) {
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
    public void fromCode(QueryParameter param, List<Code> codes) {
        List<String> slotValues = new ArrayList<String>();
        for (Code code : codes) {
            String hl7CE = fromCodeToHL7CE(code);
            slotValues.add(encodeAsStringList(hl7CE));
        }
        ebXML.addSlot(param.getSlotName(), slotValues.toArray(new String[0]));
    }

    private void toCode(QueryParameter param, List<Code> codes) {
        toCode(ebXML.getSlotValues(param.getSlotName()), codes);
    }

    /**
     * Stores a list of codes into a slot.
     * @param param
     *          the parameter.
     * @param queryList
     *          the list of codes.
     */
    public void fromCode(QueryParameter param, QueryList<Code> queryList) {
        for (List<Code> codes : queryList.getOuterList()) {
            fromCode(param, codes);
        }
    }
    
    private void toCode(QueryParameter param, QueryList<Code> queryList) {
        queryList.getOuterList().clear();

        List<EbXMLSlot> slots = ebXML.getSlots(param.getSlotName());
        for (EbXMLSlot slot : slots) {
            List<Code> innerList = new ArrayList<Code>();
            toCode(slot.getValueList(), innerList);
            queryList.getOuterList().add(innerList);
        }
    }
    
    /**
     * Retrieves a list of strings from a slot.
     * @param param
     *          the parameter.
     * @param queryList
     *          the string list to be filled.
     */
    public void toStringList(QueryParameter param, QueryList<String> queryList) {
        queryList.getOuterList().clear();

        List<EbXMLSlot> slots = ebXML.getSlots(param.getSlotName());
        for (EbXMLSlot slot : slots) {
            List<String> innerList = new ArrayList<String>();
            for (String slotValue : slot.getValueList()) {            
                innerList.addAll(decodeStringList(slotValue));
            }
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
    public void fromStringList(QueryParameter param, List<String> values) {
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
    public void toStringList(QueryParameter param, List<String> values) {
        values.clear();
        List<String> slotValues = ebXML.getSlotValues(param.getSlotName());
        for (String slotValue : slotValues) {            
            values.addAll(decodeStringList(slotValue));
        }
    }

    /**
     * Retrieves a list of codes from a slot.
     * @param param
     *          the parameter.
     * @param schemeParam
     *          the code scheme parameter.
     * @param codes
     *          the codes to be filled.
     */
    public void toCodes(QueryParameter param, QueryParameter schemeParam, List<Code> codes) {
        toCode(param, codes);
        List<String> schemes = new ArrayList<String>();
        toStringList(schemeParam, schemes);
        for (int idx = 0; idx < schemes.size() && idx < codes.size(); ++idx) {
            codes.get(idx).setSchemeName(schemes.get(idx));
        }
    }

    /**
     * Retrieves a list of codes from a slot.
     * @param param
     *          the parameter.
     * @param schemeParam
     *          the code scheme parameter.
     * @param codes
     *          the codes to be filled.
     */
    public void toCodes(QueryParameter param, QueryParameter schemeParam, QueryList<Code> codes) {
        toCode(param, codes);
        QueryList<String> schemes = new QueryList<String>();
        toStringList(schemeParam, schemes);
        
        List<List<String>> schemesOuter = schemes.getOuterList();
        List<List<Code>> codesOuter = codes.getOuterList();
        for (int outer = 0; outer < schemesOuter.size() && outer < codesOuter.size(); ++outer) {
            List<String> schemesInner = schemesOuter.get(outer);
            List<Code> codesInner = codesOuter.get(outer);
            for (int inner = 0; inner < schemesInner.size() && inner < codesInner.size(); ++inner) {
                codesInner.get(inner).setSchemeName(schemesInner.get(inner));
            }
        }
    }
    
    /**
     * Stores a numbered parameter into a slot.
     * @param param
     *          the parameter.
     * @param value
     *          the value.
     */
    public void fromNumber(QueryParameter param, String value) {
        ebXML.addSlot(param.getSlotName(), value);
    }
    
    /**
     * Retrieves a numbered parameter from a slot.
     * @param param
     *          the parameter.
     * @return the value.
     */
    public String toNumber(QueryParameter param) {
        return ebXML.getSingleSlotValue(param.getSlotName());
    }

    /**
     * Stores a status parameter into a slot.
     * @param param
     *          the parameter.
     * @param status
     *          the list of status values.
     */
    public void fromStatus(QueryParameter param, List<AvailabilityStatus> status) {
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
    public void toStatus(QueryParameter param, List<AvailabilityStatus> list) {
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
    public void fromAssociationType(QueryParameter param, List<AssociationType> associationTypes) {
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
    public void toAssociationType(QueryParameter param, List<AssociationType> associationTypes) {
        List<String> opcodes = new ArrayList<String>(); 
        toStringList(param, opcodes);

        associationTypes.clear();
        for (String opcode : opcodes) {
            associationTypes.add(AssociationType.valueOfOpcode30(opcode));
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

        Pattern pattern = Pattern.compile("\\s*,{0,1}\\s*'((?:[^']*(?:'')*[^']*)*)'(.*)", Pattern.DOTALL);
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
