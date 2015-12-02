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

package org.openehealth.ipf.platform.camel.ihe.atna;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;

/**
 * Endpoint that supports ATNA auditing. Usually the {@link AuditStrategy}
 * instances are obtained from the accompanying {@link AuditableComponent}.
 *
 * @since 3.1
 */
public interface AuditableEndpoint<AuditDatasetType extends AuditDataset> extends Endpoint {

    /**
     * Returns client-side audit strategy instance.
     *
     * @return client-side audit strategy instance
     */
    AuditStrategy<AuditDatasetType> getClientAuditStrategy();

    /**
     * Returns server-side audit strategy instance.
     *
     * @return server-side audit strategy instance
     */
    AuditStrategy<AuditDatasetType> getServerAuditStrategy();

    /**
     * Returns <tt>true</tt> when ATNA auditing should be performed.
     */
    boolean isAudit();
}
