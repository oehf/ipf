/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsExcludeQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindDocumentsExcludeQueryTransformer;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link FindDocumentsExcludeQueryTransformer}.
 * @author Christian Ohr
 * @since 5.3
 */
public class FindDocumentsExcludeQueryTransformerTest extends AbstractQueryTransformerTest<FindDocumentsExcludeQuery, FindDocumentsExcludeQueryTransformer> {

    @BeforeEach
    public void setUp() {
        transformer = FindDocumentsExcludeQueryTransformer.getInstance();
        query = (FindDocumentsExcludeQuery) SampleData.createFindDocumentsExcludeQuery().getQuery();
        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.FIND_DOCUMENTS_EXCLUDE.getId(), ebXML.getId());
        assertEquals("urn:oid:1.21.41", ebXML.getHome());
        assertEquals(Collections.singletonList("'id3^^^&1.3&ISO'"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()));

        // the excluding parameters carry the "Exclude" suffix (ITI TF-2: 3.18.4.1.2.3.7.15)
        assertEquals("$XDSDocumentEntryClassCodeExclude", QueryParameter.DOC_ENTRY_CLASS_CODE_EXCLUDE.getSlotName());
        assertEquals(List.of("('code1^^scheme1')", "('code2^^scheme2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_CLASS_CODE_EXCLUDE.getSlotName()));
        assertEquals(List.of("('codet1^^schemet1')", "('codet2^^schemet2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_TYPE_CODE_EXCLUDE.getSlotName()));
        assertEquals(List.of("('code13^^scheme13')", "('code14^^scheme14')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_FORMAT_CODE_EXCLUDE.getSlotName()));
        assertEquals(List.of("('per''son1')", "('person2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_AUTHOR_PERSON_EXCLUDE.getSlotName()));
        assertEquals(Collections.singletonList("('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_TYPE_EXCLUDE.getSlotName()));

        // AND/OR semantics are conveyed by one slot per inner list
        var excludedEventCodeSlots = ebXML.getSlots(QueryParameter.DOC_ENTRY_EVENT_CODE_EXCLUDE.getSlotName());
        assertEquals(2, excludedEventCodeSlots.size());
        assertEquals(List.of("('code7^^scheme7')", "('code8^^scheme8')"),
                excludedEventCodeSlots.get(0).getValueList());
        assertEquals(List.of("('code9^^scheme9')"), excludedEventCodeSlots.get(1).getValueList());

        var excludedConfidentialityCodeSlots =
                ebXML.getSlots(QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE_EXCLUDE.getSlotName());
        assertEquals(2, excludedConfidentialityCodeSlots.size());
        assertEquals(List.of("('code10^^scheme10')", "('code11^^scheme11')"),
                excludedConfidentialityCodeSlots.get(0).getValueList());
        assertEquals(List.of("('code12^^scheme12')"), excludedConfidentialityCodeSlots.get(1).getValueList());

        var excludedReferenceIdSlots = ebXML.getSlots(QueryParameter.DOC_ENTRY_REFERENCE_IDS_EXCLUDE.getSlotName());
        assertEquals(2, excludedReferenceIdSlots.size());
        assertEquals(List.of("('ref-id-11')", "('ref-id-12')"), excludedReferenceIdSlots.get(0).getValueList());
        assertEquals(List.of("('ref-id-21')"), excludedReferenceIdSlots.get(1).getValueList());
    }

    /**
     * The excluded type codes must not be read back from $XDSDocumentEntryTypeExclude,
     * which conveys the object type rather than a code.
     */
    @Test
    public void testExcludedTypeCodesAndDocumentEntryTypesDoNotOverlap() {
        query.setExcludedTypeCodes(Collections.singletonList(new Code("codet1", null, "schemet1")));
        query.setExcludedDocumentEntryTypes(Collections.singletonList(DocumentEntryType.ON_DEMAND));
        transformer.toEbXML(query, ebXML);

        var result = emptyQuery();
        transformer.fromEbXML(result, ebXML);

        assertEquals(Collections.singletonList(new Code("codet1", null, "schemet1")), result.getExcludedTypeCodes());
        assertEquals(Collections.singletonList(DocumentEntryType.ON_DEMAND), result.getExcludedDocumentEntryTypes());
    }

    /**
     * The non-excluding reference IDs are a parameter of this query as well, and are
     * mutually exclusive with the excluding ones.
     */
    @Test
    public void testReferenceIdsRoundTrip() {
        var referenceIds = new QueryList<String>();
        referenceIds.getOuterList().add(List.of("ref-id-31", "ref-id-32"));

        query.setExcludedReferenceIds(null);
        query.setReferenceIds(referenceIds);
        transformer.toEbXML(query, ebXML);

        assertEquals(List.of("('ref-id-31')", "('ref-id-32')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_REFERENCE_IDS.getSlotName()));

        var result = emptyQuery();
        transformer.fromEbXML(result, ebXML);
        assertEquals(query, result);
    }

    @Override
    protected FindDocumentsExcludeQuery emptyQuery() {
        return new FindDocumentsExcludeQuery();
    }
}
