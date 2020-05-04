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
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.*;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.*;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.ResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.RetrieveDocumentSetResponseTransformer;

/**
 * Camel type converters for EbXML 3.0.
 * <p>
 * There are three types of classes for EbXML:
 * <li> The version dependent classes from the schema files
 * <li> The version independent classes
 * <li> The meta data classes
 * All of these can be converted into each other with the appropriate factories or constructors.
 * @author Jens Riemschneider
 */
@Converter(generateLoader = true)
public class EbXML30Converters {
    private final static EbXMLFactory30 factory = new EbXMLFactory30();
    
    /**
     * Standard Camel converter for the Provide and Register Document Set request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static ProvideAndRegisterDocumentSetRequestType convert(ProvideAndRegisterDocumentSet in) {
        var transformer = new ProvideAndRegisterDocumentSetTransformer(factory);
        return (ProvideAndRegisterDocumentSetRequestType) transformer.toEbXML(in).getInternal();                
    }
    
    /**
     * Standard Camel converter for the Provide and Register Document Set request.
     * @param in
     *          an ebXML 3.0 object.
     * @return a version independent request object. 
     */
    @Converter
    public static ProvideAndRegisterDocumentSet convert(ProvideAndRegisterDocumentSetRequestType in) {
        var transformer = new ProvideAndRegisterDocumentSetTransformer(factory);
        return transformer.fromEbXML(new EbXMLProvideAndRegisterDocumentSetRequest30(in));        
    }

    /**
     * Standard Camel converter for the Register Document Set request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static SubmitObjectsRequest convert(RegisterDocumentSet in) {
        var transformer = new RegisterDocumentSetTransformer(factory);
        return (SubmitObjectsRequest) transformer.toEbXML(in).getInternal();
    }

    /**
     * Standard Camel converter for the Register Document Set request.
     * @param in
     *          an ebXML 3.0 object.
     * @return a version independent request object. 
     */
    @Converter
    public static RegisterDocumentSet convert(SubmitObjectsRequest in) {
        var transformer = new RegisterDocumentSetTransformer(factory);
        return transformer.fromEbXML(new EbXMLSubmitObjectsRequest30(in));
    }

    /**
     * Standard Camel converter for the Registry response.
     * @param in
     *          a version independent response object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static RegistryResponseType convert(Response in) {
        var transformer = new ResponseTransformer(factory);
        return (RegistryResponseType) transformer.toEbXML(in).getInternal();
    }

    /**
     * Standard Camel converter for the Registry response.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent response object.
     */
    @Converter
    public static Response convert(RegistryResponseType in) {
        var transformer = new ResponseTransformer(factory);
        return transformer.fromEbXML(new EbXMLRegistryResponse30(in));
    }

    /**
     * Standard Camel converter for the Query Registry request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static AdhocQueryRequest convert(QueryRegistry in) {
        var transformer = new QueryRegistryTransformer();
        return (AdhocQueryRequest)transformer.toEbXML(in).getInternal(); 
    }
    
    /**
     * Standard Camel converter for the Query Registry request.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent request object.
     */
    @Converter
    public static QueryRegistry convert(AdhocQueryRequest in) {
        var transformer = new QueryRegistryTransformer();
        return transformer.fromEbXML(new EbXMLAdhocQueryRequest30(in));
    }
    
    /**
     * Standard Camel converter for the Query response.
     * @param in
     *          a version independent response object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static AdhocQueryResponse convert(QueryResponse in) {
        var transformer = new QueryResponseTransformer(factory);
        return (AdhocQueryResponse) transformer.toEbXML(in).getInternal();
    }
    
    /**
     * Standard Camel converter for the Query response.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent response object.
     */
    @Converter
    public static QueryResponse convertToQueryResponse(AdhocQueryResponse in) {
        var transformer = new QueryResponseTransformer(factory);
        return transformer.fromEbXML(new EbXMLQueryResponse30(in));
    }
    
    /**
     * Standard Camel converter for the Retrieve Document Set request.
     * @param in
     *          a version independent request object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static RetrieveDocumentSetRequestType convert(RetrieveDocumentSet in) {
        var transformer = new RetrieveDocumentSetRequestTransformer(factory);
        return (RetrieveDocumentSetRequestType) transformer.toEbXML(in).getInternal();
    }
    
    /**
     * Standard Camel converter for the Retrieve Document Set request.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent request object.
     */
    @Converter
    public static RetrieveDocumentSet convert(RetrieveDocumentSetRequestType in) {
        var transformer = new RetrieveDocumentSetRequestTransformer(factory);
        return transformer.fromEbXML(new EbXMLNonconstructiveDocumentSetRequest30<>(in));
    }

    /**
     * Standard Camel converter for the Remove Documents request.
     * @param in
     *          a version independent request object.
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static RemoveDocumentsRequestType convert(RemoveDocuments in) {
        var transformer = new RemoveDocumentsRequestTransformer(factory);
        return (RemoveDocumentsRequestType) transformer.toEbXML(in).getInternal();
    }

    /**
     * Standard Camel converter for the Remove Documents request.
     * @param in
     *          an ebXML 3.0 object.
     * @return a version independent request object.
     */
    @Converter
    public static RemoveDocuments convert(RemoveDocumentsRequestType in) {
        var transformer = new RemoveDocumentsRequestTransformer(factory);
        return transformer.fromEbXML(new EbXMLNonconstructiveDocumentSetRequest30<>(in));
    }

    /**
     * Standard Camel converter for the Retrieve Document Set response.
     * @param in
     *          a version independent response object. 
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static RetrieveDocumentSetResponseType convert(RetrievedDocumentSet in) {
        var transformer = new RetrieveDocumentSetResponseTransformer(factory);
        return (RetrieveDocumentSetResponseType) transformer.toEbXML(in).getInternal();
    }
    
    /**
     * Standard Camel converter for the Retrieve Document Set response.
     * @param in
     *          an ebXML 3.0 object. 
     * @return a version independent response object.
     */
    @Converter
    public static RetrievedDocumentSet convert(RetrieveDocumentSetResponseType in) {
        var transformer = new RetrieveDocumentSetResponseTransformer(factory);
        return transformer.fromEbXML(new EbXMLRetrieveDocumentSetResponse30(in));
    }

    /**
     * Standard Camel converter for the Retrieve Imaging Document Set request.
     * @param in    A version independent request object
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static RetrieveImagingDocumentSetRequestType convert(RetrieveImagingDocumentSet in) {
        var transformer = new RetrieveImagingDocumentSetRequestTransformer(factory);
        return (RetrieveImagingDocumentSetRequestType) transformer.toEbXML(in).getInternal();
    }

    /**
     * Standard Camel converter for the Retrieve Imaging Document Set request.
     * @param in    An ebXML 3.0 object.
     * @return a version independent request object.
     */
    @Converter
    public static RetrieveImagingDocumentSet convert(RetrieveImagingDocumentSetRequestType in) {
        var transformer = new RetrieveImagingDocumentSetRequestTransformer(factory);
        return transformer.fromEbXML(new EbXMLRetrieveImagingDocumentSetRequest30(in));
    }

    /**
     * Standard Camel converter for the Remove Objects request.
     * @param in    A version independent request object
     * @return an ebXML 3.0 object.
     */
    @Converter
    public static RemoveObjectsRequest convert(RemoveMetadata in) {
        var transformer = new RemoveMetadataRequestTransformer();
        return (RemoveObjectsRequest) transformer.toEbXML(in).getInternal();
    }

    /**
     * Standard Camel converter for the Remove Document Set request.
     * @param in    An ebXML 3.0 object.
     * @return a version independent request object.
     */
    @Converter
    public static RemoveMetadata convert(RemoveObjectsRequest in) {
        var transformer = new RemoveMetadataRequestTransformer();
        return transformer.fromEbXML(new EbXMLRemoveMetadataRequest30(in));
    }

}

