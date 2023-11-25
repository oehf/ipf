/*
 * Copyright 2023 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.xacml20;

import lombok.experimental.UtilityClass;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CV;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType;

import javax.xml.bind.JAXBElement;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
@UtilityClass
public class Xacml20AuditUtils {

    private static List<AttributeType> extractRequest(XACMLAuthzDecisionQueryType query, Function<RequestType, List<AttributeType>> extractor) {
        try {
            for (JAXBElement<?> jaxbElement : query.getRest()) {
                if (jaxbElement.getValue() instanceof RequestType) {
                    var request = (RequestType) jaxbElement.getValue();
                    List<AttributeType> attributes = extractor.apply(request);
                    return (attributes == null) ? Collections.emptyList() : attributes;
                }
            }
        } catch (Exception e) {
            // nop
        }
        return Collections.emptyList();
    }

    public static List<AttributeType> extractSubjectAttributes(XACMLAuthzDecisionQueryType query) {
        return extractRequest(query, request -> request.getSubjects().get(0).getAttributes());
    }

    public static List<AttributeType> extractResourceAttributes(XACMLAuthzDecisionQueryType query) {
        return extractRequest(query, request -> request.getResources().get(0).getAttributes());
    }

    public static List<AttributeType> extractActionAttributes(XACMLAuthzDecisionQueryType query) {
        return extractRequest(query, request -> request.getAction().getAttributes());
    }

    public static String extractStringAttributeValue(AttributeType attribute) {
        try {
            return (String) attribute.getAttributeValues().get(0).getContent().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public static ParticipantObjectIdType extractCodeAttributeValue(AttributeType attribute) {
        try {
            for (Object o : attribute.getAttributeValues().get(0).getContent()) {
                if (o instanceof JAXBElement) {
                    var value = ((JAXBElement<?>) o).getValue();
                    if (value instanceof CV) {
                        CV cv = (CV) value;
                        return ParticipantObjectIdType.of(cv.getCode(), cv.getCodeSystem(), cv.getDisplayName());
                    }
                }
            }
        } catch (Exception e) {
            // nop
        }
        return null;
    }

}
