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
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.types.IiDataTypeAttribute
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants

import static org.openehealth.ipf.commons.ihe.xacml20.chadr.AdrUtils.toIi

@CompileStatic
class AdrResourceAtcAttributes extends AdrAttributes<ResourceType> {

    private final String eprSpid

    AdrResourceAtcAttributes(String eprSpid) {
        this.eprSpid = Objects.requireNonNull(eprSpid)
    }

    @Override
    List<ResourceType> createAdrRequestParts() {
        def result = new ResourceType()
        add(result.attributes, PpqConstants.AttributeIds.XACML_1_0_RESOURCE_ID, new AnyURIDataTypeAttribute(), "urn:e-health-suisse:2015:epr-subset:${eprSpid}:patient-audit-trail-records".toString())
        add(result.attributes, PpqConstants.AttributeIds.EHEALTH_SUISSSE_2015_EPR_SPID, new IiDataTypeAttribute(), toIi(eprSpid))
        return [result]
    }

}
