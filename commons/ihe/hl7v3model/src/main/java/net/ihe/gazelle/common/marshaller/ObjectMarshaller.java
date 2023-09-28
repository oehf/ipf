package net.ihe.gazelle.common.marshaller;

import java.io.InputStream;
import java.io.OutputStream;

import jakarta.xml.bind.JAXBException;

/**
 * Interface for marshallers in Gazelle.
 *
 * @author Abderrazek Boufahja
 *
 * @param <T>
 *            the kind of object we are marshalling in
 */
public interface ObjectMarshaller<T> {

	/**
	 * Marshall an object.
	 *
	 * @param t
	 *            the object to marshall
	 * @param os
	 *            the output stream
	 * @throws JAXBException
	 *             if there are a problem.
	 */
	void marshall(T t, OutputStream os) throws JAXBException;

	/**
	 * Unmarshall an InputStream as T.
	 *
	 * @param is
	 *            input stream
	 * @return an instance of T
	 * @throws JAXBException
	 *             if there are a problem
	 */
	T unmarshall(InputStream is) throws JAXBException;

}
