/*
 * Copyright 2013 the original author or authors.
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


import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query;

import java.util.List;

/**
 * Encapsulation of the ebXML classes for {@code RemoveObjectsRequest}.
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLRemoveObjectsRequest extends EbXMLAdhocQueryRequest {

    /**
     * Sets the object references of the remove request.
     * @param references
     *          the object references.
     */
    void setReferences(List<ObjectReference> references);

    /**
     * @return the object reference of the remove request.
     */
    List<ObjectReference> getReferences();

    /**
     *
     * @return the deletionScope of the remove request.
     */
    String getDeletionScope();

    /**
     * Sets the deletionScope of the remove request.
     */
    void setDeletionScope(String deletionScope);

    /**
     * @return the wrapped ebXML representation of this request.
     */
    Object getInternal();
}
