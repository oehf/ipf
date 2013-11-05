/*
 * Copyright 2011 the original author or authors.
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

import groovy.lang.Closure;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.TypeConverter;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveImagingDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.*;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.attachment.AttachmentMarshaller;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * Utility class for rendering of ebXML stub POJOs and simplified
 * XDS model classes into XML.
 *
 * @author Dmytro Rud
 */
abstract public class XdsRenderingUtils {

    /**
     * Correspondence between relevant XDS data types from
     * ebXML model and IPF simplified model.
     */
    private static final Map<Class, Class> TYPES_CORRESPONDENCE;
    private static final JAXBContext JAXB_CONTEXT;

    static {
        TYPES_CORRESPONDENCE = new HashMap<Class, Class>();

        /* --------- REQUESTS --------- */

        // ITI-18, 38, 51, 63
        TYPES_CORRESPONDENCE.put(QueryRegistry.class, AdhocQueryRequest.class);

        // ITI-41
        TYPES_CORRESPONDENCE.put(ProvideAndRegisterDocumentSet.class, ProvideAndRegisterDocumentSetRequestType.class);

        // ITI-42, 57, 61
        TYPES_CORRESPONDENCE.put(RegisterDocumentSet.class, SubmitObjectsRequest.class);

        // ITI-39, 43
        TYPES_CORRESPONDENCE.put(RetrieveDocumentSet.class, RetrieveDocumentSetRequestType.class);

        // ITI-62
        TYPES_CORRESPONDENCE.put(RemoveDocumentSet.class, RemoveObjectsRequest.class);

        // RAD-69, 75
        TYPES_CORRESPONDENCE.put(RetrieveImagingDocumentSet.class, RetrieveImagingDocumentSetRequestType.class);

        /* --------- RESPONSES --------- */

        // ITI-18, 38, 51, 63
        TYPES_CORRESPONDENCE.put(QueryResponse.class, AdhocQueryResponse.class);

        // ITI-41, 42, 57, 61, 62
        TYPES_CORRESPONDENCE.put(Response.class, RegistryResponseType.class);

        // ITI-39, ITI-43, RAD-69, RAD-75
        TYPES_CORRESPONDENCE.put(RetrievedDocumentSet.class, RetrieveDocumentSetResponseType.class);


        try {
            JAXB_CONTEXT = JAXBContext.newInstance(
                    AdhocQueryRequest.class,
                    ProvideAndRegisterDocumentSetRequestType.class,
                    SubmitObjectsRequest.class,
                    RetrieveDocumentSetRequestType.class,
                    RemoveObjectsRequest.class,
                    RetrieveImagingDocumentSetRequestType.class,
                    AdhocQueryResponse.class,
                    RegistryResponseType.class,
                    RetrieveDocumentSetResponseType.class,

                    org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType.class,
                    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.SubmitObjectsRequest.class,
                    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse.class,
                    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.AdhocQueryRequest.class
            );
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Constructor.
     */
    private XdsRenderingUtils() {
        throw new IllegalStateException("Cannot instantiate helper class");
    }


    /**
     * Renders an XDS object (either ebXML POJO or an object from the simplified model)
     * contained in the input message of given Camel exchange.
     *
     * @param exchange
     *      Camel exchange containing the XDS object in <code>exchange.in.body</code>.
     * @return
     *      XML representation of the XDS object contained in the given Camel exchange.
     */
    public static String render(Exchange exchange) {
        return doRender(exchange, exchange.getIn().getBody());
    }


    /**
     * Renders an XDS object (either ebXML POJO or an object from the simplified model)
     * contained in the given Camel exchange.
     *
     * @param exchange
     *      Camel exchange containing the XDS object.
     * @param closure
     *      Groovy closure to extract the XDS object from the exchange.
     * @return
     *      XML representation of the XDS object contained in the given Camel exchange.
     */
    public static String render(Exchange exchange, Closure closure) {
        return doRender(exchange, closure.call(exchange));
    }


    /**
     * Renders an XDS object (either ebXML POJO or an object from the simplified model)
     * contained in the given Camel exchange.
     *
     * @param exchange
     *      Camel exchange containing the XDS object.
     * @param expression
     *      Camel expression to extract the XDS object from the exchange.
     * @return
     *      XML representation of the XDS object contained in the given Camel exchange.
     */
    public static String render(Exchange exchange, Expression expression) {
        return doRender(exchange, expression.evaluate(exchange, Object.class));
    }


    /**
     * Renders an XDS object (either ebXML POJO or an object from the simplified model).
     *
     * @param exchange
     *      Camel exchange.
     * @param body
     *      XDS object (either ebXML POJO or an object from the simplified model).
     * @return
     *      XML representation of the given XDS object.
     */
    public static String doRender(Exchange exchange, Object body) {
        if (TYPES_CORRESPONDENCE.containsKey(body.getClass())) {
            TypeConverter converter = exchange.getContext().getTypeConverter();
            body = converter.convertTo(TYPES_CORRESPONDENCE.get(body.getClass()), exchange, body);
        }
        return renderEbxml(body);
    }


    /**
     * Returns marshaled XML representation of the given ebXML POJO.
     * @param ebXml
     *      ebXML POJO.
     * @return
     *      XML string representing the given POJO.
     */
    public static String renderEbxml(Object ebXml) {
        try {
            StringWriter writer = new StringWriter();
            Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
            marshaller.setAttachmentMarshaller(new NonReadingAttachmentMarshaller());
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ebXml, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * An attachment marshaller implementation which does not read any data
     * from the provided data handlers in order to keep all streams usable.
     */
    private static class NonReadingAttachmentMarshaller extends AttachmentMarshaller {
        @Override
        public boolean isXOPPackage() {
            return true;
        }

        @Override
        public String addMtomAttachment(DataHandler data, String elementNamespace, String elementLocalName) {
            return attachmentDescription(data.getName(), null, data.getContentType());
        }

        @Override
        public String addMtomAttachment(byte[] data, int offset, int length, String mimeType,
                                        String elementNamespace, String elementLocalName)
        {
            String size = Integer.toString(Math.min(length, data.length - offset));
            return attachmentDescription(null, size, mimeType);
        }

        @Override
        public String addSwaRefAttachment(DataHandler data) {
            return attachmentDescription(data.getName(), null, data.getContentType());
        }

        private static String attachmentDescription(String name, String size, String contentType) {
            return new StringBuilder()
                    .append("Attachment: name='")
                    .append(defaultString(name, "[unknown]"))
                    .append("', size='")
                    .append(defaultString(size, "[unknown]"))
                    .append("', content type='")
                    .append(defaultString(contentType, "[unknown]"))
                    .append('\'')
                    .toString();
        }

    }

}
