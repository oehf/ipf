/*
 * Copyright 2009 the original author or authors.
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
@XmlSchema(namespace = "urn:ihe:iti:xds-b:2007", 
    elementFormDefault = jakarta.xml.bind.annotation.XmlNsForm.QUALIFIED, xmlns = {
        @XmlNs(prefix = "xds", namespaceURI = "urn:ihe:iti:xds-b:2007"),
        @XmlNs(prefix = "rmd", namespaceURI = "urn:ihe:iti:rmd:2017")})
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlSchema;
