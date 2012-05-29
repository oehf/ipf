/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

/**
 * Audit Event ID codes for custom eHealth Transactions
 */
public abstract class CustomIHETransactionEventTypeCodes extends CodedValueType {

    public static final class PatientLocationQuery extends IHETransactionEventTypeCodes {
        public PatientLocationQuery() {
            super("ITI-56", "Patient Location Query");
        }   
    }

    public static final class RegisterOnDemandDocumentEntry extends IHETransactionEventTypeCodes {
        public RegisterOnDemandDocumentEntry() {
            super("ITI-61", "Register On-Demand Document Entry");
        }
    }

    public static final class CrossCommunityFetch extends IHETransactionEventTypeCodes {
        public CrossCommunityFetch() {
            super("ITI-63", "XCF Fetch");
        }
    }

    public static final class CrossCommunityFetchIntermediateDocumentCreation extends IHETransactionEventTypeCodes {
        public CrossCommunityFetchIntermediateDocumentCreation() {
            super("ITI-63", "XCF Fetch Intermediate Document Creation");
        }
    }

    public static final class NotifyXadPidLinkChange extends IHETransactionEventTypeCodes {
        public NotifyXadPidLinkChange() {
            super("ITI-64", "Notify XAD-PID Link Change");
        }
    }

    public static final class QueryExistingData extends IHETransactionEventTypeCodes {
        public QueryExistingData() {
            super("PCC-1", "Query Existing Data");
        }
    }
}


