/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.stub.json;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*;

import java.util.List;

/**
 * Intermediary representation of an HPD {@link BatchResponse} required because the JSON deserializer cannot handle lists of JAXBElements.
 * <p>
 * Usage:
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     BatchResponseIntermediary intermediary = mapper.readValue(json1, BatchResponseIntermediary.class);
 *     BatchResponse batchResponse = batchResponseIntermediary.toBatchResponse();
 * </pre>
 *
 * @author Dmytro Rud
 */
public class BatchResponseIntermediary {

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    @Getter
    @Setter
    private List<BatchResponseElement> batchResponses;

    @Getter
    @Setter
    private String requestID;

    /**
     * Transforms this intermediary representation into a proper JAXB POJO.
     */
    public BatchResponse toBatchResponse() {
        var batchResponse = new BatchResponse();
        batchResponse.setRequestID(requestID);
        if (batchResponses != null) {
            for (Object response : batchResponses) {
                if (response instanceof ExtendedResponse) {
                    batchResponse.getBatchResponses().add(OBJECT_FACTORY.createBatchResponseExtendedResponse((ExtendedResponse) response));
                } else if (response instanceof SearchResponse) {
                    batchResponse.getBatchResponses().add(OBJECT_FACTORY.createBatchResponseSearchResponse((SearchResponse) response));
                } else if (response instanceof ErrorResponse) {
                    batchResponse.getBatchResponses().add(OBJECT_FACTORY.createBatchResponseErrorResponse((ErrorResponse) response));
                } else if (response instanceof LDAPResult ldapResult) {
                    switch (ldapResult.getElementName()) {
                        case "modDNResponse":
                            batchResponse.getBatchResponses().add(OBJECT_FACTORY.createBatchResponseModDNResponse(ldapResult));
                            break;
                        case "compareResponse":
                            batchResponse.getBatchResponses().add(OBJECT_FACTORY.createBatchResponseCompareResponse(ldapResult));
                            break;
                        case "delResponse":
                            batchResponse.getBatchResponses().add(OBJECT_FACTORY.createBatchResponseDelResponse(ldapResult));
                            break;
                        case "modifyResponse":
                            batchResponse.getBatchResponses().add(OBJECT_FACTORY.createBatchResponseModifyResponse(ldapResult));
                            break;
                        case "authResponse":
                            batchResponse.getBatchResponses().add(OBJECT_FACTORY.createBatchResponseAuthResponse(ldapResult));
                            break;
                        case "addResponse":
                            batchResponse.getBatchResponses().add(OBJECT_FACTORY.createBatchResponseAddResponse(ldapResult));
                            break;
                        default:
                            throw new IllegalStateException("Cannot handle LDAPResult element name " + ldapResult.getElementName());
                    }
                } else {
                    throw new IllegalStateException("Cannot handle class " + response.getClass().getName());
                }
            }
        }
        return batchResponse;
    }

}
