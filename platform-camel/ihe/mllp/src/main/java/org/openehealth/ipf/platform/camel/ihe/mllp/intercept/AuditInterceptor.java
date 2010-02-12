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
package org.openehealth.ipf.platform.camel.ihe.mllp.intercept;

import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.ihe.mllp.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.MllpAuditStrategy;

/**
 * Interface for PIX/PDQ auditing interceptors.
 * @author Dmytro Rud
 */
public interface AuditInterceptor extends MllpInterceptor {

    /**
     * Determines local and remote network addresses on the basis of the
     * given exchange and stores them into the given audit dataset.   
     */
    public void determineParticipantsAddresses(
            Exchange exchange, 
            MllpAuditDataset auditDataset) throws Exception;

    /**
     * Returns the audit strategy instance configured for this interceptor.
     */
    public MllpAuditStrategy getAuditStrategy();
}
