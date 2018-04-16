/*
 * Copyright 2018 the original author or authors.
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
