/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;

import java.util.Date;

abstract class AbstractProvideDocumentBundle<T extends AbstractProvideDocumentBundle<T>> extends Bundle {

    public AbstractProvideDocumentBundle() {
        super();
        setTimestamp(new Date());
    }

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

    public T addFolderList(String fullUrl, FolderList<?> folderList) {
        return addEntry(fullUrl, folderList);
    }

    public T addBinary(String fullUrl, Binary binary) {
        return addEntry(fullUrl, binary);
    }

}
