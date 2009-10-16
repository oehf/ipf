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
package org.openehealth.ipf.platform.camel.ihe.xds.core.converters;

import org.apache.camel.Converter;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ProvideAndRegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.ResponseTransformer;

/**
 * Camel type converters for EbXML 2.1.
 * <p>
 * There are three types of classes for EbXML:
 * <li> The version dependent classes from the schema files
 * <li> The version independent classes
 * <li> The meta data classes
 * All of these can be converted into each other with the appropriate factories or constructors.
 * @author Jens Riemschneider
 */
@Converter
public class EbXML21Converters {
    private final static EbXMLFactory21 factory = new EbXMLFactory21();
    
    /**
     * Standard Camel converter for the Provide and Register Document Set request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 2.1 object.
     */
    @Converter
    public static ProvideAndRegisterDocumentSetRequestType convert(ProvideAndRegisterDocumentSet in) {
        ProvideAndRegisterDocumentSetTransformer transformer = new ProvideAndRegisterDocumentSetTransformer(factory);
        return (ProvideAndRegisterDocumentSetRequestType) transformer.toEbXML(in).getInternal();                
    }
    
    /**
     * Standard Camel converter for the Provide and Register Document Set request.
     * @param in
     *          an ebXML 2.1 object.
     * @return a version independent request object. 
     */
    @Converter
    public static ProvideAndRegisterDocumentSet convert(ProvideAndRegisterDocumentSetRequestType in) {
        ProvideAndRegisterDocumentSetTransformer transformer = new ProvideAndRegisterDocumentSetTransformer(factory);
        return transformer.fromEbXML(new EbXMLProvideAndRegisterDocumentSetRequest21(in));        
    }

    /**
     * Standard Camel converter for the Register Document Set request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 2.1 object.
     */
    @Converter
    public static SubmitObjectsRequest convert(RegisterDocumentSet in) {
        RegisterDocumentSetTransformer transformer = new RegisterDocumentSetTransformer(factory);
        return (SubmitObjectsRequest) transformer.toEbXML(in).getInternal();
    }

    /**
     * Standard Camel converter for the Register Document Set request.
     * @param in
     *          an ebXML 2.1 object.
     * @return a version independent request object. 
     */
    @Converter
    public static RegisterDocumentSet convert(SubmitObjectsRequest in) {
        RegisterDocumentSetTransformer transformer = new RegisterDocumentSetTransformer(factory);
        return transformer.fromEbXML(new EbXMLSubmitObjectsRequest21(in));
    }

    /**
     * Standard Camel converter for the Registry response.
     * @param in
     *          a version independent response object. 
     * @return an ebXML 2.1 object.
     */
    @Converter
    public static RegistryResponse convert(Response in) {
        ResponseTransformer transformer = new ResponseTransformer(factory);
        return (RegistryResponse) transformer.toEbXML(in).getInternal();
    }

    /**
     * Standard Camel converter for the Registry response.
     * @param in
     *          an ebXML 2.1 object. 
     * @return a version independent response object.
     */
    @Converter
    public static Response convert(RegistryResponse in) {
        ResponseTransformer transformer = new ResponseTransformer(factory);
        return transformer.fromEbXML(new EbXMLRegistryResponse21(in));
    }
    
    /**
     * Standard Camel converter for the Query Registry request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 2.1 object.
     */
    @Converter
    public static AdhocQueryRequest convert(QueryRegistry in) {
        QueryRegistryTransformer transformer = new QueryRegistryTransformer();
        return (AdhocQueryRequest)transformer.toEbXML(in).getInternal(); 
    }
    
    /**
     * Standard Camel converter for the Query Registry request.
     * @param in
     *          an ebXML 2.1 object. 
     * @return a version independent request object.
     */
    @Converter
    public static QueryRegistry convert(AdhocQueryRequest in) {
        QueryRegistryTransformer transformer = new QueryRegistryTransformer();
        return transformer.fromEbXML(new EbXMLAdhocQueryRequest21(in));
    }
    
    /**
     * Standard Camel converter for the Query response.
     * @param in
     *          a version independent response object. 
     * @return an ebXML 2.1 object.
     */
    @Converter
    public static RegistryResponse convert(QueryResponse in) {
        QueryResponseTransformer transformer = new QueryResponseTransformer(factory);
        return (RegistryResponse) transformer.toEbXML(in).getInternal();
    }
    
    /**
     * Standard Camel converter for the Query response.
     * @param in
     *          an ebXML 2.1 object. 
     * @return a version independent response object.
     */
    @Converter
    public static QueryResponse convertToQueryResponse(RegistryResponse in) {
        QueryResponseTransformer transformer = new QueryResponseTransformer(factory);
        return transformer.fromEbXML(new EbXMLQueryResponse21(in));
    }
}

