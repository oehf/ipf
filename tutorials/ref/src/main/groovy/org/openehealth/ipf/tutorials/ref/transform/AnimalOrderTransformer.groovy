/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.tutorials.ref.transform

import groovy.xml.Namespaceimport groovy.text.SimpleTemplateEngine
import groovy.text.TemplateEngine
import groovy.text.Template

import javax.annotation.PostConstruct

import org.openehealth.ipf.commons.core.modules.api.Transmogrifier
import org.springframework.core.io.Resource

/**
 * @author Martin Krasser
 */
class AnimalOrderTransformer implements Transmogrifier {
     
    // --------------------------------------------------------
    //  Template setup
    // --------------------------------------------------------

    TemplateEngine engine = new SimpleTemplateEngine();
    Template template
    Resource templateResource 
    
    @PostConstruct
    void init() {
        template = engine.createTemplate(templateResource.inputStream.text)
    }

    // --------------------------------------------------------
    //  Implementation method
    // --------------------------------------------------------

    Object zap(Object order, Object... params) {
        def binding = [
            customer : order.customer.text(),
            item     : order.item.text(),
            count    : order.count.text()
        ]
        template.make(binding).toString()
    }

}