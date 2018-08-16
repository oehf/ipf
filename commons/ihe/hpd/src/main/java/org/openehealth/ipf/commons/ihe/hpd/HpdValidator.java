/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadResponse;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ErrorResponse.ErrorType;
import org.openehealth.ipf.commons.xml.XsdValidator;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import java.util.function.Consumer;

/**
 * @author Dmytro Rud
 */
public class HpdValidator {
    private static final JAXBContext JAXB_CONTEXT;
    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(
                    org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ObjectFactory.class,
                    org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.ObjectFactory.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static final XsdValidator XSD_VALIDATOR = new XsdValidator();

    private static void check(boolean condition, String message) {
        if (! condition) {
            throw new HpdException(message, ErrorType.MALFORMED_REQUEST);
        }
    }

    private static void validateWithXsd(Object object, String schemaName) {
        try {
            Source source = new JAXBSource(JAXB_CONTEXT, object);
            XSD_VALIDATOR.validate(source, schemaName);
        } catch (ValidationException e) {
            throw new HpdException(e, ErrorType.MALFORMED_REQUEST);
        } catch (Exception e) {
            throw new HpdException(e, ErrorType.OTHER);
        }
    }

    private static void validateBatchRequest(
            BatchRequest batch,
            Class<? extends DsmlMessage>[] allowedElementTypes,
            Consumer<DsmlMessage> requestValidator)
    {
        check(batch.getBatchRequests() != null, "Batch is null");
        check(!batch.getBatchRequests().isEmpty(), "Batch is empty");
        for(DsmlMessage dsml : batch.getBatchRequests()) {
            check(dsml != null, "Batch element is null");
            check(ArrayUtils.contains(allowedElementTypes, dsml.getClass()), "Bad batch element type " + ClassUtils.getSimpleName(dsml));
            requestValidator.accept(dsml);
        }
    }

    private static void validateSearchRequest(SearchRequest request) {
        try {
            LdapName ldapName = new LdapName(request.getDn());
            boolean oCheck = false;
            boolean dcCheck = false;
            for (Rdn rdn : ldapName.getRdns()) {
                String value = (String) rdn.getValue();
                switch (rdn.getType().toLowerCase()) {
                    case "o":
                        oCheck = StringUtils.isNotBlank(value);
                        break;
                    case "dc":
                        dcCheck = "HPD".equals(value);
                        break;
                }
            }
            check(oCheck, "Missing DN.O");
            check(dcCheck, "DN.DC not equal to 'HPD'");
        } catch (InvalidNameException e) {
            throw new HpdException(e, ErrorType.MALFORMED_REQUEST);
        }
    }

    public static void validateIti58Request(BatchRequest batchRequest) {
        validateWithXsd(batchRequest, "/schema/DSMLv2.xsd");
        validateBatchRequest(batchRequest,
                new Class[] {SearchRequest.class},
                element -> validateSearchRequest((SearchRequest) element));
    }

    public static void validateIti58Response(BatchResponse batchResponse) {
        validateWithXsd(batchResponse, "/schema/DSMLv2.xsd");
    }

    public static void validateIti59Request(BatchRequest batchRequest) {
        validateWithXsd(batchRequest, "/schema/DSMLv2.xsd");
        validateBatchRequest(batchRequest,
                new Class[] {AddRequest.class, ModifyRequest.class, ModifyDNRequest.class, DelRequest.class},
                element -> { /* TODO */ });
    }

    public static void validateIti59Response(BatchResponse batchResponse) {
        validateWithXsd(batchResponse, "/schema/DSMLv2.xsd");
    }

    public static void validateChPiddRequest(DownloadRequest downloadRequest) {
        validateWithXsd(downloadRequest, "/schema/PIDD.xsd");
    }

    public static void validateChpiddResponse(DownloadResponse downloadResponse) {
        validateWithXsd(downloadResponse, "/schema/PIDD.xsd");
        if (downloadResponse.getBatchRequest() != null) {
            for (BatchRequest batchRequest : downloadResponse.getBatchRequest()) {
                validateBatchRequest(batchRequest,
                        new Class[] {AddRequest.class, ModifyRequest.class, ModifyDNRequest.class, DelRequest.class},
                        element -> { /* TODO */ });
            }
        }
    }
}
