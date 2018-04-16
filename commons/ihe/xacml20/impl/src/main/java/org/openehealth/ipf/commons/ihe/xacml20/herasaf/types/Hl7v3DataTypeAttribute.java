package org.openehealth.ipf.commons.ihe.xacml20.herasaf.types;

import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.ObjectFactory;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.AbstractDataTypeAttribute;
import org.openehealth.ipf.commons.xml.XmlUtils;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * @author Dmytro Rud
 */
abstract public class Hl7v3DataTypeAttribute<T> extends AbstractDataTypeAttribute<T> {

    private final Class<T> typeClass;

    private static final JAXBContext JAXB_CONTEXT;
    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public Hl7v3DataTypeAttribute(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    public T convertTo(String jaxbRepresentation) throws SyntaxException {
        try {
            JAXBElement<T> jaxbElement = (JAXBElement) JAXB_CONTEXT.createUnmarshaller().unmarshal(XmlUtils.source(jaxbRepresentation));
            return jaxbElement.getValue();
        } catch (JAXBException e) {
            throw new SyntaxException(e);
        }
    }

    @Override
    public T convertTo(List<?> list) throws SyntaxException {
        try {
            // try with JAXB elements, optionally accompanied with strings (most probably spaces and line feeds)
            for (Object o : list) {
                if (o instanceof Node) {
                    o = JAXB_CONTEXT.createUnmarshaller().unmarshal((Node) o);
                }
                Object pojo = (o instanceof JAXBElement) ? ((JAXBElement) o).getValue() : o;
                if (typeClass.isAssignableFrom(pojo.getClass())) {
                    return (T) pojo;
                }
            }
            // fallback to string
            return convertTo((String) list.get(0));
        } catch (Exception e) {
            throw new SyntaxException(e);
        }
    }
}
