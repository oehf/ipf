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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;

/**
 * Transforms between a {@link QueryRegistry} and an {@link EbXMLAdhocQueryRequest}. 
 * @author Jens Riemschneider
 */
public class QueryRegistryTransformer {
    private final EbXMLFactory factory30 = new EbXMLFactory30();
    private final EbXMLFactory factory21 = new EbXMLFactory21();
    
    /**
     * Transforms the request into its ebXML representation.
     * @param request
     *          the request. Can be <code>null</code>.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLAdhocQueryRequest toEbXML(QueryRegistry request) {
        if (request == null) {
            return null;
        }
        
        Query query = request.getQuery();
        EbXMLAdhocQueryRequest ebXML = createAdhocQueryRequest(query);        
        query.accept(new ToEbXMLVisitor(ebXML));        

        ebXML.setReturnType(request.getReturnType().getCode());

        return ebXML;        
    }

    /**
     * Transforms the ebXML representation into a request.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     * @return the request. <code>null</code> if the input was <code>null</code>.
     */
    public QueryRegistry fromEbXML(EbXMLAdhocQueryRequest ebXML) {
        if (ebXML == null) {
            return null;
        }
        
        String id = ebXML.getId();
        QueryType queryType = id == null ? QueryType.SQL : QueryType.valueOfId(id);
        if (queryType == null) {
            return null;
        }
        
        Query query = createQuery(queryType);        
        query.accept(new FromEbXMLVisitor(ebXML));
        
        QueryRegistry queryRegistry = new QueryRegistry(query);
        queryRegistry.setReturnType(QueryReturnType.valueOfCode(ebXML.getReturnType()));

        return queryRegistry;
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

    private EbXMLAdhocQueryRequest createAdhocQueryRequest(Query query) {
        EbXMLFactory factory = query.getType() == QueryType.SQL ? factory21 : factory30;
        return factory.createAdhocQueryRequest();
    }
}
