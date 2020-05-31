package org.openehealth.ipf.commons.ihe.xds.core.requests;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RegisterDocumentSetBuilderTest {

    @Test
    public void supportiveBuilder() throws GSSException {
        Identifiable patientID = new Identifiable("id3", new Oid("1.3"));
        SubmissionSet submissionSet = SampleData.createSubmissionSet(patientID);
        submissionSet.setEntryUuid(null);
        submissionSet.setUniqueId(null);
        submissionSet.setSubmissionTime((String) null);
        DocumentEntry document = SampleData.createDocumentEntry(patientID);
        document.setEntryUuid(null);
        document.setUniqueId(null);
        Folder folder1 = SampleData.createFolder(patientID);
        folder1.setEntryUuid(null);
        folder1.setUniqueId(null);
        Folder folder2 = SampleData.createFolder(patientID);
        folder2.setEntryUuid("lol");
        folder2.setUniqueId("2.999.2.3.4");
        
        RegisterDocumentSet registerDocuments = RegisterDocumentSet.supportiveBuilderWith(submissionSet)
                .withDocument(document).withFolders(List.of(folder1, folder2)).build();
        assertEquals("Expecting 3 Association to be created", 3, registerDocuments.getAssociations().size());
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
        Identifiable patientID = new Identifiable("id3", new Oid("1.3"));
        SubmissionSet submissionSet = SampleData.createSubmissionSet(patientID);
        submissionSet.setEntryUuid(null);
        submissionSet.setUniqueId(null);
        DocumentEntry document = SampleData.createDocumentEntry(patientID);
        document.setEntryUuid(null);
        document.setUniqueId(null);

        RegisterDocumentSet registerDocuments = RegisterDocumentSet.builderWith(submissionSet)
                .withDocument(document).build();
        
        assertEquals(0, registerDocuments.getAssociations().size());
        assertNull(submissionSet.getEntryUuid());
        assertEquals(1, registerDocuments.getDocumentEntries().size());

    }

}
