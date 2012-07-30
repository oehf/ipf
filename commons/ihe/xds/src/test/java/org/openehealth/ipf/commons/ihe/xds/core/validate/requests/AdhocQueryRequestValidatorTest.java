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
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.*;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;

import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link AdhocQueryRequestValidator}.
 * @author Jens Riemschneider
 * @author Michael Ottati
 */
public class AdhocQueryRequestValidatorTest {
    private AdhocQueryRequestValidator validator;
    private QueryRegistry request, requestMpq, folderRequest, folderRequestMpq;
    private QueryRegistryTransformer transformer;
    private ValidationProfile iti16Profile, iti18Profile, iti51Profile;

    @Before
    public void setUp() {
        validator = new AdhocQueryRequestValidator();
        transformer = new QueryRegistryTransformer();
        request = SampleData.createFindDocumentsQuery();
        requestMpq = SampleData.createFindDocumentsForMultiplePatientsQuery();
        folderRequest = SampleData.createFindFoldersQuery();
        folderRequestMpq = SampleData.createFindFoldersForMultiplePatientsQuery();

        iti16Profile = new ValidationProfile(IpfInteractionId.ITI_16);
        iti18Profile = new ValidationProfile(IpfInteractionId.ITI_18);
        iti51Profile = new ValidationProfile(IpfInteractionId.ITI_51);
    }
    
    @Test
    public void testGoodCase() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(request), iti18Profile);
    }
    
    @Test
    public void testUnknownReturnType() {        
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.setReturnType("lol");
        expectFailure(UNKNOWN_RETURN_TYPE, ebXML, iti18Profile);
    }

    @Test
    public void testMissingRequiredQueryParameter() {
        ((FindDocumentsQuery)request.getQuery()).setPatientId(null);
        expectFailure(MISSING_REQUIRED_QUERY_PARAMETER, iti18Profile);
    }
        
    @Test
    public void testTooManyQueryParameterValues() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()).get(0).getValueList().add("'lol'");
        expectFailure(TOO_MANY_VALUES_FOR_QUERY_PARAMETER, ebXML, iti18Profile);
    }

    @Test
    public void testParameterValueNotString() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()).get(0).getValueList().set(0, "lol");
        expectFailure(PARAMETER_VALUE_NOT_STRING, ebXML, iti18Profile);
    }

    @Test
    public void testParameterValueNotStringList() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().add("lol");
        expectFailure(PARAMETER_VALUE_NOT_STRING_LIST, ebXML, iti18Profile);
    }

    @Test
    public void testCodeListNotEnoughSchemes() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().add("('code^^')");
        expectFailure(INVALID_QUERY_PARAMETER_VALUE, ebXML, iti18Profile);
    }

    @Test
    public void testCodeListOldStyleCorrect() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().set(0, "('code1')");
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().set(1, "('code2')");
        ebXML.addSlot(QueryParameter.DOC_ENTRY_CLASS_CODE_SCHEME.getSlotName(), "('scheme1','scheme2')");
        validator.validate(transformer.toEbXML(request), iti18Profile);
    }

    @Test
    public void testCodeListOldStyleNotEnoughSchemes() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().set(0, "('code1')");
        ebXML.getSlots(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()).get(0).getValueList().set(1, "('code2')");
        ebXML.addSlot(QueryParameter.DOC_ENTRY_CLASS_CODE_SCHEME.getSlotName(), "('scheme1')");
        expectFailure(INVALID_QUERY_PARAMETER_VALUE, ebXML, iti18Profile);
    }

    @Test
    public void testUnknownStatusCodes() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        List<String> valueList = ebXML.getSlots(QueryParameter.DOC_ENTRY_STATUS.getSlotName()).get(0).getValueList();

        // no codes at all -- should fail
        valueList.clear();
        expectFailure(MISSING_REQUIRED_QUERY_PARAMETER, ebXML, iti18Profile);

        // only unknown codes -- should fail
        valueList.add("('lol')");
        valueList.add("('foo')");
        expectFailure(MISSING_REQUIRED_QUERY_PARAMETER, ebXML, iti18Profile);

        // at least one code -- should pass
        valueList.set(0, "('bar')");
        valueList.set(1, "('Approved')");
        validator.validate(ebXML, iti18Profile);
    }
    
    @Test
    public void testQueryParametersCannotBeSetTogether() {
        request = SampleData.createGetDocumentsQuery();        
        ((GetDocumentsQuery)request.getQuery()).setUniqueIds(Collections.singletonList("1.2.3"));
        expectFailure(QUERY_PARAMETERS_CANNOT_BE_SET_TOGETHER, iti18Profile);
    }

    /*
        The validation profiles for ITI-18 and ITI-51 are identical except for how they handle
        PatientId data. The ITI-18 queries MUST contain exactly 1 PatientId. The ITI-51 Mutli
        Patient Queries MAY contain a (possibly empty) list of patient IDs. The Following set
        of tests suffixed with "MPQ" test the MPQ PatientId validation code.
    */

    @Test
    public void testGoodCaseMPQ() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(requestMpq), iti51Profile);
    }

    @Test
    public void testMissingPatientIdsMPQ() {
        ((FindDocumentsForMultiplePatientsQuery) requestMpq.getQuery()).setPatientIds(null);
        validator.validate(transformer.toEbXML(requestMpq), iti51Profile);
    }

    @Test
    public void testPatientIdMustBeISO_MPQ() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(requestMpq);
        ebXML.getSlots(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()).get(0).getValueList().add("('Invalid ISO Patient ID')");
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXML, iti51Profile);
    }

    // Folder and FolderMPQ test cases
    @Test
    public void testGoodCaseFolder() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(folderRequest), iti18Profile);
    }

    @Test
    public void testMissingPatientIdFolder() throws XDSMetaDataException {
        ((FindFoldersQuery)folderRequest.getQuery()).setPatientId(null);
        expectFailure(MISSING_REQUIRED_QUERY_PARAMETER,transformer.toEbXML(folderRequest), iti18Profile );
    }

    @Test
    public void testMissingPatientIdFolderMPQ() {
        ((FindFoldersForMultiplePatientsQuery) folderRequestMpq.getQuery()).setPatientIds(null);
        validator.validate(transformer.toEbXML(folderRequestMpq), iti51Profile);

    }

    @Test
    public void testGoodCaseFolderMPQ() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(folderRequestMpq), iti51Profile);
    }

    @Test
    public void testPatientIdMustBeISOFolder_MPQ() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(folderRequestMpq);
        ebXML.getSlots(QueryParameter.FOLDER_PATIENT_ID.getSlotName()).get(0).getValueList().add("('Invalid ISO Patient ID')");
        expectFailure(UNIVERSAL_ID_TYPE_MUST_BE_ISO, ebXML, iti51Profile);
    }

    @Test
    public void testGoodCaseSql() throws XDSMetaDataException {
        validator.validate(transformer.toEbXML(SampleData.createSqlQuery()), iti16Profile);
    }
    
    @Test
    public void testMissingSqlQuery() {
        request = SampleData.createSqlQuery();
        ((SqlQuery)request.getQuery()).setSql(null);
        expectFailure(MISSING_SQL_QUERY_TEXT, iti16Profile);
    }
    
    @Test
    public void testUnknownQueryType() {
        EbXMLAdhocQueryRequest ebXML = transformer.toEbXML(request);
        ebXML.setId("lol");
        expectFailure(UNKNOWN_QUERY_TYPE, ebXML, iti18Profile);
    }

    @Test
    public void testHomeCommunityIdAttributeValidation() {
        request = SampleData.createGetDocumentsQuery();
        // without prefix
        ((GetDocumentsQuery)request.getQuery()).setHomeCommunityId("1.2.3");
        expectFailure(INVALID_OID, iti18Profile);
        // wrong suffix
        ((GetDocumentsQuery)request.getQuery()).setHomeCommunityId("urn:oid:foo");
        expectFailure(INVALID_OID, iti18Profile);
    }

    private void expectFailure(ValidationMessage expectedMessage, ValidationProfile profile) {
        expectFailure(expectedMessage, transformer.toEbXML(request), profile);
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLAdhocQueryRequest request, ValidationProfile profile) {
        try {
            validator.validate(request, profile);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}
