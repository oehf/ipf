/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Represents a stored query for SubscriptionForPatientIndependentDocumentEntryQuery used for ITI-52 filters
 * @author Christian Ohr
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubscriptionForPatientIndependentDocumentEntryQuery")
@XmlRootElement(name = "subscriptionForPatientIndependentDocumentEntryQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class SubscriptionForPatientIndependentDocumentEntryQuery extends DocumentsQuery {

    public SubscriptionForPatientIndependentDocumentEntryQuery() {
        super(QueryType.SUBSCRIPTION_FOR_PATIENT_INDEPENDENT_DOCUMENT_ENTRY);
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
