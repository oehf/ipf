/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.core;

import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;

/**
 * This is meant for IHE transactions that differ depending on what option(s) are chosen.
 * Usually there is a constant TransactionOptionsProvider for a specific transaction, but
 * for customization purposes it would be possible to assign a different one.
 *
 * @author Christian Ohr
 */
public interface TransactionOptionsProvider<S extends AuditDataset, T extends Enum<T> & TransactionOptions<?>> {

    /**
     * @return the class that lists the options for a transaction
     */
    Class<T> getTransactionOptionsType();

    /**
     * @return the default option
     */
    T getDefaultOption();

    /**
     *
     * @param serverSide true for server-side auditing, false for client-side
     * @return audit strategy for the transaction
     */
    AuditStrategy<S> getAuditStrategy(boolean serverSide);

}
