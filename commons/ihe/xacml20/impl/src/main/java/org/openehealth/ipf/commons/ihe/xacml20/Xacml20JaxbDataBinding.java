package org.openehealth.ipf.commons.ihe.xacml20;

import com.sun.xml.bind.marshaller.MinimumEscapeHandler;
import org.apache.cxf.jaxb.JAXBDataBinding;

/**
 * Prevents the usage of com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput.NewLineEscapeHandler
 * introduced in Glassfish JAXB 2.3.0.
 *
 * @author Dmytro Rud
 */
public class Xacml20JaxbDataBinding extends JAXBDataBinding {

    public Xacml20JaxbDataBinding() {
        super();

        // the key is the constant com.sun.xml.bind.v2.runtime.MarshallerImpl.ENCODING_HANDLER
        getMarshallerProperties().put("com.sun.xml.bind.characterEscapeHandler", MinimumEscapeHandler.theInstance);
    }

}
