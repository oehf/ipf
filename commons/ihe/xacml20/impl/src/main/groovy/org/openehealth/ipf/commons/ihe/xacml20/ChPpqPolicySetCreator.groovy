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

package org.openehealth.ipf.commons.ihe.xacml20

import jakarta.xml.bind.JAXBElement
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import org.herasaf.xacml.core.policy.impl.PolicySetType
import org.openehealth.ipf.commons.xml.XmlUtils

class ChPpqPolicySetCreator {

    private static final VelocityEngine VELOCITY
    private static final Map<String, Template> POLICY_SET_TEMPLATES

    static {
        Properties velocityProperties = new Properties()
        velocityProperties.setProperty('resource.loaders', 'classpath')
        velocityProperties.setProperty('resource.loader.classpath.class', ClasspathResourceLoader.class.getName());
        VELOCITY = new VelocityEngine(velocityProperties)
        VELOCITY.init()
        POLICY_SET_TEMPLATES = ['201', '202', '203', '301', '302', '303', '304'].collectEntries { templateId ->
            [templateId, VELOCITY.getTemplate("templates/policy-set-template-${templateId}.xml")]
        }
    }

    static String createPolicySetString(String templateId, VelocityContext substitutions) {
        Writer writer = new StringWriter()
        POLICY_SET_TEMPLATES[templateId].merge(substitutions, writer)
        return writer.toString()
    }

    static PolicySetType createPolicySet(String templateId, VelocityContext substitutions) {
        String s = createPolicySetString(templateId, substitutions)
        JAXBElement jaxbElement = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller().unmarshal(XmlUtils.source(s))
        return jaxbElement.value as PolicySetType
    }

}
