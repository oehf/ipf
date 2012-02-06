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
package org.openehealth.ipf.commons.xml

import static org.openehealth.ipf.commons.xml.XmlYielder.*
import groovy.util.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import org.junit.BeforeClass
import org.junit.Test

/**
 * Unit test for GPath-to-XMLBuilder content yielding.
 * @author Dmytro Rud
 * @author Mitko Kolev
 */
class XmlYielderTest {

    @BeforeClass
    static void setUpClass() {
        XMLUnit.setCompareUnmatched(true)
        XMLUnit.setIgnoreAttributeOrder(true)
        XMLUnit.setIgnoreComments(true)
        XMLUnit.setIgnoreWhitespace(true)
    }

    private static MarkupBuilder getBuilder(StringWriter writer) {
        MarkupBuilder builder = new MarkupBuilder(writer)
        builder.setDoubleQuotes(true)
        builder.setOmitNullAttributes(true)
        builder.setOmitEmptyAttributes(true)
        return builder
    }

    @Test
    void testXmlYieldAtributeValueQnameXSIDefaultNamespace() {
        def sourceText = '''
            <!-- default NS with prefix -->
            <urn:envelope xmlns="http://www.w3.org/2001/XMLSchema-instance" xmlns:urn="urn:hl7-org:v3">
                <urn:element>
                    <urn:child type="urn:II"/>
                </urn:element>
            </urn:envelope>
        '''
        def expected = '''
            <rootElement xmlns="urn:hl7-org:v3">
                <element>
                    <child ns1:type="II" xmlns:ns1="http://www.w3.org/2001/XMLSchema-instance"/>
                </element>
            </rootElement>
        '''
        yieldAndAssertIdentical(expected, sourceText, false)
    }

    @Test
    void testXmlYieldAtributeValueQname() {
        def sourceText = '''
            <urn:envelope xmlns:urn="urn:hl7-org:v3">
                <element>
                    <child xsi:type="urn:II" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                </element>
            </urn:envelope>
        '''
        def expected = '''
            <rootElement xmlns="urn:hl7-org:v3">
                <element>
                    <child ns1:type="II" xmlns:ns1="http://www.w3.org/2001/XMLSchema-instance"/>
                </element>
            </rootElement>
        '''
        yieldAndAssertIdentical(expected, sourceText, false)
    }
    
    @Test
    void testXmlYieldAtributeValueQnameNoNamespacePrefix() {
        def sourceText = '''
            <envelope xmlns="urn:hl7-org:v3">
                <element>
                    <child xsi:type="II" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                </element>
            </envelope>
        '''
        def expected = '''
            <rootElement xmlns="urn:hl7-org:v3">
                <element>
                    <child ns1:type="II" xmlns:ns1="http://www.w3.org/2001/XMLSchema-instance"/>
                </element>
            </rootElement>
        '''
        yieldAndAssertIdentical(expected, sourceText, false)
    }
    
    @Test
    void testXmlYieldAtributeValueQnameNoNamespacePrefix2() {
        def sourceText = '''
            <envelope xmlns="urn:hl7-org:v3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <element>
                    <child xsi:type="II" />
                </element>
            </envelope>
        '''
        def expected = '''
            <rootElement xmlns="urn:hl7-org:v3">
                <element>
                    <child ns1:type="II" xmlns:ns1="http://www.w3.org/2001/XMLSchema-instance"/>
                </element>
            </rootElement>
        '''
        yieldAndAssertIdentical(expected, sourceText, false)
    }

    @Test
    void testAttributeNamespaceSpecified() {
        def sourceText = '''
            <urn:envelope xmlns:urn="urn:hl7-org:v3">
                <element attrib="value" xmlns="abcd">
                </element>
            </urn:envelope>
        '''
        def expected = '''
            <rootElement xmlns="urn:hl7-org:v3">
               <ns1:element ns1:attrib="value" xmlns:ns1="abcd"/>
            </rootElement>
        '''
        yieldAndAssertIdentical(expected, sourceText, false)
    }

    @Test
    void testAttributeNamespaceSpecified2() {
        def sourceText = '''
            <urn:envelope xmlns:urn="urn:hl7-org:v3">
                <element attrib="value" xmlns="abcd">
                    <ns1:element2 ns1:attrib="value" xmlns:ns1="ns1"/>
                </element>
            </urn:envelope>
        '''
        def expected = '''
            <rootElement xmlns="urn:hl7-org:v3">
               <ns1:element ns1:attrib="value" xmlns:ns1="abcd">
                   <ns2:element2 ns2:attrib="value" xmlns:ns2="ns1"/>
               </ns1:element>
            </rootElement>
        '''
        yieldAndAssertIdentical(expected, sourceText, false)
    }
    
    @Test
    void testAttributesNamespaceNotSpecified() {
        def sourceText = '''
            <urn:envelope xmlns:urn="urn:hl7-org:v3">
                <element attrib="value"/>
            </urn:envelope>
        '''
        def expected = '''
            <rootElement xmlns="urn:hl7-org:v3">
                <element attrib="value"/>
            </rootElement>
        '''
        yieldAndAssertIdentical(expected, sourceText, false)
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
        def expected = '''
            <rootElement xmlns="urn:hl7-org:v3">
                <childElement>
                    <ns1:element ns1:attrib="value" xmlns:ns1="abcd">
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
        yieldAndAssertIdentical(expected, sourceText, true)
    }

    private yieldAndAssertIdentical(String expected, String yielded, boolean useChildElement) {
        GPathResult source = new XmlSlurper(false, true).parseText(yielded)
        Writer writer = new StringWriter()
        MarkupBuilder builder = getBuilder(writer)

        String defaultNs = 'urn:hl7-org:v3'
        builder.rootElement(xmlns: defaultNs) {
            if (useChildElement){
                childElement {
                    yieldElement(source.element, builder, defaultNs)
                }
            } else {
                yieldElement(source.element, builder, defaultNs)
            }
        }
        Diff diff = new Diff(expected, writer.toString())
        assert diff.identical()
    }

    @Test
    void testMissingSource() {
        GPathResult source = new XmlSlurper(false, true).parseText('<abc />')
        Writer writer = new StringWriter()
        MarkupBuilder builder = getBuilder(writer)
        builder.element() {}
        yieldChildren(source, builder, 'urn:dummy-ns')
        assert writer.toString() == '<element />'
    }
}
