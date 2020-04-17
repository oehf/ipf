package org.openehealth.ipf.commons.ihe.ws.cxf;

import static org.junit.Assert.assertEquals;

import javax.activation.DataHandler;

import org.junit.Test;

public class NonReadingAttachmentMarshallerTest {
    private NonReadingAttachmentMarshaller classUnderTest = new NonReadingAttachmentMarshaller();

    @Test
    public void mtomAttachmentWithoutName() {
        assertEquals("Attachment: name='[unknown]', size='[unknown]', content type='text/xml'",
                classUnderTest.addMtomAttachment(new DataHandler("myobject", "text/xml"), null, null));
    }
}
