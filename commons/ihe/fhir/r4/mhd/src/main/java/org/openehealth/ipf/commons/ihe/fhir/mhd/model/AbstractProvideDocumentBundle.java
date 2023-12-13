package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;

public class AbstractProvideDocumentBundle<T extends AbstractProvideDocumentBundle<T>> extends Bundle {

    public T addEntry(String fullUrl, Resource resource) {
        addEntry()
            .setFullUrl(fullUrl)
            .setRequest(
                new Bundle.BundleEntryRequestComponent()
                    .setMethod(HTTPVerb.POST)
                    .setUrl(resource.getResourceType().name()))
            .setResource(resource);
        return (T)this;
    }

    public T addFolder(String fullUrl, FolderList<?> folderList) {
        return addEntry(fullUrl, folderList);
    }

    public T addBinary(String fullUrl, Binary binary) {
        return addEntry(fullUrl, binary);
    }

}
