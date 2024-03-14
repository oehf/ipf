package net.ihe.gazelle.common.marshaller;

import java.io.InputStream;
import java.io.OutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

/**
 * Marshaller of an object T.
 *
 * @author Abderrazek Boufahja
 *
 * @param <T>
 *            the kind of object to be marshalled
 */
public class ObjectMarshallerImpl<T> implements ObjectMarshaller<T> {

	/**
	 * The kind of class to be marshalled.
	 */
	private Class<T> c;

	/**
	 * Create an object marshaller.
	 *
	 * @param cl
	 *            the type of class to marshall (it shall be T.class)
	 */
	public ObjectMarshallerImpl(final Class<T> cl) {
		this.c = cl;
	}

	/**
	 * The marshalling is pretty formatted.
	 */
	@Override
	public void marshall(final T t, final OutputStream os)
			throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(c);
		Marshaller mar = jc.createMarshaller();
		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		mar.marshal(t, os);
	}

	/**
	 * simple unmarshalling method.
	 */
	@Override
	public T unmarshall(final InputStream is) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(c);
		Unmarshaller um = jc.createUnmarshaller();
		return (T) um.unmarshal(is);
	}

}
