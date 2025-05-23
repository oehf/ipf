/*
 * Copyright 2018 the original author or authors.
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
@XmlSchema(
    namespace = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol",
    elementFormDefault = jakarta.xml.bind.annotation.XmlNsForm.QUALIFIED,
    xmlns = {@XmlNs(namespaceURI = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", prefix = "xacml-samlp")}
)
package org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlSchema;