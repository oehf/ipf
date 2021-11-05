package org.openehealth.ipf.commons.ihe.xds.core.requests;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterDocumentSetBuilderTest {

    @Test
    public void supportiveBuilder() throws GSSException {
        var patientID = new Identifiable("id3", new Oid("1.3"));
        var submissionSet = SampleData.createSubmissionSet(patientID);
        submissionSet.setEntryUuid(null);
        submissionSet.setUniqueId(null);
        submissionSet.setSubmissionTime((String) null);
        var document = SampleData.createDocumentEntry(patientID);
        document.setEntryUuid(null);
        document.setUniqueId(null);
        var folder1 = SampleData.createFolder(patientID);
        folder1.setEntryUuid(null);
        folder1.setUniqueId(null);
        var folder2 = SampleData.createFolder(patientID);
        folder2.setEntryUuid("lol");
        folder2.setUniqueId("2.999.2.3.4");

        var registerDocuments = RegisterDocumentSet.supportiveBuilderWith(submissionSet)
                .withDocument(document).withFolders(List.of(folder1, folder2)).build();
        assertEquals(3, registerDocuments.getAssociations().size(), "Expecting 3 Association to be created");
        assertEquals(2, registerDocuments.getFolders().size());
        assertEquals(1, registerDocuments.getDocumentEntries().size());
        assertNotNull(submissionSet.getEntryUuid());
        assertNotNull(submissionSet.getSubmissionTime());
        assertNotNull(folder1.getEntryUuid());
        assertNotNull(document.getEntryUuid());
        assertNotNull(submissionSet.getUniqueId());
        assertNotNull(folder1.getUniqueId());
        assertNotNull(document.getUniqueId());
        assertEquals("lol", folder2.getEntryUuid());
        assertEquals("2.999.2.3.4", folder2.getUniqueId());
    }
    
    @Test
    public void basicBuilder() throws GSSException {
        var patientID = new Identifiable("id3", new Oid("1.3"));
        var submissionSet = SampleData.createSubmissionSet(patientID);
        submissionSet.setEntryUuid(null);
        submissionSet.setUniqueId(null);
        var document = SampleData.createDocumentEntry(patientID);
        document.setEntryUuid(null);
        document.setUniqueId(null);

        var registerDocuments = RegisterDocumentSet.builderWith(submissionSet)
                .withDocument(document).build();
        
        assertEquals(0, registerDocuments.getAssociations().size());
        assertNull(submissionSet.getEntryUuid());
        assertEquals(1, registerDocuments.getDocumentEntries().size());

    }

}
