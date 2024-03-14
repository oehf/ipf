/*
 * Copyright 2020 the original author or authors.
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

import jakarta.xml.bind.annotation.*;

/**
 * Represents a stored query for FindPrescriptionsForValidationQuery (PHARM-1).
 * @author Quentin Ligier
 * @since 3.7
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindPrescriptionsForValidationQuery", propOrder = {})
@XmlRootElement(name = "findPrescriptionsForValidationQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindPrescriptionsForValidationQuery extends PharmacyStableDocumentsQuery {
    private static final long serialVersionUID = 1029367082044223870L;

    /**
     * Constructs the query.
     */
    public FindPrescriptionsForValidationQuery() {
        super(QueryType.FIND_PRESCRIPTIONS_FOR_VALIDATION);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
