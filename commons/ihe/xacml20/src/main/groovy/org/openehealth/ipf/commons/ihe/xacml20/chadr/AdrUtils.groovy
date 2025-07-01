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
import jakarta.xml.bind.JAXBElement
import org.herasaf.xacml.core.context.impl.*
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute
import org.openehealth.ipf.commons.ihe.xacml20.model.EprConstants.AttributeIds
import org.openehealth.ipf.commons.ihe.xacml20.model.EprConstants.CodingSystemIds
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CE
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CV
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.II
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.ObjectFactory

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@CompileStatic
class AdrUtils {

    private static final ObjectFactory HL7V3_OBJECT_FACTORY = new ObjectFactory()

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern('yyyy-MM-dd')

    private AdrUtils() {
        throw new IllegalStateException('Cannot instantiate utility class')
    }

    static String formatDate(LocalDate date) {
        return DATE_FORMATTER.format(date)
    }

    static AttributeType createAttr(String id, DataTypeAttribute dataType, Object value) {
        return new AttributeType(
                attributeId: id,
                dataType: dataType,
                attributeValues: [new AttributeValueType(content: [value])])
    }

    static JAXBElement<CV> toCv(CE ce) {
        return HL7V3_OBJECT_FACTORY.createCodedValue(ce)
    }

    static JAXBElement<II> toIi(String eprSpid) {
        def ii = new II(extension: eprSpid, root: CodingSystemIds.SWISS_PATIENT_ID)
        return HL7V3_OBJECT_FACTORY.createInstanceIdentifier(ii)
    }

    static String extractEprSpid(RequestType request) {
        for (resource in request.resources) {
            for (attr in resource.attributes) {
                if (attr.attributeId == AttributeIds.EHEALTH_SUISSSE_2015_EPR_SPID) {
                    for (value in attr.attributeValues) {
                        for (content in value.content) {
                            if (content instanceof JAXBElement<II>) {
                                return content.value.extension
                            }
                        }
                    }
                }
            }
        }
        throw new  IllegalArgumentException('Cannot extract EPR-SPID from ADR request')
    }

    static ResultType createNotHolderOfPatientPoliciesResult() {
        return new ResultType(
                decision: DecisionType.INDETERMINATE,
                status: new StatusType(
                        statusCode: new StatusCodeType(
                                value: 'urn:e-health-suisse:2015:error:not-holder-of-patient-policies',
                        ),
                ),
        )
    }

}
