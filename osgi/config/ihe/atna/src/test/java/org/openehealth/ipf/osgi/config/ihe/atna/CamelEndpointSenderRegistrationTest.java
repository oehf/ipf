package org.openehealth.ipf.osgi.config.ihe.atna;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.platform.camel.ihe.atna.util.CamelEndpointSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context.xml"})
public class CamelEndpointSenderRegistrationTest {

    @Autowired
    CamelEndpointSenderRegistration camelEndpointSenderListener;


    @Test
    public void testSetAndUnset(){
        CamelEndpointSender sender = new CamelEndpointSender();
        assertNotNull(camelEndpointSenderListener.getIheAuditorContext());
        assertEquals("localhost", camelEndpointSenderListener.getIheAuditorContext().getConfig().getAuditRepositoryHost());
        assertEquals(514, camelEndpointSenderListener.getIheAuditorContext().getConfig().getAuditRepositoryPort());

        camelEndpointSenderListener.setCamelEndpointSenderService(sender, null);
        assertEquals("0.0.0.0", camelEndpointSenderListener.getIheAuditorContext().getConfig().getAuditRepositoryHost());
        assertEquals(0, camelEndpointSenderListener.getIheAuditorContext().getConfig().getAuditRepositoryPort());
        assertEquals(sender, camelEndpointSenderListener.getIheAuditorContext().getSender());

        camelEndpointSenderListener.unsetCamelEndpointSenderService(sender, null);
        assertEquals("localhost", camelEndpointSenderListener.getIheAuditorContext().getConfig().getAuditRepositoryHost());
        assertEquals(514, camelEndpointSenderListener.getIheAuditorContext().getConfig().getAuditRepositoryPort());
    }

}