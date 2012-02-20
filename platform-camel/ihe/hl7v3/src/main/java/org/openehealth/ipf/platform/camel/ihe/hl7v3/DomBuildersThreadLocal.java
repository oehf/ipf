/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Thread local for DOM document builders.
 * @author Dmytro Rud
 */
class DomBuildersThreadLocal extends ThreadLocal<DocumentBuilder> {

    public DocumentBuilder initialValue() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            return factory.newDocumentBuilder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentBuilder get() {
        DocumentBuilder builder = (DocumentBuilder) super.get();
        builder.reset();
        return builder;
    }
}
