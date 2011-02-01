/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xca.iti38;

import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18AuditStrategy;

/**
 * Base audit strategy for ITI-38.
 * @author Dmytro Rud
 */
abstract public class Iti38AuditStrategy extends Iti18AuditStrategy {

    public Iti38AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }

    @Override
    public void enrichDatasetFromResponse(
            Object response,
            WsAuditDataset auditDataset) throws Exception
    {
        auditDataset.setEventOutcomeCode(getEventOutcomeCode(response));
    }
}
