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
package org.openehealth.ipf.commons.ihe.xds.core.validate.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SqlQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.IheProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;

import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link AdhocQueryRequestValidator}.
 * @author Jens Riemschneider
 */
public class AdhocQueryRequestValidatorTest {
    private AdhocQueryRequestValidator validator;
    private QueryRegistry request;
    private QueryRegistryTransformer transformer;
    private ValidationProfile profile;

    @Before
    public void setUp() {
        validator = new AdhocQueryRequestValidator();
        transformer = new QueryRegistryTransformer();
        request = SampleData.createFindDocumentsQuery();
        profile = new ValidationProfile();
        profile.setIheProfile(IheProfile.XDS_B);
    }
    
    @Test
    public void testGoodCase() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(request), profile);
    }
    
    @Test
    public void testUnknownReturnType() {        
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.setReturnType("lol");
        expectFailure(UNKNOWN_RETURN_TYPE, ebXML);
    }
    
    @Test
    public void testMissingRequiredQueryParameter() {
        ((FindDocumentsQuery)request.getQuery()).setPatientId(null);
        expectFailure(MISSING_REQUIRED_QUERY_PARAMETER);
    }
        
    @Test
    public void testTooManyQueryParameterValues() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()).get(0).getValueList().add("'lol'");
        expectFailure(TOO_MANY_VALUES_FOR_QUERY_PARAMETER, ebXML);
    }
    
    @Test
    public void testParameterValueNotString() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()).get(0).getValueList().set(0, "lol");
        expectFailure(PARAMETER_VALUE_NOT_STRING, ebXML);
    }
    
    @Test
    public void testCodeListNotEnoughSchemes() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().add("('code^^')");
        expectFailure(INVALID_QUERY_PARAMETER_VALUE, ebXML);
    }

    @Test
    public void testCodeListOldStyleCorrect() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().set(0, "('code1')");
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().set(1, "('code2')");
        ebXML.addSlot(QueryParameter.DOC_ENTRY_CLASS_CODE_SCHEME.getSlotName(), "('scheme1','scheme2')");
        validator.validate(transformer.toEbXML(request), profile);
    }

    @Test
    public void testCodeListOldStyleNotEnoughSchemes() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().set(0, "('code1')");
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().set(1, "('code2')");
        ebXML.addSlot(QueryParameter.DOC_ENTRY_CLASS_CODE_SCHEME.getSlotName(), "('scheme1')");
        expectFailure(INVALID_QUERY_PARAMETER_VALUE, ebXML);
    }

    @Test
    public void testUnknownStatusCodes() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        List<String> valueList = ebXML.getSlots(QueryParameter.DOC_ENTRY_STATUS.getSlotName()).get(0).getValueList();

        // no codes at all -- should fail
        valueList.clear();
        expectFailure(MISSING_REQUIRED_QUERY_PARAMETER, ebXML);

        // only unknown codes -- should fail
        valueList.add("('lol')");
        valueList.add("('foo')");
        expectFailure(MISSING_REQUIRED_QUERY_PARAMETER, ebXML);

        // at least one code -- should pass
        valueList.set(0, "('bar')");
        valueList.set(1, "('Approved')");
        validator.validate(ebXML, profile);
    }
    
    @Test
    public void testQueryParametersCannotBeSetTogether() {
        request = SampleData.createGetDocumentsQuery();        
        ((GetDocumentsQuery)request.getQuery()).setUniqueIds(Collections.singletonList("1.2.3"));
        expectFailure(QUERY_PARAMETERS_CANNOT_BE_SET_TOGETHER);
    }
    
    @Test
    public void testGoodCaseSql() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(SampleData.createSqlQuery()), profile);
    }
    
    @Test
    public void testMissingSqlQuery() {
        request = SampleData.createSqlQuery();
        ((SqlQuery)request.getQuery()).setSql(null);
        expectFailure(MISSING_SQL_QUERY_TEXT);
    }
    
    @Test
    public void testUnknownQueryType() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.setId("lol");
        expectFailure(UNKNOWN_QUERY_TYPE, ebXML);        
    }

    @Test
    public void testHomeCommunityIdAttributeValidation() {
        request = SampleData.createGetDocumentsQuery();
        // without prefix
        ((GetDocumentsQuery)request.getQuery()).setHomeCommunityId("1.2.3");
        expectFailure(INVALID_OID);
        // wrong suffix
        ((GetDocumentsQuery)request.getQuery()).setHomeCommunityId("urn:oid:foo");
        expectFailure(INVALID_OID);
    }

    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(request));
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLAdhocQueryRequest request) {
        try {
            validator.validate(request, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
