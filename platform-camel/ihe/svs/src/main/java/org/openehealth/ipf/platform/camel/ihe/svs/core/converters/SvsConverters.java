package org.openehealth.ipf.platform.camel.ihe.svs.core.converters;

import org.apache.camel.Converter;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Camel type converters for SVS models.
 *
 * @author Quentin Ligier
 */
@Converter(generateLoader = true)
public class SvsConverters {

    private static final JAXBContext JAXB_CONTEXT_SVS_REQUEST;
    private static final JAXBContext JAXB_CONTEXT_SVS_RESPONSE;
    static {
        try {
            JAXB_CONTEXT_SVS_REQUEST = JAXBContext.newInstance(RetrieveValueSetRequest.class);
            JAXB_CONTEXT_SVS_RESPONSE = JAXBContext.newInstance(RetrieveValueSetResponse.class);
        } catch (final JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Converter
    public static RetrieveValueSetRequest xmlToSvsQuery(final String xml) throws JAXBException {
        var unmarshaller = JAXB_CONTEXT_SVS_REQUEST.createUnmarshaller();
        return (RetrieveValueSetRequest) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Converter
    public static String svsQueryToXml(final RetrieveValueSetRequest query) throws JAXBException {
        var marshaller = JAXB_CONTEXT_SVS_REQUEST.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        var stringWriter = new StringWriter();
        marshaller.marshal(query, stringWriter);
        return stringWriter.toString();
    }

    @Converter
    public static RetrieveValueSetResponse xmlToSvsResponse(final String xml) throws JAXBException {
        var unmarshaller = JAXB_CONTEXT_SVS_RESPONSE.createUnmarshaller();
        return (RetrieveValueSetResponse) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Converter
    public static String svsResponseToXml(final RetrieveValueSetResponse response) throws JAXBException {
        var marshaller = JAXB_CONTEXT_SVS_RESPONSE.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        var stringWriter = new StringWriter();
        marshaller.marshal(response, stringWriter);
        return stringWriter.toString();
    }
}
