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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Transforms between a {@link org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocumentSet} and its ebXML representation.
 * @author Boris Stanojevic
 */
public class RemoveDocumentSetTransformer {
    private final EbXMLFactory factory = new EbXMLFactory30();

    /**
     * Constructs the transformer
     */
    public RemoveDocumentSetTransformer() {
    }

    /**
     * Transforms the request into its ebXML representation.
     * @param request
     *          the request. Can be <code>null</code>.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLRemoveObjectsRequest toEbXML(RemoveDocumentSet request) {
        if (request == null) {
            return null;
        }

        EbXMLRemoveObjectsRequest ebXML = factory.createRemoveObjectsRequest();
        ebXML.setReferences(request.getReferences());
        ebXML.setDeletionScope(request.getDeletionScope());

        if (request.getQuery() != null){
            Query query = request.getQuery();
            query.accept(new ToEbXMLVisitor(ebXML));
        }

        return ebXML;
    }

    /**
     * Transforms the ebXML representation into a request.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     * @return the request. <code>null</code> if the input was <code>null</code>.
     */
    public RemoveDocumentSet fromEbXML(EbXMLRemoveObjectsRequest ebXML) {
        if (ebXML == null) {
            return null;
        }

        RemoveDocumentSet request = new RemoveDocumentSet();
        request.getReferences().addAll(ebXML.getReferences());
        request.setDeletionScope(ebXML.getDeletionScope());

        if (ebXML.getId() != null){
            String id = ebXML.getId();
            QueryType queryType = id == null ? QueryType.SQL : QueryType.valueOfId(id);
            if (queryType == null) {
                return null;
            }

            Query query = createQuery(queryType);
            query.accept(new FromEbXMLVisitor(ebXML));
            request.setQuery(query);
        }
        return request;
    }

    private Query createQuery(QueryType queryType) {
        try {
            return queryType.getType().newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Invalid query class for type: " + queryType, e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Invalid query class for type: " + queryType, e);
        }
    }

}
