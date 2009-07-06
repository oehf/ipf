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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.converters;

import org.apache.camel.Converter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.AdhocQueryRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLQueryResponse30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RegistryResponse30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RetrieveDocumentSetRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RetrieveDocumentSetResponse30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.SubmitObjectsRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.RegisterDocumentSetTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.RetrieveDocumentSetRequestTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses.ResponseTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses.RetrieveDocumentSetResponseTransformer;

/**
 * Camel type converters for EbXML 3.0.
 * @author Jens Riemschneider
 */
@Converter
public class EbXML30Converters {
    private final static EbXMLFactory30 factory = new EbXMLFactory30();
    private final static ProvideAndRegisterDocumentSetTransformer provideAndRegisterDocumentSetTransformer = new ProvideAndRegisterDocumentSetTransformer(factory);
    private final static RegisterDocumentSetTransformer registerDocumentSetTransformer = new RegisterDocumentSetTransformer(factory);
    private final static ResponseTransformer responseTransformer = new ResponseTransformer(factory);
    private final static QueryRegistryTransformer queryRegistryTransformer = new QueryRegistryTransformer();
    private final static QueryResponseTransformer queryResponseTransformer = new QueryResponseTransformer(factory);
    private final static RetrieveDocumentSetRequestTransformer retrieveDocumentSetRequestTransformer = new RetrieveDocumentSetRequestTransformer(factory);
    private final static RetrieveDocumentSetResponseTransformer retrieveDocumentSetResponseTransformer = new RetrieveDocumentSetResponseTransformer(factory);

    @Converter
    public static SubmitObjectsRequest convert(ProvideAndRegisterDocumentSet in) {
        return (SubmitObjectsRequest) provideAndRegisterDocumentSetTransformer.toEbXML(in).getInternal();                
    }
    
    @Converter
    public static ProvideAndRegisterDocumentSet convert(ProvideAndRegisterDocumentSetRequestType in) {
        return provideAndRegisterDocumentSetTransformer.fromEbXML(ProvideAndRegisterDocumentSetRequest30.create(in));        
    }

    @Converter
    public static SubmitObjectsRequest convert(RegisterDocumentSet in) {
        return (SubmitObjectsRequest) registerDocumentSetTransformer.toEbXML(in).getInternal();
    }

    @Converter
    public static RegisterDocumentSet convert(SubmitObjectsRequest in) {
        return registerDocumentSetTransformer.fromEbXML(SubmitObjectsRequest30.create(in));
    }

    @Converter
    public static RegistryResponseType convert(Response in) {
        return (RegistryResponseType) responseTransformer.toEbXML(in).getInternal();
    }

    @Converter
    public static Response convert(RegistryResponseType in) {
        return responseTransformer.fromEbXML(RegistryResponse30.create(in));
    }

    @Converter
    public static AdhocQueryRequest convert(QueryRegistry in) {
        return (AdhocQueryRequest)queryRegistryTransformer.toEbXML(in).getInternal(); 
    }
    
    @Converter
    public static QueryRegistry convert(AdhocQueryRequest in) {
        return queryRegistryTransformer.fromEbXML(AdhocQueryRequest30.create(in));
    }
    
    @Converter
    public static AdhocQueryResponse convert(QueryResponse in) {
        return (AdhocQueryResponse) queryResponseTransformer.toEbXML(in).getInternal();
    }
    
    @Converter
    public static QueryResponse convertToQueryResponse(AdhocQueryResponse in) {
        return queryResponseTransformer.fromEbXML(EbXMLQueryResponse30.create(in));
    }
    
    @Converter
    public static RetrieveDocumentSetRequestType convert(RetrieveDocumentSet in) {
        return (RetrieveDocumentSetRequestType) retrieveDocumentSetRequestTransformer.toEbXML(in).getInternal();
    }
    
    @Converter
    public static RetrieveDocumentSet convert(RetrieveDocumentSetRequestType in) {
        return retrieveDocumentSetRequestTransformer.fromEbXML(RetrieveDocumentSetRequest30.create(in));
    }

    
    @Converter
    public static RetrieveDocumentSetResponseType convert(RetrievedDocumentSet in) {
        return (RetrieveDocumentSetResponseType) retrieveDocumentSetResponseTransformer.toEbXML(in).getInternal();
    }
    
    @Converter
    public static RetrievedDocumentSet convert(RetrieveDocumentSetResponseType in) {
        return retrieveDocumentSetResponseTransformer.fromEbXML(RetrieveDocumentSetResponse30.create(in));
    }
}

