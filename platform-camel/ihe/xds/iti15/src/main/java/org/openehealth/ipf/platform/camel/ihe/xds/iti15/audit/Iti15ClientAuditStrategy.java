/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti15.audit;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Client audit strategy for ITI-15.
 * 
 * @author Dmytro Rud
 */
public class Iti15ClientAuditStrategy extends Iti15AuditStrategy {

    @Override
    public void doAudit(RFC3881EventOutcomeCodes eventOutcome, AuditDataset genericAuditDataset) {
    }

    @Override
    public AuditDataset createAuditDataset() {
        return new Iti15AuditDataset(false);
    }
}
