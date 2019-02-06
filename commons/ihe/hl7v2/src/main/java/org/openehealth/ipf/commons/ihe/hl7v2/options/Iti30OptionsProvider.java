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

package org.openehealth.ipf.commons.ihe.hl7v2.options;

import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionOptionsProvider;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.FeedAuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.iti30.Iti30AuditStrategy;

/**
 * @author Christian Ohr
 */
public class Iti30OptionsProvider implements Hl7v2TransactionOptionsProvider<FeedAuditDataset, Iti30Options> {

    @Override
    public Class<Iti30Options> getTransactionOptionsType() {
        return Iti30Options.class;
    }

    @Override
    public Iti30Options getDefaultOption() {
        return Iti30Options.MERGE;
    }

    @Override
    public AuditStrategy<FeedAuditDataset> getAuditStrategy(boolean serverSide) {
        return new Iti30AuditStrategy(serverSide);
    }
}
