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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLAdhocQueryRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLQueryResponse30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLProvideAndRegisterDocumentSetRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLRetrieveDocumentSetRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLRetrieveDocumentSetResponse30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
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

    /**
     * Standard Camel converter for the Provide and Register Document Set request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static ProvideAndRegisterDocumentSetRequestType convert(ProvideAndRegisterDocumentSet in) {
        return (ProvideAndRegisterDocumentSetRequestType) provideAndRegisterDocumentSetTransformer.toEbXML(in).getInternal();                
    }
    
    /**
     * Standard Camel converter for the Provide and Register Document Set request.
     * @param in
     *          an ebXML 3.0 object.
     * @return a version independent request object. 
     */
    @Converter
    public static ProvideAndRegisterDocumentSet convert(ProvideAndRegisterDocumentSetRequestType in) {
        return provideAndRegisterDocumentSetTransformer.fromEbXML(new EbXMLProvideAndRegisterDocumentSetRequest30(in));        
    }

    /**
     * Standard Camel converter for the Register Document Set request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static SubmitObjectsRequest convert(RegisterDocumentSet in) {
        return (SubmitObjectsRequest) registerDocumentSetTransformer.toEbXML(in).getInternal();
    }

    /**
     * Standard Camel converter for the Register Document Set request.
     * @param in
     *          an ebXML 3.0 object.
     * @return a version independent request object. 
     */
    @Converter
    public static RegisterDocumentSet convert(SubmitObjectsRequest in) {
        return registerDocumentSetTransformer.fromEbXML(new EbXMLSubmitObjectsRequest30(in));
    }

    /**
     * Standard Camel converter for the Registry response.
     * @param in
     *          a version independent response object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static RegistryResponseType convert(Response in) {
        return (RegistryResponseType) responseTransformer.toEbXML(in).getInternal();
    }

    /**
     * Standard Camel converter for the Registry response.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent response object.
     */
    @Converter
    public static Response convert(RegistryResponseType in) {
        return responseTransformer.fromEbXML(new EbXMLRegistryResponse30(in));
    }

    /**
     * Standard Camel converter for the Query Registry request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static AdhocQueryRequest convert(QueryRegistry in) {
        return (AdhocQueryRequest)queryRegistryTransformer.toEbXML(in).getInternal(); 
    }
    
    /**
     * Standard Camel converter for the Query Registry request.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent request object.
     */
    @Converter
    public static QueryRegistry convert(AdhocQueryRequest in) {
        return queryRegistryTransformer.fromEbXML(new EbXMLAdhocQueryRequest30(in));
    }
    
    /**
     * Standard Camel converter for the Query response.
     * @param in
     *          a version independent response object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static AdhocQueryResponse convert(QueryResponse in) {
        return (AdhocQueryResponse) queryResponseTransformer.toEbXML(in).getInternal();
    }
    
    /**
     * Standard Camel converter for the Query response.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent response object.
     */
    @Converter
    public static QueryResponse convertToQueryResponse(AdhocQueryResponse in) {
        return queryResponseTransformer.fromEbXML(new EbXMLQueryResponse30(in));
    }
    
    /**
     * Standard Camel converter for the Retrieve Document Set request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static RetrieveDocumentSetRequestType convert(RetrieveDocumentSet in) {
        return (RetrieveDocumentSetRequestType) retrieveDocumentSetRequestTransformer.toEbXML(in).getInternal();
    }
    
    /**
     * Standard Camel converter for the Retrieve Document Set request.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent request object.
     */
    @Converter
    public static RetrieveDocumentSet convert(RetrieveDocumentSetRequestType in) {
        return retrieveDocumentSetRequestTransformer.fromEbXML(new EbXMLRetrieveDocumentSetRequest30(in));
    }
    
    /**
     * Standard Camel converter for the Retrieve Document Set response.
     * @param in
     *          a version independent response object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static RetrieveDocumentSetResponseType convert(RetrievedDocumentSet in) {
        return (RetrieveDocumentSetResponseType) retrieveDocumentSetResponseTransformer.toEbXML(in).getInternal();
    }
    
    /**
     * Standard Camel converter for the Retrieve Document Set response.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent response object.
     */
    @Converter
    public static RetrievedDocumentSet convert(RetrieveDocumentSetResponseType in) {
        return retrieveDocumentSetResponseTransformer.fromEbXML(new EbXMLRetrieveDocumentSetResponse30(in));
    }
}

