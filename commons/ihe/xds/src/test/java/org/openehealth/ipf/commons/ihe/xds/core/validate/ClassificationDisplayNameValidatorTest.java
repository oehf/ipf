/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_EVENT_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage.OPTIONAL;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage.REQUIRED;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ProvideAndRegisterDocumentSetTransformer;

/**
 * Tests the display name validation for XDS.b. 
 * 
 * @author Mitko Kolev
 */
public class ClassificationDisplayNameValidatorTest {


    private static final SlotValueValidation[] NO_SLOT_VALUE_VALIDATION = new SlotValueValidation[] {};

    private EbXMLProvideAndRegisterDocumentSetRequest ebXMLObject;
    private EbXMLExtrinsicObject extrinsicObject;

    @Before
    public void setUp() throws Exception {
        ebXMLObject = createProvideAndRegisterDocumentSetRequest();
        extrinsicObject = ebXMLObject.getExtrinsicObjects().get(0);
        //marshalEbXML(ebXMLObject);
    }

    @Test
    public void testValidateClassificationHappyCase() throws XDSMetaDataException {
        doValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME, REQUIRED);
    }
    
    @Test
    public void testValidateClassificationNamePresentAndOptional() throws XDSMetaDataException {
        doValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME, OPTIONAL);
    }
    
    @Test
    public void testValidateClassificationNoNameRequiredButNamePresent() throws XDSMetaDataException {
        clearNameInFirstClassification(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);

        doValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME, OPTIONAL);
    }

    @Test(expected = XDSMetaDataException.class)
    public void testValidateClassificationNameRequiredButNotAvailable() throws XDSMetaDataException {
        clearNameInFirstClassification(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);
        
        doValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME, REQUIRED);
    }
    
    
    @Test
    public void testEventCodeClassificationNameExists() throws XDSMetaDataException {
        ClassificationValidation validator = buildEventListValidator();
        validator.validate(extrinsicObject);
    }
    
    
    @Test(expected = XDSMetaDataException.class)
    public void testEventCodeClassificationNameDoesNotExist() throws XDSMetaDataException {
        clearNameInFirstClassification(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME);

        ClassificationValidation validator = buildEventListValidator();
        validator.validate(extrinsicObject);
    }
    
    
    private void doValidation(String classScheme, DisplayNameUsage displayNameUsage){
        ClassificationValidation validator = new ClassificationValidation(classScheme,
                                                                          displayNameUsage,
                                                                          NO_SLOT_VALUE_VALIDATION);
        validator.validate(extrinsicObject);
    }
    
    private void clearNameInFirstClassification(String classScheme){
        extrinsicObject.getClassifications(classScheme).get(0).setName(null);
    }
    
    private ClassificationValidation buildEventListValidator(){
        SlotValueValidation [] eventCodeListValidator = new SlotValueValidation [] {  new EventCodeListDisplayNameValidator()};
        ClassificationValidation validator = new ClassificationValidation(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME,
                                                                           OPTIONAL,
                                                                           eventCodeListValidator);
        return validator;
    }
    
    
    private EbXMLProvideAndRegisterDocumentSetRequest createProvideAndRegisterDocumentSetRequest() throws Exception {
        EbXMLFactory factory = new EbXMLFactory30();
        ProvideAndRegisterDocumentSet request = SampleData.createProvideAndRegisterDocumentSet();
        ProvideAndRegisterDocumentSetTransformer transformer = new ProvideAndRegisterDocumentSetTransformer(factory);
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        return ebXML;
    }
    /**
     * Helper method to print the content of a EbXMLProvideAndRegisterDocumentSetRequest. Can be used to understand the test.
     * @param ebXML
     */
    @SuppressWarnings("unused")
    private void marshalEbXML(EbXMLProvideAndRegisterDocumentSetRequest ebXML) {
        try {
            JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs");
            Marshaller marshaller = context.createMarshaller();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ProvideAndRegisterDocumentSetRequestType request = (ProvideAndRegisterDocumentSetRequestType) ebXML.getInternal();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(request.getSubmitObjectsRequest(), outputStream);
            System.out.println(new String(outputStream.toByteArray()));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
