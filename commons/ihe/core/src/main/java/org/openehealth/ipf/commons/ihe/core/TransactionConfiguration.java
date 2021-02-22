/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;

/**
 * @author Dmytro Rud
 */
@RequiredArgsConstructor
abstract public class TransactionConfiguration<T extends AuditDataset> {

    /**
     * name of the transaction
     */
    @Getter @NonNull
    private final String name;

    /**
     * description of the transaction
     */
    @Getter @NonNull
    private final String description;

    /**
     * <code>true</code> if this transaction describes a query, <code>false</code> otherwise
     */
    @Getter
    private final boolean isQuery;

    /**
     * {@link AuditStrategy} to be used on client side to accomplish ATNA audit
     */
    @Getter
    private final AuditStrategy<T> clientAuditStrategy;

    /**
     * {@link AuditStrategy} to be used on server side to accomplish ATNA audit
     */
    @Getter
    private final AuditStrategy<T> serverAuditStrategy;

    /**
     * whether strict validation rules are applied to the transaction
     */
    @Getter @Setter
    private boolean strict;
}
