package net.ihe.gazelle.hl7v3transformer;

import java.io.InputStream;
import java.io.OutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class HL7V3Transformer {
	
	@SuppressWarnings("unchecked")
	public static <T> T  unmarshallMessage(Class<T> messageType, InputStream is) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(messageType);
		Unmarshaller u = jc.createUnmarshaller();
        return (T) u.unmarshal(is);
	}
	
	public static <T> void marshallMessage(Class<T> messageType, OutputStream out, T message) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(messageType);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
		m.marshal(message, out);		
	}
}
