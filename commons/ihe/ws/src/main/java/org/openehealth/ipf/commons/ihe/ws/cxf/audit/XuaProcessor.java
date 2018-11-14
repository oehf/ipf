/*
 * Copyright 2016 the original author or authors.
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
 * @author Dmytro Rud
 */
public interface XuaProcessor {

    /**
     * Enriches the given audit dataset with elements from the XUA token (SAML2 assertion)
     * contained in the given CXF message.
     *
     * @param message
     *      source CXF message.
     * @param headerDirection
     *      direction of the header containing the SAML2 assertion.
     * @param auditDataset
     *      target ATNA audit dataset.
     */
    void enrichAuditDatasetFromXuaToken(
            SoapMessage message,
            Header.Direction headerDirection,
            WsAuditDataset auditDataset);

    XuaProcessor NOOP = (message, headerDirection, auditDataset) -> {};
}
