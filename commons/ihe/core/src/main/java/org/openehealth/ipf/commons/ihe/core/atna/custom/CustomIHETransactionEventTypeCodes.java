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

    public static final class MultiPatientQuery extends IHETransactionEventTypeCodes {
        public MultiPatientQuery() {
            super("ITI-51", "Multi-Patient Stored Query");
        }
    }

    public static final class PatientLocationQuery extends IHETransactionEventTypeCodes {
        public PatientLocationQuery() {
            super("ITI-56", "Patient Location Query");
        }
    }

    public static final class UpdateDocumentSet extends IHETransactionEventTypeCodes {
        public UpdateDocumentSet() {
            super("ITI-57", "Update Document Set");
        }
    }

    public static final class ProviderInformationFeed extends IHETransactionEventTypeCodes {
        public ProviderInformationFeed() {
            super("ITI-59", "Provider Information Feed");
        }
    }

    public static final class RegisterOnDemandDocumentEntry extends IHETransactionEventTypeCodes {
        public RegisterOnDemandDocumentEntry() {
            super("ITI-61", "Register On-Demand Document Entry");
        }
    }

    public static final class RemoveMetadata extends IHETransactionEventTypeCodes {
        public RemoveMetadata() {
            super("ITI-62", "Remove Metadata");
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

    public static final class ProvideDocumentBundle extends IHETransactionEventTypeCodes {
        public ProvideDocumentBundle() {
            super("ITI-65", "Provide Document Bundle");
        }
    }

    public static final class DocumentManifestQuery extends IHETransactionEventTypeCodes {
        public DocumentManifestQuery() {
            super("ITI-66", "Mobile Document Manifest Query");
        }
    }

    public static final class DocumentReferenceQuery extends IHETransactionEventTypeCodes {
        public DocumentReferenceQuery() {
            super("ITI-67", "Mobile Document Reference Query");
        }
    }

    public static final class PDQMQuery extends IHETransactionEventTypeCodes {
        public PDQMQuery() {
            super("ITI-78", "Mobile Patient Demographics Query");
        }
    }

    public static final class PIXMQuery extends IHETransactionEventTypeCodes {
        public PIXMQuery() {
            super("ITI-83", "Mobile Patient Identifier Cross-reference Query");
        }
    }

    public static final class RemoveDocuments extends IHETransactionEventTypeCodes {
        public RemoveDocuments() {
            super("ITI-86", "Remove Documents");
        }
    }

    public static final class CrossGatewayUpdateDocumentSet extends IHETransactionEventTypeCodes {
        public CrossGatewayUpdateDocumentSet() {
            super("ITI-X1", "Cross-Gateway Update Document Set");
        }
    }

    public static final class RetrieveImagingDocumentSet extends IHETransactionEventTypeCodes {
        public RetrieveImagingDocumentSet() {
            super("RAD-69", "Retrieve Imaging Document Set");
        }
    }

    public static final class CrossGatewayRetrieveImagingDocumentSet extends IHETransactionEventTypeCodes {
        public CrossGatewayRetrieveImagingDocumentSet() {
            super("RAD-75", "Cross Gateway Retrieve Imaging Document Set");
        }
    }

    public static final class QueryExistingData extends IHETransactionEventTypeCodes {
        public QueryExistingData() {
            super("PCC-1", "Query Existing Data");
        }
    }

}


