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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import static org.apache.commons.lang3.Validate.notNull;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7;
import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7Delimiter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wrapper class for ebXML query request to simplify access to slots.
 * <p>
 * This class ensures that the various encoding rules of query parameter
 * values are met.
 * <p>
 * Note that this class is only used for ebXML 3.0! 
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class QuerySlotHelper {
    private static final Pattern PATTERN =
            Pattern.compile("\\s*,?\\s*'((?:[^']*(?:'')*[^']*)*)'(.*)", Pattern.DOTALL);

    private final EbXMLAdhocQueryRequest ebXML;

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
        if (codes == null) {
            return;
        }
        
        List<String> slotValues = new ArrayList<String>();
        for (Code code : codes) {
            String hl7CE = fromCodeToHL7CE(code);
            slotValues.add(encodeAsStringList(hl7CE));
        }
        ebXML.addSlot(param.getSlotName(), slotValues.toArray(new String[slotValues.size()]));
    }

    /**
     * Stores a list of codes into a slot.
     * @param param
     *          the parameter.
     * @param queryList
     *          the list of codes.
     */
    public void fromCode(QueryParameter param, QueryList<Code> queryList) {
        if (queryList == null) {
            return;
        }
        
        for (List<Code> codes : queryList.getOuterList()) {
            fromCode(param, codes);
        }
    }
    
    private QueryList<Code> toCodeQueryList(QueryParameter param) {
        List<EbXMLSlot> slots = ebXML.getSlots(param.getSlotName());
        if (slots.isEmpty()) {
            return null;
        }
        
        QueryList<Code> queryList = new QueryList<Code>(); 
        for (EbXMLSlot slot : slots) {
            List<Code> innerList = toCode(slot.getValueList());
            queryList.getOuterList().add(innerList);
        }
        return queryList;
    }
    
    /**
     * Retrieves a list of strings from a slot.
     * @param param
     *          the parameter.
     * @return the string list.
     */
    public QueryList<String> toStringQueryList(QueryParameter param) {
        List<EbXMLSlot> slots = ebXML.getSlots(param.getSlotName());
        if (slots.isEmpty()) {
            return null;
        }
        
        QueryList<String> queryList = new QueryList<String>();
        for (EbXMLSlot slot : slots) {
            List<String> innerList = new ArrayList<String>();
            for (String slotValue : slot.getValueList()) {            
                innerList.addAll(decodeStringList(slotValue));
            }
            queryList.getOuterList().add(innerList);
        }
        return queryList;
    }
    
    /**
     * Stores a list of strings into a slot.
     * @param param
     *          the parameter.
     * @param values
     *          the string list.
     */
    public void fromStringList(QueryParameter param, List<String> values) {
        if (values == null) {
            return;
        }
        
        List<String> slotValues = new ArrayList<String>();
        for (String value : values) {
            slotValues.add(encodeAsStringList(value));
        }
        ebXML.addSlot(param.getSlotName(), slotValues.toArray(new String[slotValues.size()]));
    }
    
    /**
     * Retrieves a list of strings from a slot.
     * @param param
     *          the parameter.
     * @return the string list.
     */
    public List<String> toStringList(QueryParameter param) {
        List<String> slotValues = ebXML.getSlotValues(param.getSlotName());
        if (slotValues.isEmpty()) {
            return null;
        }
        
        List<String> values = new ArrayList<String>();
        for (String slotValue : slotValues) {            
            values.addAll(decodeStringList(slotValue));
        }
        return values;
    }

    /**
     * Retrieves a list of codes from a slot.
     * @param param
     *          the parameter.
     * @return the codes.
     */
    public List<Code> toCodeList(QueryParameter param) {
        return toCode(ebXML.getSlotValues(param.getSlotName()));
    }

    /**
     * Retrieves a list of codes from a slot.
     * @param param
     *          the parameter.
     * @param schemeParam
     *          the code scheme parameter.
     * @return the codes.
     */
    public QueryList<Code> toCodeQueryList(QueryParameter param, QueryParameter schemeParam) {
        QueryList<Code> codes = toCodeQueryList(param);
        if (codes == null) {
            return null;            
        }
        
        QueryList<String> schemes = toStringQueryList(schemeParam);
        if (schemes != null) {
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
        return codes;
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
        if (status == null) {
            return;
        }
        
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
     * @return the list of status values.
     */
    public List<AvailabilityStatus> toStatus(QueryParameter param) {
        List<String> opcodes = toStringList(param);
        if (opcodes == null) {
            return null;
        }

        List<AvailabilityStatus> list = new ArrayList<AvailabilityStatus>();
        for (String opcode : opcodes) {
            AvailabilityStatus status = AvailabilityStatus.valueOfOpcode(opcode);
            if (status != null) {
                list.add(status);
            }
        }
        return list;
    }

    /**
     * Stores an association parameter into a slot.
     * @param param
     *          the parameter.
     * @param associationTypes
     *          the list of association types.
     */
    public void fromAssociationType(QueryParameter param, List<AssociationType> associationTypes) {
        if (associationTypes == null) {
            return;
        }
        
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
     * @return the list of association types.
     */
    public List<AssociationType> toAssociationType(QueryParameter param) {
        List<String> opcodes = toStringList(param);
        if (opcodes == null) {
            return null;
        }

        List<AssociationType> associationTypes = new ArrayList<AssociationType>();
        for (String opcode : opcodes) {
            associationTypes.add(AssociationType.valueOfOpcode30(opcode));
        }
        return associationTypes;
    }

    public void fromDocumentEntryType(QueryParameter param, List<DocumentEntryType> documentEntryTypes) {
        if (documentEntryTypes == null) {
            return;
        }

        List<String> uuids = new ArrayList<String>(documentEntryTypes.size());
        for (DocumentEntryType type : documentEntryTypes) {
            uuids.add(DocumentEntryType.toUuid(type));
        }
        fromStringList(param, uuids);
    }

    public List<DocumentEntryType> toDocumentEntryType(QueryParameter param) {
        List<String> uuids = toStringList(param);
        if (uuids == null) {
            return null;
        }

        ArrayList<DocumentEntryType> documentEntryTypes = new ArrayList<DocumentEntryType>();
        for (String uuid : uuids) {
            documentEntryTypes.add(DocumentEntryType.valueOfUuid(uuid));
        }
        return documentEntryTypes;
    }

    private String fromCodeToHL7CE(Code code) {
        if (code == null) {
            return null;
        }
        return HL7.render(HL7Delimiter.COMPONENT, 
                HL7.escape(code.getCode()),
                null,
                HL7.escape(code.getSchemeName()));
    }
    
    private Code toCodeFromHL7CE(String hl7CE) {
        if (hl7CE == null) {
            return null;
        }
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7CE);
        return new Code(
                HL7.get(parts, 1, true), 
                null, 
                HL7.get(parts, 3, true));
    }

    private List<Code> toCode(List<String> slotValues) {
        if (slotValues.isEmpty()) {
            return null;
        }
        
        List<Code> codes = new ArrayList<Code>();
        for (String slotValue : slotValues) {
            for (String hl7CE : decodeStringList(slotValue)) {
                codes.add(toCodeFromHL7CE(hl7CE));
            }
        }
        return codes;
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
            return null;
        }
        
        if (list.startsWith("(") && list.endsWith(")")) {
            list = list.substring(1, list.length() - 1);
        }
        
        List<String> values = new ArrayList<String>();

        Matcher matcher = PATTERN.matcher(list);
        while (matcher.matches() && matcher.groupCount() == 2) {
            String value = matcher.group(1);
            value = value.replaceAll("''", "'");
            values.add(value);
            matcher = PATTERN.matcher(matcher.group(2));
        }
        
        return values;
    }
}
