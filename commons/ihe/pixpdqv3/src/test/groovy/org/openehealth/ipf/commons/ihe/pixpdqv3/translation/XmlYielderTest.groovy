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
package org.openehealth.ipf.commons.ihe.pixpdqv3.translation

import static Utils.*
import static XmlYielder.*

import groovy.xml.MarkupBuilderimport org.junit.BeforeClassimport org.junit.Test

import org.custommonkey.xmlunit.XMLUnit
import org.custommonkey.xmlunit.Diffimport org.custommonkey.xmlunit.DetailedDiffimport org.custommonkey.xmlunit.Differenceimport org.custommonkey.xmlunit.DifferenceConstants

/**
 * Unit test for GPath-to-XMLBuilder content yielding.
 * @author Dmytro Rud
 */
class XmlYielderTest {

    @BeforeClass
    static void setUpClass() {
        XMLUnit.setCompareUnmatched(true)
        XMLUnit.setIgnoreAttributeOrder(true)
        XMLUnit.setIgnoreComments(true)
        XMLUnit.setIgnoreWhitespace(true)
    }

    @Test
    void testXmlYield() {
        def sourceText = '''
            <!-- default NS with prefix -->
            <urn:envelope xmlns:urn="urn:hl7-org:v3">

                <!-- locally defined NS without prefix -->
                <element attrib="value" xmlns="abcd">

                    <!-- default NS with prefix in an enemy context -->
                    <urn:child1 /> 

                    <!-- locally defined NS with prefix -->
                    <prefix:child2 xmlns:prefix="http://utiputi">
                        text content
                    </prefix:child2>

                    <!-- empty element without namespace prefix, attribute witl prefix, comment -->
                    <child3 urn:attr2="3"><!-- empty --></child3>

                    <prefix:child4 xmlns:prefix="http://figlimigli">
                        <prefix:child5 xmlns:prefix="http://utiputi" />
                    </prefix:child4>
                </element>
            </urn:envelope>
        '''
        
        def expectedTargetText = '''
            <rootElement xmlns="urn:hl7-org:v3">
                <childElement>
                    <ns1:element attrib="value" xmlns:ns1="abcd">
                        <child1></child1> 
                        <ns2:child2 xmlns:ns2="http://utiputi">
                            text content
                        </ns2:child2>
                        <ns1:child3 xmlns:ns2="urn:hl7-org:v3" ns2:attr2="3"></ns1:child3>
                        <ns2:child4 xmlns:ns2="http://figlimigli">
                            <ns3:child5 xmlns:ns3="http://utiputi" />
                        </ns2:child4>
                    </ns1:element>
                </childElement>
            </rootElement>
        '''

        def source  = slurp(sourceText)
        def output = new ByteArrayOutputStream()
        def builder = getBuilder(output)
        
        String defaultNs = 'urn:hl7-org:v3' 
        builder.rootElement(xmlns: defaultNs) {
            childElement {
                yieldElement(source.element, builder, defaultNs)
            }
        }

        Diff diff = new Diff(expectedTargetText, output.toString())
        assert diff.identical()
    }
    
}
