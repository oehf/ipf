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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.AdhocQueryRequest21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.EbXMLQueryResponse21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequest21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.RegistryResponse21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.SubmitObjectsRequest21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.RegisterDocumentSetTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses.ResponseTransformer;

/**
 * Camel type converters for EbXML 2.1.
 * @author Jens Riemschneider
 */
@Converter
public class EbXML21Converters {
    private final static EbXMLFactory21 factory = new EbXMLFactory21();
    private final static ProvideAndRegisterDocumentSetTransformer provideAndRegisterDocumentSetTransformer = new ProvideAndRegisterDocumentSetTransformer(factory);
    private final static RegisterDocumentSetTransformer registerDocumentSetTransformer = new RegisterDocumentSetTransformer(factory);
    private final static ResponseTransformer responseTransformer = new ResponseTransformer(factory);
    private final static QueryRegistryTransformer queryRegistryTransformer = new QueryRegistryTransformer();
    private final static QueryResponseTransformer queryResponseTransformer = new QueryResponseTransformer(factory);

    @Converter
    public static SubmitObjectsRequest convert(ProvideAndRegisterDocumentSet in) {
        return (SubmitObjectsRequest) provideAndRegisterDocumentSetTransformer.toEbXML(in).getInternal();                
    }
    
    @Converter
    public static ProvideAndRegisterDocumentSet convert(ProvideAndRegisterDocumentSetRequestType in) {
        return provideAndRegisterDocumentSetTransformer.fromEbXML(ProvideAndRegisterDocumentSetRequest21.create(in));        
    }

    @Converter
    public static SubmitObjectsRequest convert(RegisterDocumentSet in) {
        return (SubmitObjectsRequest) registerDocumentSetTransformer.toEbXML(in).getInternal();
    }

    @Converter
    public static RegisterDocumentSet convert(SubmitObjectsRequest in) {
        return registerDocumentSetTransformer.fromEbXML(SubmitObjectsRequest21.create(in));
    }

    @Converter
    public static RegistryResponse convert(Response in) {
        return (RegistryResponse) responseTransformer.toEbXML(in).getInternal();
    }

    @Converter
    public static Response convert(RegistryResponse in) {
        return responseTransformer.fromEbXML(RegistryResponse21.create(in));
    }
    
    @Converter
    public static AdhocQueryRequest convert(QueryRegistry in) {
        return (AdhocQueryRequest)queryRegistryTransformer.toEbXML(in).getInternal(); 
    }
    
    @Converter
    public static QueryRegistry convert(AdhocQueryRequest in) {
        return queryRegistryTransformer.fromEbXML(AdhocQueryRequest21.create(in));
    }
    
    @Converter
    public static RegistryResponse convert(QueryResponse in) {
        return (RegistryResponse) queryResponseTransformer.toEbXML(in).getInternal();
    }
    
    @Converter
    public static QueryResponse convertToQueryResponse(RegistryResponse in) {
        return queryResponseTransformer.fromEbXML(EbXMLQueryResponse21.create(in));
    }
}
    
