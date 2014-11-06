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

import org.junit.Test
import org.springframework.core.io.ClassPathResource

/**
 * @author Martin Krasser
 */
class AnimalOrderTransformerTest extends groovy.test.GroovyAssert {

    @Test
    void testSomething() {
        
        def input = new ClassPathResource('order/order-animals.xml').inputStream.text
        def result = new ClassPathResource('order/order-animals-transformed.txt').inputStream.text.replace('\r\n', '\n')
        def node = new XmlParser(false, false).parseText(input)
        
        def transformer = new AnimalOrderTransformer()
        transformer.templateResource = new ClassPathResource('order/order.template')
        transformer.init()

        def expectedResult = transformer.zap(node)
        assertEquals(result, expectedResult)
        
    }
    
}