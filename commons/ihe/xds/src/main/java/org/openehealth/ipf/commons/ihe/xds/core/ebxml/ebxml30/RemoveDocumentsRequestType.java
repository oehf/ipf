/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Java class for RemoveDocumentsRequestType complex type.
 *
 * @since 3.3
 */
@XmlRootElement(name = "RemoveDocumentSetRequest")
public class RemoveDocumentsRequestType extends RetrieveDocumentSetRequestType {
    // differences only in JAXB annotations
}
