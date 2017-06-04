/*
 * Copyright 2017 the original author or authors.
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

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.XDM;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RegisterDocumentSetTransformer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.UUID;

/**
 * @author Dmytro Rud
 */
@Slf4j
public class LimitedMetadataTest {

    private RegisterDocumentSet createXdmRequest() {
        DocumentEntry documentEntry1 = new DocumentEntry();
        documentEntry1.setLimitedMetadata(true);
        documentEntry1.setEntryUuid(UUID.randomUUID().toString());
        documentEntry1.setHash("01234567890ABCDEF01234567890ABCDEF012345");
        documentEntry1.setMimeType("text/xml");
        documentEntry1.setSize(7777L);
        documentEntry1.setUniqueId("1.2.3.4.5.111");
        documentEntry1.setUri("CDA-1.XML");

        DocumentEntry documentEntry2 = new DocumentEntry();
        documentEntry2.setLimitedMetadata(true);
        documentEntry2.setEntryUuid(UUID.randomUUID().toString());
        documentEntry2.setHash("ABCDEF0123456789ABCDEF0123456789ABCDEF01");
        documentEntry2.setMimeType("text/xml");
        documentEntry2.setSize(12345L);
        documentEntry2.setUniqueId("1.2.3.4.5.222");
        documentEntry2.setUri("CDA-2.XML");

        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.setLimitedMetadata(true);
        submissionSet.setEntryUuid(UUID.randomUUID().toString());
        submissionSet.setSourceId("1.2.3.4.5");
        submissionSet.setSubmissionTime(new Timestamp(new DateTime(), Timestamp.Precision.SECOND));
        submissionSet.setUniqueId("1.2.3.4.5.777");

        Association association1 = new Association();
        association1.setLabel(AssociationLabel.ORIGINAL);
        association1.setAssociationType(AssociationType.HAS_MEMBER);
        association1.setSourceUuid(submissionSet.getEntryUuid());
        association1.setTargetUuid(documentEntry1.getEntryUuid());

        Association association2 = new Association();
        association2.setLabel(AssociationLabel.ORIGINAL);
        association2.setAssociationType(AssociationType.HAS_MEMBER);
        association2.setSourceUuid(submissionSet.getEntryUuid());
        association2.setTargetUuid(documentEntry2.getEntryUuid());

        RegisterDocumentSet request = new RegisterDocumentSet();
        request.getDocumentEntries().add(documentEntry1);
        request.getDocumentEntries().add(documentEntry2);
        request.setSubmissionSet(submissionSet);
        request.getAssociations().add(association1);
        request.getAssociations().add(association2);

        return request;
    }

    @Test
    public void testXdmRequestValidation() throws Exception {
        RegisterDocumentSet xdmRequest = createXdmRequest();

        RegisterDocumentSetTransformer transformer = new RegisterDocumentSetTransformer(new EbXMLFactory30());
        EbXMLSubmitObjectsRequest ebXmlRequest = transformer.toEbXML(xdmRequest);

        /*
        JAXBContext jaxbContext = JAXBContext.newInstance(SubmitObjectsRequest.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(ebXmlRequest.getInternal(), writer);
        System.out.println(writer.toString());
        */

        SubmitObjectsRequestValidator validator = new SubmitObjectsRequestValidator();
        validator.validate(ebXmlRequest, XDM.Interactions.ITI_41);
    }

}
