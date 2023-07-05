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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ProvideAndRegisterDocumentSetTransformer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage.OPTIONAL;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage.REQUIRED;

/**
 * Tests the display name validation for XDS.b. 
 * 
 * @author Mitko Kolev
 */
public class ClassificationDisplayNameValidatorTest {

    private static final SlotValueValidation[] NO_SLOT_VALUE_VALIDATION = new SlotValueValidation[] {};

    private EbXMLExtrinsicObject extrinsicObject;

    @BeforeEach
    public void setUp() {
        var ebXMLObject = createProvideAndRegisterDocumentSetRequest();
        extrinsicObject = ebXMLObject.getExtrinsicObjects().get(0);
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

    @Test
    public void testValidateClassificationNameRequiredButNotAvailable() throws XDSMetaDataException {
        clearNameInFirstClassification(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);
        Assertions.assertThrows(XDSMetaDataException.class,
                () -> doValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME, REQUIRED));
    }
    
    
    @Test
    public void testEventCodeClassificationNameExists() throws XDSMetaDataException {
        var validator = buildEventListValidator();
        validator.validate(extrinsicObject);
    }
    
    
    private void doValidation(String classScheme, DisplayNameUsage displayNameUsage){
        var validator = new ClassificationValidation(classScheme, 1, 1, displayNameUsage,
                Vocabulary.NodeRepresentationUsage.REQUIRED, NO_SLOT_VALUE_VALIDATION);
        validator.validate(extrinsicObject);
    }
    
    private void clearNameInFirstClassification(String classScheme){
        extrinsicObject.getClassifications(classScheme).get(0).setName(null);
    }
    
    private ClassificationValidation buildEventListValidator(){
        var eventCodeListValidator =
                new SlotValueValidation [] {new SlotValueValidation(SLOT_NAME_CODING_SCHEME, new NopValidator())};
        return new ClassificationValidation(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME, 1, 1,
                OPTIONAL, Vocabulary.NodeRepresentationUsage.REQUIRED, eventCodeListValidator);
    }
    
    
    private EbXMLProvideAndRegisterDocumentSetRequest<ProvideAndRegisterDocumentSetRequestType> createProvideAndRegisterDocumentSetRequest() {
        EbXMLFactory factory = new EbXMLFactory30();
        var request = SampleData.createProvideAndRegisterDocumentSet();
        var transformer = new ProvideAndRegisterDocumentSetTransformer(factory);
        return transformer.toEbXML(request);
    }
    /**
     * Helper method to print the content of a EbXMLProvideAndRegisterDocumentSetRequest. Can be used to understand the test.
     * @param ebXML ebXML object
     */
    @SuppressWarnings("unused")
    private void marshalEbXML(EbXMLProvideAndRegisterDocumentSetRequest<ProvideAndRegisterDocumentSetRequestType> ebXML) {
        try {
            var context = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs");
            var marshaller = context.createMarshaller();
            var outputStream = new ByteArrayOutputStream();
            var request = ebXML.getInternal();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(request.getSubmitObjectsRequest(), outputStream);
            // System.out.println(new String(outputStream.toByteArray()));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
