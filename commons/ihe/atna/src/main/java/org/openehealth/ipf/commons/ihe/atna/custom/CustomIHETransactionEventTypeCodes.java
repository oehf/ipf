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
package org.openehealth.ipf.commons.ihe.atna.custom;

import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

/**
 * Audit Event ID codes for XCPD Transactions
 */
public abstract class CustomIHETransactionEventTypeCodes extends CodedValueType {

    /**
     * "IHE Transactions","ITI-55","Cross Gateway Patient Discovery"
     */
    public static final class CrossGatewayPatientDiscovery extends IHETransactionEventTypeCodes
    {
        public CrossGatewayPatientDiscovery()
        {
            super("ITI-55", "Cross Gateway Patient Discovery");
        }   
    }

    /**
     * "IHE Transactions","ITI-56","Patient Location Query"
     */
    public static final class PatientLocationQuery extends IHETransactionEventTypeCodes
    {
        public PatientLocationQuery()
        {
            super("ITI-56", "Patient Location Query");
        }   
    }

}


