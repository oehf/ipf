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

import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.AssociationTypeFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.AvailabilityStatusForQueryFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.DocumentAvailabilityFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.DocumentEntryTypeFactory;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.Validate.notNull;

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
        
        List<String> slotValues = new ArrayList<>();
        for (Code code : codes) {
            String hl7CE = Hl7v2Based.render(code);
            slotValues.add(encodeAsStringList(hl7CE));
        }
        ebXML.addSlot(param.getSlotName(), slotValues.toArray(new String[slotValues.size()]));
    }

    /**
     * Stores a code list with AND/OR semantics into a set of slots with the same name.
     * @param param
     *          standard query parameter (implies the name of the slots).
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
        
        QueryList<Code> queryList = new QueryList<>();
        for (EbXMLSlot slot : slots) {
            List<Code> innerList = toCode(slot.getValueList());
            queryList.getOuterList().add(innerList);
        }
        return queryList;
    }
    
    /**
     * Retrieves a string list with AND/OR semantics from a set of slots with the same name.
     * @param param
     *          standard query parameter (implies the name of the slots).
     * @return the string list.
     */
    public QueryList<String> toStringQueryList(QueryParameter param) {
        return toStringQueryList(param.getSlotName());
    }

    /**
     * Retrieves a string list with AND/OR semantics from a set of slots with the same name.
     * @param slotName
     *          name of the source slots, may correspond to either
     *          a standard query parameter or an extra parameter.
     * @return the string list.
     */
    public QueryList<String> toStringQueryList(String slotName) {
        if (isEmpty(slotName)) {
            return null;
        }

        List<EbXMLSlot> slots = ebXML.getSlots(slotName);
        if (slots.isEmpty()) {
            return null;
        }
        
        QueryList<String> queryList = new QueryList<>();
        for (EbXMLSlot slot : slots) {
            List<String> innerList = new ArrayList<>();
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
     *          standard query parameter (implies the name of the slots).
     * @param values
     *          the string list.
     */
    public void fromStringList(QueryParameter param, List<String> values) {
        fromStringList(param.getSlotName(), values);
    }

    /**
     * Stores a list of strings into a slot.
     * @param slotName
     *          name of the target slot, may correspond to either
     *          a standard query parameter or an extra parameter.
     * @param values
     *          the string list.
     */
    public void fromStringList(String slotName, List<String> values) {
        if (isEmpty(slotName) || (values == null)) {
            return;
        }
        
        List<String> slotValues = new ArrayList<>();
        for (String value : values) {
            slotValues.add(encodeAsStringList(value));
        }
        ebXML.addSlot(slotName, slotValues.toArray(new String[slotValues.size()]));
    }

    /**
     * Stores a string list with AND/OR semantics into a set of slots with the same name.
     * @param param
     *          standard query parameter (implies the name of the slots).
     * @param queryList
     *          the list of strings.
     */
    public void fromStringList(QueryParameter param, QueryList<String> queryList) {
        fromStringList(param.getSlotName(), queryList);
    }

    /**
     * Stores a string list with AND/OR semantics into a set of slots with the same name.
     * @param slotName
     *          name of the target slots, may correspond to either
     *          a standard query parameter or an extra parameter.
     * @param queryList
     *          the list of strings.
     */
    public void fromStringList(String slotName, QueryList<String> queryList) {
        if (isEmpty(slotName) || (queryList == null)) {
            return;
        }

        for (List<String> list : queryList.getOuterList()) {
            fromStringList(slotName, list);
        }
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
        
        List<String> values = new ArrayList<>();
        for (String slotValue : slotValues) {            
            values.addAll(decodeStringList(slotValue));
        }
        return values;
    }


    /**
     * Stores a list of patientIds into a slot.
     * @param param
     *          the parameter.
     * @param values
     *          the patientId list.
     */
    public void fromPatientIdList(QueryParameter param, List<Identifiable> values) {
        if (values == null) {
            return;
        }

        List<String> slotValues = new ArrayList<>();
        for (Identifiable value : values) {
            slotValues.add(encodeAsStringList(Hl7v2Based.render(value)));
        }
        ebXML.addSlot(param.getSlotName(), slotValues.toArray(new String[slotValues.size()]));
    }

    /**
     * Retrieves a list of strings from a slot.
     * @param param
     *          the parameter.
     * @return the string list.
     */
    public List<Identifiable> toPatientIdList(QueryParameter param) {
        List<String> values = toStringList(param);
        if (values == null) {
            return null;
        }

        List<Identifiable> patientIds = new ArrayList<>();
        for (String value : values) {
            patientIds.add(Hl7v2Based.parse(value, Identifiable.class));
        }
        return patientIds;
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
     * Retrieves a code list with AND/OR semantics from a set of slots with the same name.
     * @param param
     *          standard query parameter (implies the name of the slots).
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
        
        List<String> opcodes = new ArrayList<>(status.size());
        for (AvailabilityStatus statusValue : status) {
            opcodes.add(new AvailabilityStatusForQueryFactory().toEbXML(statusValue));
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

        List<AvailabilityStatus> list = new ArrayList<>();
        for (String opcode : opcodes) {
            list.add(new AvailabilityStatusForQueryFactory().fromEbXML(opcode));
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
        
        List<String> opcodes = new ArrayList<>(associationTypes.size());
        for (AssociationType type : associationTypes) {
            opcodes.add(new AssociationTypeFactory30().toEbXML(type));
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

        List<AssociationType> associationTypes = new ArrayList<>();
        for (String opcode : opcodes) {
            associationTypes.add(new AssociationTypeFactory30().fromEbXML(opcode));
        }
        return associationTypes;
    }

    public void fromDocumentEntryType(QueryParameter param, List<DocumentEntryType> documentEntryTypes) {
        if (documentEntryTypes == null) {
            return;
        }

        List<String> uuids = new ArrayList<>(documentEntryTypes.size());
        for (DocumentEntryType type : documentEntryTypes) {
            uuids.add(new DocumentEntryTypeFactory().toEbXML(type));
        }
        fromStringList(param, uuids);
    }

    public List<DocumentEntryType> toDocumentEntryType(QueryParameter param) {
        List<String> uuids = toStringList(param);
        if (uuids == null) {
            return null;
        }

        ArrayList<DocumentEntryType> documentEntryTypes = new ArrayList<>();
        for (String uuid : uuids) {
            documentEntryTypes.add(new DocumentEntryTypeFactory().fromEbXML(uuid));
        }
        return documentEntryTypes;
    }

    public static List<Code> toCode(List<String> slotValues) {
        if (slotValues.isEmpty()) {
            return null;
        }
        
        List<Code> codes = new ArrayList<>();
        for (String slotValue : slotValues) {
            for (String hl7CE : decodeStringList(slotValue)) {
                Code code = Hl7v2Based.parse(hl7CE, Code.class);
                if (code == null || StringUtils.isEmpty(code.getCode()) || StringUtils.isEmpty(code.getSchemeName())) {
                    throw new XDSMetaDataException(ValidationMessage.INVALID_QUERY_PARAMETER_VALUE, hl7CE);
                }
                codes.add(code);
            }
        }
        return codes;
    }


    /**
     * Stores a status parameter into a slot.
     * @param param
     *          the parameter.
     * @param status
     *          the list of documentAvailability values.
     */
    public void fromDocumentAvailability(QueryParameter param, List<DocumentAvailability> status) {
        if (status == null) {
            return;
        }

        List<String> opcodes = new ArrayList<>(status.size());
        for (DocumentAvailability availabilityValue : status) {
            opcodes.add(new DocumentAvailabilityFactory().toEbXML(availabilityValue));
        }
        fromStringList(param, opcodes);
    }

    /**
     * Retrieves a status parameter from a slot.
     * @param param
     *          the parameter.
     * @return the list of documentAvailability values.
     */
    public List<DocumentAvailability> toDocumentAvailability(QueryParameter param) {
        List<String> opcodes = toStringList(param);
        if (opcodes == null) {
            return null;
        }

        List<DocumentAvailability> list = new ArrayList<>();
        for (String opcode : opcodes) {
            list.add(new DocumentAvailabilityFactory().fromEbXML(opcode));
        }
        return list;
    }

    /**
     * Stores a numbered parameter into a slot.
     * @param param
     *          the parameter.
     * @param value
     *          the value.
     */
    public void fromInteger(QueryParameter param, Integer value) {
        if (value == null){
            return;
        }
        ebXML.addSlot(param.getSlotName(), String.valueOf(value));
    }

    /**
     * Retrieves a numbered parameter from a slot.
     * @param param
     *          the parameter.
     * @return the value.
     */
    public Integer toInteger(QueryParameter param) {
        Integer result = null;
        try {
            String slotValue = ebXML.getSingleSlotValue(param.getSlotName());
            if (StringUtils.isNotBlank(slotValue)) {
                result = Integer.valueOf(slotValue);
            }
        } catch (NumberFormatException nfe){
            // ok, return null
        }
        return result;
    }
    
    public static String encodeAsString(String value) {
        if (value == null) {
            return null;
        }
        return "'" + value.replace("'", "''") + "'";
    }

    public static String decodeString(String value) {
        if (value == null) {
            return null;
        }
        
        if (value.startsWith("'") && value.endsWith("'")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replaceAll("''", "'");
    }

    public static String encodeAsStringList(String value) {
        if (value == null) {
            return null;
        }
        return "('" + value.replace("'", "''") + "')";
    }

    public static List<String> decodeStringList(String list) {
        if (list == null) {
            return null;
        }

        list = list.trim();
        if (list.startsWith("(")) {
            list = list.substring(1);
        }
        if (list.endsWith(")")) {
            list = list.substring(0, list.length() - 1);
        }
        
        List<String> values = new ArrayList<>();
        for (String value: list.split(",")){
            String decodedValue = isNotBlank(value)? decodeString(value.trim()): "";
            values.add(decodedValue);
        }
        return values;
    }
}
