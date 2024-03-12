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

import ca.uhn.hl7v2.model.Composite;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.Validate.noNullElements;
import static java.util.Objects.requireNonNull;

/**
 * Wrapper class for ebXML query request to simplify access to slots.
 * <p>
 * This class ensures that the various encoding rules of query parameter
 * values are met.
 * <p>
 * Note that this class is only used for ebXML 3.0!
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class QuerySlotHelper {

    private final EbXMLAdhocQueryRequest<AdhocQueryRequest> ebXML;

    /**
     * Constructs the wrapper.
     *
     * @param ebXML the wrapped object.
     */
    public QuerySlotHelper(EbXMLAdhocQueryRequest<AdhocQueryRequest> ebXML) {
        this.ebXML = requireNonNull(ebXML, "ebXML cannot be null");
    }

    public List<EbXMLSlot> getSlots() {
        return ebXML.getSlots();
    }

    /**
     * Retrieves a string-valued parameter from a slot.
     *
     * @param param the parameter.
     * @return the string value.
     */
    public String toString(QueryParameter param) {
        var value = ebXML.getSingleSlotValue(param.getSlotName());
        return decodeString(value);
    }

    /**
     * Stores a string-valued parameter into a slot.
     *
     * @param param the parameter.
     * @param value the string value.
     */
    public void fromString(QueryParameter param, String value) {
        if (value != null) {
            ebXML.addSlot(param.getSlotName(), encodeAsString(value));
        }
    }

    /**
     * Stores a list of codes into a slot.
     *
     * @param param the parameter.
     * @param codes the list of codes.
     */
    public void fromCode(QueryParameter param, List<Code> codes) {
        if (codes == null) {
            return;
        }

        ebXML.addSlot(param.getSlotName(), codes.stream()
                .map(Hl7v2Based::render)
                .map(QuerySlotHelper::encodeAsStringList)
                .toArray(String[]::new));
    }

    /**
     * Stores a code list with AND/OR semantics into a set of slots with the same name.
     *
     * @param param     standard query parameter (implies the name of the slots).
     * @param queryList the list of codes.
     */
    public void fromCode(QueryParameter param, QueryList<Code> queryList) {
        if (queryList == null) {
            return;
        }
        for (var codes : queryList.getOuterList()) {
            fromCode(param, codes);
        }
    }

    private QueryList<Code> toCodeQueryList(QueryParameter param) {
        var slots = ebXML.getSlots(param.getSlotName());
        if (slots.isEmpty()) {
            return null;
        }
        var queryList = new QueryList<Code>();
        slots.stream()
                .map(slot -> toCode(slot.getValueList()))
                .forEach(innerList -> queryList.getOuterList().add(innerList));
        return queryList;
    }

    /**
     * Retrieves a string list with AND/OR semantics from a set of slots with the same name.
     *
     * @param param standard query parameter (implies the name of the slots).
     * @return the string list.
     */
    public QueryList<String> toStringQueryList(QueryParameter param) {
        return toStringQueryList(param.getSlotName());
    }

    /**
     * Retrieves a string list with AND/OR semantics from a set of slots with the same name.
     *
     * @param slotName name of the source slots, may correspond to either
     *                 a standard query parameter or an extra parameter.
     * @return the string list.
     */
    public QueryList<String> toStringQueryList(String slotName) {
        if (isEmpty(slotName)) {
            return null;
        }
        var slots = ebXML.getSlots(slotName);
        if (slots.isEmpty()) {
            return null;
        }
        var queryList = new QueryList<String>();
        slots.forEach(slot -> {
            var innerList = new ArrayList<String>();
            for (var slotValue : slot.getValueList()) {
                innerList.addAll(decodeStringList(slotValue));
            }
            queryList.getOuterList().add(innerList);
        });
        return queryList;
    }

    /**
     * Stores a list of strings into a slot.
     *
     * @param param  standard query parameter (implies the name of the slots).
     * @param values the string list.
     */
    public void fromStringList(QueryParameter param, List<String> values) {
        fromStringList(param.getSlotName(), values);
    }

    /**
     * Stores a list of strings into a slot.
     *
     * @param slotName name of the target slot, may correspond to either
     *                 a standard query parameter or an extra parameter.
     * @param values   the string list.
     */
    public void fromStringList(String slotName, List<String> values) {
        if (isEmpty(slotName) || (values == null)) {
            return;
        }
        var slotValues = values.stream()
                .map(QuerySlotHelper::encodeAsStringList)
                .toArray(String[]::new);
        ebXML.addSlot(slotName, slotValues);
    }

    /**
     * Stores a string list with AND/OR semantics into a set of slots with the same name.
     *
     * @param param     standard query parameter (implies the name of the slots).
     * @param queryList the list of strings.
     */
    public void fromStringList(QueryParameter param, QueryList<String> queryList) {
        fromStringList(param.getSlotName(), queryList);
    }

    /**
     * Stores a string list with AND/OR semantics into a set of slots with the same name.
     *
     * @param slotName  name of the target slots, may correspond to either
     *                  a standard query parameter or an extra parameter.
     * @param queryList the list of strings.
     */
    public void fromStringList(String slotName, QueryList<String> queryList) {
        if (isEmpty(slotName) || (queryList == null)) {
            return;
        }
        queryList.getOuterList().forEach(list -> fromStringList(slotName, list));
    }

    /**
     * Retrieves a list of strings from a slot.
     *
     * @param param the parameter.
     * @return the string list.
     */
    public List<String> toStringList(QueryParameter param) {
        var slotValues = ebXML.getSlotValues(param.getSlotName());
        if (slotValues.isEmpty()) {
            return null;
        }
        return slotValues.stream()
                .flatMap(slotValue -> decodeStringList(slotValue).stream())
                .collect(Collectors.toList());
    }


    /**
     * Stores a list of patient IDs into a slot.
     *
     * @param param  the parameter.
     * @param values the patient ID list.
     */
    public void fromPatientIdList(QueryParameter param, List<Identifiable> values) {
        if (values == null) {
            return;
        }
        var slotValues = values.stream()
                .map(value -> encodeAsStringList(Hl7v2Based.render(value)))
                .toArray(String[]::new);
        ebXML.addSlot(param.getSlotName(), slotValues);
    }

    /**
     * Retrieves a list of patient IDs from a slot.
     *
     * @param param the parameter.
     * @return the patient ID list.
     */
    public List<Identifiable> toPatientIdList(QueryParameter param) {
        var values = toStringList(param);
        if (values == null) {
            return null;
        }
        return values.stream()
                .map(value -> Hl7v2Based.parse(value, Identifiable.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of codes from a slot.
     *
     * @param param the parameter.
     * @return the codes.
     */
    public List<Code> toCodeList(QueryParameter param) {
        return toCode(ebXML.getSlotValues(param.getSlotName()));
    }

    /**
     * Retrieves a code list with AND/OR semantics from a set of slots with the same name.
     *
     * @param param       standard query parameter (implies the name of the slots).
     * @param schemeParam the code scheme parameter.
     * @return the codes.
     */
    public QueryList<Code> toCodeQueryList(QueryParameter param, QueryParameter schemeParam) {
        var codes = toCodeQueryList(param);
        if (codes == null) {
            return null;
        }
        var schemes = toStringQueryList(schemeParam);
        if (schemes != null) {
            var schemesOuter = schemes.getOuterList();
            var codesOuter = codes.getOuterList();
            for (var outer = 0; outer < schemesOuter.size() && outer < codesOuter.size(); ++outer) {
                var schemesInner = schemesOuter.get(outer);
                var codesInner = codesOuter.get(outer);
                for (var inner = 0; inner < schemesInner.size() && inner < codesInner.size(); ++inner) {
                    codesInner.get(inner).setSchemeName(schemesInner.get(inner));
                }
            }
        }
        return codes;
    }


    /**
     * Stores a timestamp parameter into a slot.
     *
     * @param param the parameter.
     * @param value the value.
     */
    public void fromTimestamp(QueryParameter param, Timestamp value) {
        ebXML.addSlot(param.getSlotName(), Timestamp.toHL7(value));
    }

    /**
     * Retrieves a timestamp parameter from a slot.
     * According to CP-ITI-1260, a timestamp parameter may be enclosed in single quotes.
     *
     * @param param the parameter.
     * @return the value.
     */
    public String toTimestamp(QueryParameter param) {
        var value = ebXML.getSingleSlotValue(param.getSlotName());
        return decodeString(value);
    }

    /**
     * Stores a status parameter into a slot.
     *
     * @param param  the parameter.
     * @param status the list of status values.
     */
    public void fromStatus(QueryParameter param, List<AvailabilityStatus> status) {
        if (status == null) {
            return;
        }
        var opcodes = status.stream()
                .map(AvailabilityStatus::toQueryOpcode)
                .collect(Collectors.toList());
        fromStringList(param, opcodes);
    }

    /**
     * Retrieves a status parameter from a slot.
     *
     * @param param the parameter.
     * @return the list of status values.
     */
    public List<AvailabilityStatus> toStatus(QueryParameter param) {
        var opcodes = toStringList(param);
        if (opcodes == null) {
            return null;
        }
        return opcodes.stream()
                .map(AvailabilityStatus::valueOfOpcode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Stores an association parameter into a slot.
     *
     * @param param            the parameter.
     * @param associationTypes the list of association types.
     */
    public void fromAssociationType(QueryParameter param, List<AssociationType> associationTypes) {
        if (associationTypes == null) {
            return;
        }
        var opcodes = associationTypes.stream()
                .map(type -> AssociationType.getOpcode30(type))
                .collect(Collectors.toList());
        fromStringList(param, opcodes);
    }

    /**
     * Retrieves an association parameter from a slot.
     *
     * @param param the parameter.
     * @return the list of association types.
     */
    public List<AssociationType> toAssociationType(QueryParameter param) {
        var opcodes = toStringList(param);
        if (opcodes == null) {
            return null;
        }
        return opcodes.stream()
                .map(AssociationType::valueOfOpcode30)
                .collect(Collectors.toList());
    }

    public void fromDocumentEntryType(QueryParameter param, List<DocumentEntryType> documentEntryTypes) {
        if (documentEntryTypes == null) {
            return;
        }
        var uuids = documentEntryTypes.stream()
                .map(DocumentEntryType::toUuid)
                .collect(Collectors.toList());
        fromStringList(param, uuids);
    }

    public List<DocumentEntryType> toDocumentEntryType(QueryParameter param) {
        var uuids = toStringList(param);
        if (uuids == null) {
            return null;
        }
        return uuids.stream()
                .map(DocumentEntryType::valueOfUuid)
                .collect(Collectors.toList());
    }

    public static List<Code> toCode(List<String> slotValues) {
        if (slotValues.isEmpty()) {
            return null;
        }
        var codes = new ArrayList<Code>();
        slotValues.forEach(slotValue ->
                decodeStringList(slotValue).forEach(hl7CE -> {
                    var code = Hl7v2Based.parse(hl7CE, Code.class);
                    if (code == null || isEmpty(code.getCode()) || isEmpty(code.getSchemeName())) {
                        throw new XDSMetaDataException(ValidationMessage.INVALID_QUERY_PARAMETER_VALUE, hl7CE);
                    }
                    codes.add(code);
                }));
        return codes;
    }

    /**
     * Stores a status parameter into a slot.
     *
     * @param param  the parameter.
     * @param status the list of documentAvailability values.
     */
    public void fromDocumentAvailability(QueryParameter param, List<DocumentAvailability> status) {
        if (status == null) {
            return;
        }
        var opcodes = status.stream()
                .map(DocumentAvailability::toFullQualifiedOpcode)
                .collect(Collectors.toList());
        fromStringList(param, opcodes);
    }

    /**
     * Retrieves a status parameter from a slot.
     *
     * @param param the parameter.
     * @return the list of documentAvailability values.
     */
    public List<DocumentAvailability> toDocumentAvailability(QueryParameter param) {
        var opcodes = toStringList(param);
        if (opcodes == null) {
            return null;
        }
        return opcodes.stream()
                .map(DocumentAvailability::valueOfOpcode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Stores a numbered parameter into a slot.
     *
     * @param param the parameter.
     * @param value the value.
     */
    public void fromInteger(QueryParameter param, Integer value) {
        if (value == null) {
            return;
        }
        ebXML.addSlot(param.getSlotName(), String.valueOf(value));
    }

    /**
     * Retrieves a numbered parameter from a slot.
     *
     * @param param the parameter.
     * @return the value.
     */
    public Integer toInteger(QueryParameter param) {
        try {
            var slotValue = ebXML.getSingleSlotValue(param.getSlotName());
            if (StringUtils.isNotBlank(slotValue)) {
                return Integer.valueOf(slotValue);
            }
        } catch (NumberFormatException nfe) {
            // ok, return null
        }
        return null;
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
        return value.replace("''", "'");
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
        String trimmed = list.trim();
        if (trimmed.startsWith("(")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith(")")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return Arrays.stream(trimmed.split(","))
                .map(value -> isNotBlank(value) ? decodeString(value.trim()) : "")
                .collect(Collectors.toList());
    }

    public static <T extends Hl7v2Based<?>> QueryList<String> render(QueryList<T> source) {
        if (source == null) {
            return null;
        }
        noNullElements(source.getOuterList(), "outer list cannot contain NULL elements");
        var target = new QueryList<String>();
        source.getOuterList().forEach(list -> target.getOuterList().add(render(list)));
        return target;
    }

    public static <T extends Hl7v2Based<?>> List<String> render(List<T> source) {
        if (source == null) {
            return null;
        }
        noNullElements(source, "list cannot contain NULL elements");
        return source.stream()
                .map(Hl7v2Based::render)
                .collect(Collectors.toList());
    }

    public static <C extends Composite, T extends Hl7v2Based<C>> QueryList<T> parse(
            QueryList<String> source,
            Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        noNullElements(source.getOuterList(), "outer list cannot contain NULL elements");
        var target = new QueryList<T>();
        source.getOuterList().forEach(list -> target.getOuterList().add(parse(list, targetClass)));
        return target;
    }

    public static <C extends Composite, T extends Hl7v2Based<C>> List<T> parse(
            List<String> source,
            Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        noNullElements(source, "list cannot contain NULL elements");
        return source.stream()
                .map(value -> Hl7v2Based.parse(value, targetClass))
                .collect(Collectors.toList());
    }

}
