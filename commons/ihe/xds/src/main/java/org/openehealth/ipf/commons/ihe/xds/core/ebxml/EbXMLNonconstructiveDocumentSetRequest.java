/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml;

import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;

import java.util.List;

/**
 * Encapsulation of the ebXML classes for {@code RetrieveDocumentSetRequestType} and {@code RemoveDocumentsRequestType}.
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLNonconstructiveDocumentSetRequest<E> {
    /**
     * Sets the documents of the request.
     * @param documents
     *          the documents.
     */
    void setDocuments(List<DocumentReference> documents);

    /**
     * @return the documents of the request.
     */
    List<DocumentReference> getDocuments();

    /**
     * @return the ebXML object being wrapped by this class. 
     */
    E getInternal();
}
