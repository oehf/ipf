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
package org.openehealth.ipf.modules.hl7.validation.builder
import groovy.util.GroovyTestCase

/**
 * @author Christian Ohr
 */
public class VersionBuilderTest extends GroovyTestCase {
    
     void testType() {
         def builder = new VersionBuilder('2.4', null).primitiveType('ST')
         assert builder.version == '2.4'
         assert builder.typeName == 'ST'
     }     
     
     void testMessage() {
         def builder = new VersionBuilder('2.4', null).message('ADT', 'A01')
         assert builder.version == '2.4'
         assert builder.messageType == 'ADT'
         assert builder.triggerEvent == 'A01'
     }
     
     void testMessage2() {
         def builder = new VersionBuilder('2.4', null).message('ADT', 'A01 A02 A03')
         assert builder.version == '2.4'
         assert builder.messageType == 'ADT'
         assert builder.triggerEvent == 'A01 A02 A03'
     }
     
     void testMessage3() {
         def builder = new VersionBuilder('2.4', null).message('ADT', ['A01','A02','A03'])
         assert builder.version == '2.4'
         assert builder.messageType == 'ADT'
         assert builder.triggerEvent == ['A01','A02','A03']
     }
     
     void testEncoding() {
         def builder = new VersionBuilder('2.4', null).encoding('blorg')
         assert builder.version == '2.4'
         assert builder.encoding == 'blorg'
     }     

}
