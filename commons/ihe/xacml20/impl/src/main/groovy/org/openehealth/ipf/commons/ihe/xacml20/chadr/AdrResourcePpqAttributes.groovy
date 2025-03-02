/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xacml20.chadr

import groovy.transform.CompileStatic
import org.herasaf.xacml.core.context.impl.ResourceType
import org.herasaf.xacml.core.dataTypeAttribute.impl.AnyURIDataTypeAttribute
import org.herasaf.xacml.core.dataTypeAttribute.impl.DateDataTypeAttribute
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.types.IiDataTypeAttribute
import org.openehealth.ipf.commons.ihe.xacml20.model.EprConstants.AttributeIds

import java.time.LocalDate

import static org.openehealth.ipf.commons.ihe.xacml20.chadr.AdrUtils.toIi

/**
 * Attributes of the element "Resource" of an ADR request for trigger event PPQ.
 */
@CompileStatic
class AdrResourcePpqAttributes extends AdrAttributes<ResourceType> {

    private final String policySetId
    private final String eprSpid
    private final String referencedPolicySet
    private final String fromDate
    private final String toDate

    AdrResourcePpqAttributes(String policySetId, String eprSpid, String referencedPolicySet, LocalDate fromDate, LocalDate toDate) {
        this.policySetId = Objects.requireNonNull(policySetId)
        this.eprSpid = Objects.requireNonNull(eprSpid)
        this.referencedPolicySet = Objects.requireNonNull(referencedPolicySet)
        this.fromDate = AdrUtils.formatDate(fromDate)
        this.toDate = AdrUtils.formatDate(toDate)
    }

    @Override
    List<ResourceType> createAdrRequestParts() {
        def result = new ResourceType()
        add(result.attributes, AttributeIds.XACML_1_0_RESOURCE_ID, new AnyURIDataTypeAttribute(), policySetId)
        add(result.attributes, AttributeIds.EHEALTH_SUISSSE_2015_EPR_SPID, new IiDataTypeAttribute(), toIi(eprSpid))
        add(result.attributes, AttributeIds.EHEALTH_SUISSSE_2015_REFERENCED_POLICY_SET, new AnyURIDataTypeAttribute(), referencedPolicySet)
        add(result.attributes, AttributeIds.EHEALTH_SUISSSE_START_DATE, new DateDataTypeAttribute(), fromDate)
        add(result.attributes, AttributeIds.EHEALTH_SUISSSE_END_DATE, new DateDataTypeAttribute(), toDate)
        return [result]
    }
    
}
