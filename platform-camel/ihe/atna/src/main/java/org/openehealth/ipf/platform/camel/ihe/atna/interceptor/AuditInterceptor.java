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
package org.openehealth.ipf.platform.camel.ihe.atna.interceptor;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;

/**
 * Interface for Auditing interceptors. Usually the {@link AuditStrategy} is obtained
 * from the {@link AuditableEndpoint}
 *
 * @author Christian Ohr
 * @since 3.1
 */
public interface AuditInterceptor<T extends AuditDataset, E extends AuditableEndpoint<T>> extends Interceptor<E> {

    /**
     * Returns the audit strategy instance configured for this interceptor.
     */
    AuditStrategy<T> getAuditStrategy();

    /**
     * Determines local and remote network addresses on the basis of the
     * given exchange and stores them into the given audit dataset.
     */
    void determineParticipantsAddresses(
            Exchange exchange,
            T auditDataset) throws Exception;
}
