/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.svs.core;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.util.JAXBSource;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;
import org.openehealth.ipf.commons.xml.XsdValidator;

import javax.xml.transform.Source;

/**
 * @author Quentin Ligier
 **/
public class SvsValidator {
    public static final JAXBContext JAXB_CONTEXT;
    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(
                org.openehealth.ipf.commons.ihe.svs.core.requests.ObjectFactory.class,
                org.openehealth.ipf.commons.ihe.svs.core.responses.ObjectFactory.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static final XsdValidator XSD_VALIDATOR = new XsdValidator();

    public static void validateIti48Request(final RetrieveValueSetRequest request) {
        validateWithXsd(request, "/wsdl/iti48/SVS.xsd");
        // Not much to do here
    }

    public static void validateIti48Response(final RetrieveValueSetResponse response) {
        validateWithXsd(response, "/wsdl/iti48/SVS.xsd");
        // Not much to do here
    }

    private static void validateWithXsd(final Object object, final String schemaName) {
        try {
            final Source source = new JAXBSource(JAXB_CONTEXT, object);
            XSD_VALIDATOR.validate(source, schemaName);
        } catch (Exception e) {
            throw new SvsException(e);
        }
    }
}
