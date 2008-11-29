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

import org.openehealth.ipf.commons.core.modules.api.Transmogrifier

/**
 * @author Martin Krasser
 */
class AnimalOrderTransformer implements Transmogrifier {
 
    // --------------------------------------------------------
    //  Support for namespaces
    // --------------------------------------------------------
    
    def oehf = new Namespace("http://www.openehealth.org/tutorial", 'oehf')
    
    // --------------------------------------------------------
    //  Template setup
    // --------------------------------------------------------

    def engine = new SimpleTemplateEngine();
    def template = engine.createTemplate(
            
'''
Order
-----
Customer: ${customer}
Item:     ${item}
Count:    ${count}
'''

    );
    
    // --------------------------------------------------------
    //  Implementation method
    // --------------------------------------------------------

    Object zap(Object input, Object... params) {
        def order = new XmlParser().parseText(input)
        def binding = [
            customer : order[oehf.customer].text(),
            item     : order[oehf.item].text(),
            count    : order[oehf.count].text()
        ]
        template.make(binding).toString()
    }

}