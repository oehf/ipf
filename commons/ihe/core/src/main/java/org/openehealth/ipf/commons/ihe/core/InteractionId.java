/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core;

import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.core.atna.NoAuditStrategy;

/**
 * Interface for eHealth interactions. Implementations of this interface should ensure that instances are singletons.
 * The recommended way to achieve this is using enums.
 *
 * @author Dmytro Rud
 */
public interface InteractionId {

    /**
     * @return name of the transaction
     */
    String getName();

    /**
     * @return description of the transaction
     */
    String getDescription();

    /**
     * @return true if this transaction described a query, false otherwise
     */
    boolean isQuery();

    /**
     * @param <T> AuditDataset subtype
     * @return {@link AuditStrategy} to be used on client side to accomplish ATNA audit
     */
    default <T extends AuditDataset> AuditStrategy<T> getClientAuditStrategy() {
        return null;
    }

    /**
     * @param <T> AuditDataset subtype
     * @return {@link AuditStrategy} to be used on server side to accomplish ATNA audit
     */
    default <T extends AuditDataset> AuditStrategy<T> getServerAuditStrategy() {
        return null;
    }


    class Null implements InteractionId {

        @Override
        public String getName() {
            return "unknown";
        }

        @Override
        public String getDescription() {
            return "unknown";
        }

        @Override
        public boolean isQuery() {
            return false;
        }

        @Override
        public <T extends AuditDataset> AuditStrategy<T> getClientAuditStrategy() {
            return new NoAuditStrategy<>(false);
        }

        @Override
        public <T extends AuditDataset> AuditStrategy<T> getServerAuditStrategy() {
            return new NoAuditStrategy<>(true);
        }
    }
}
