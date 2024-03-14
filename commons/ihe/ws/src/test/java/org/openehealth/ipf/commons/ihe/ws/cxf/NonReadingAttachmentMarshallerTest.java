package org.openehealth.ipf.commons.ihe.ws.cxf;

import org.junit.jupiter.api.Test;

import jakarta.activation.DataHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonReadingAttachmentMarshallerTest {
    private final NonReadingAttachmentMarshaller classUnderTest = new NonReadingAttachmentMarshaller();

    @Test
    public void mtomAttachmentWithoutName() {
        assertEquals("Attachment: name='[unknown]', size='[unknown]', content type='text/xml'",
                classUnderTest.addMtomAttachment(new DataHandler("myobject", "text/xml"), null, null));
    }
}
