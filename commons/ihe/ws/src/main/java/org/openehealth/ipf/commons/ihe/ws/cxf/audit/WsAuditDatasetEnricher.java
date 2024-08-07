/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.audit;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;

/**
 * Interface for Web Service ATNA audit dataset enrichers.
 * Each implementing class shall have a default constructor and be thread-safe.
 *
 * @author Dmytro Rud
 */
public interface WsAuditDatasetEnricher extends org.openehealth.ipf.commons.audit.WsAuditDatasetEnricher {

    /**
     * Enriches the given audit dataset with elements from the given CXF request message.
     *
     * @param message         CXF request message.
     * @param headerDirection direction of SOAP headers.
     * @param auditDataset    target ATNA audit dataset.
     */
    void enrichAuditDatasetFromRequest(SoapMessage message, Header.Direction headerDirection, WsAuditDataset auditDataset);

    /**
     * Enriches the given audit dataset with elements from the given CXF response message.
     *
     * @param message         CXF response  message.
     * @param headerDirection direction of SOAP headers.
     * @param auditDataset    target ATNA audit dataset.
     */
    void enrichAuditDatasetFromResponse(SoapMessage message, Header.Direction headerDirection, WsAuditDataset auditDataset);

}
