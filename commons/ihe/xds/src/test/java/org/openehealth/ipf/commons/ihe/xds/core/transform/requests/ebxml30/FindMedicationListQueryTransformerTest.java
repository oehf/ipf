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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindMedicationListQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindMedicationListQueryTransformer;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FindMedicationListQueryTransformer}.
 * @author Quentin Ligier
 */
public class FindMedicationListQueryTransformerTest {
    private FindMedicationListQueryTransformer transformer;
    private FindMedicationListQuery query;
    private EbXMLAdhocQueryRequest ebXML;

    @Before
    public void setUp() {
        transformer = new FindMedicationListQueryTransformer();

        query = new FindMedicationListQuery();
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("uni1", "uniType1")));
        query.setHomeCommunityId("12.21.41");
        query.getServiceStart().setFrom("1982");
        query.getServiceStart().setTo("1983");
        query.getServiceEnd().setFrom("1984");
        query.getServiceEnd().setTo("1985");
        query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        query.setFormatCodes(Arrays.asList(new Code("code13", null, "scheme13"), new Code("code14", null, "scheme14")));
        query.setTypeCodes(Arrays.asList(new Code("codet1", null, "schemet1"), new Code("codet2", null, "schemet2")));

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.FIND_MEDICATION_LIST.getId(), ebXML.getId());
        assertEquals("12.21.41", ebXML.getHome());
        assertEquals(Collections.singletonList("'id1^^^&uni1&uniType1'"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()));
        assertEquals(Collections.singletonList("1982"), ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_START_FROM.getSlotName()));
        assertEquals(Collections.singletonList("1983"), ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_START_TO.getSlotName()));
        assertEquals(Collections.singletonList("1984"), ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_END_FROM.getSlotName()));
        assertEquals(Collections.singletonList("1985"), ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_END_TO.getSlotName()));
        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')", "('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_STATUS.getSlotName()));
        assertEquals(Arrays.asList("('code13^^scheme13')", "('code14^^scheme14')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_FORMAT_CODE.getSlotName()));
        assertEquals(Arrays.asList("('codet1^^schemet1')", "('codet2^^schemet2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_TYPE_CODE.getSlotName()));
    }

    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new FindMedicationListQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        FindMedicationListQuery result = new FindMedicationListQuery();
        transformer.fromEbXML(result, ebXML);
        assertEquals(query, result);
    }

    @Test
    public void testFromEbXMLNull() {
        FindMedicationListQuery result = new FindMedicationListQuery();
        transformer.fromEbXML(result, null);
        assertEquals(new FindMedicationListQuery(), result);
    }

    @Test
    public void testFromEbXMLEmpty() {
        FindMedicationListQuery result = new FindMedicationListQuery();
        transformer.fromEbXML(result, ebXML);
        assertEquals(new FindMedicationListQuery(), result);
    }
}
