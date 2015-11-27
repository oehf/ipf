/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2.atna;

import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;


/**
 * ATNA audit strategy base for MLLP-based transactions. This strategy is accompanied with a
 * dedicated subclass of {@link MllpAuditDataset} containing the data for the audit record.
 * 
 * @author Dmytro Rud
 * @since 3.1
 */
abstract public class MllpAuditUtils {

    /**
     * Audits an authentication node failure.
     * @param hostAddress
     *          the address of the node that is responsible for the failure.
     */
    public static void auditAuthenticationNodeFailure(String hostAddress) {
        AuditorManager.getPIXManagerAuditor().auditNodeAuthenticationFailure(
            true, null, "IPF MLLP Component", null, hostAddress, null);
    }
    
    
}
