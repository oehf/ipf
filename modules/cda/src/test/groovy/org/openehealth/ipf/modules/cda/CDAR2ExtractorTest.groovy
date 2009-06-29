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
package org.openehealth.ipf.modules.cda

import org.openehealth.ipf.modules.cda.CDAR2Parser
import org.openehealth.ipf.modules.cda.builder.CDAR2ModelExtension
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*

import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.modules.cda.builder.CDAR2ModelExtension

/**
 * @author Christian Ohr
 */
public class CDAR2ExtractorTest {

  private CDAR2Parser parser;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    ExpandoMetaClass.enableGlobally()
    CDAR2ModelExtension extensions = new CDAR2ModelExtension()
	 extensions.extensions.call()
  }

  @Before
  public void setUp() throws Exception {
    parser = new CDAR2Parser();
  }

  /*
   * This test reads an CDA document and parses it into the OHT CDAR2
   * object model. You can use GPath expressions to navigate or select
   * elements from the document. All custom extensions to the CDA
   * object model are available.
   */
  @Test
  public void testExtractFromSampleDocument() throws IOException {
    InputStream is = getClass().getResourceAsStream(
            "/SampleCDADocument.xml");
    def clinicalDocument = parser.parse(is);
    assertNotNull(clinicalDocument);
    def components = clinicalDocument.structuredComponents

    // Simple navigation
    assertEquals('en-US', clinicalDocument.languageCode.code)
    assertEquals('KP00017', clinicalDocument.author[0].assignedAuthor.id[0].extension)

    // Avoid NullPointerException by with safe dereferencing using the ?. operator
    assertEquals('KP00017', clinicalDocument?.author[0].assignedAuthor.id[0].extension)
    def clinicalDocument2 = null
    assertNull(clinicalDocument2?.languageCode?.code)


    // Use any(Closure) to check if the predicate is true at least once
    assertTrue(components.any { it.section.code.code == '10164-2' })

    // Use every(Closure) to check if the predicate is always true
    assertTrue(components.every { it.section.code.codeSystem == '2.16.840.1.113883.6.1' })

    // Use find(Closure) to return the first value matching the closure condition    
    assertEquals('History of Present Illness',
            components.find { it.section.code.code == '10164-2' }?.section.title.text)

    // Use findAll to return all values matching the closure condition
    assertEquals(1, components.findAll { it.section.code.code == '10164-2' }.size())

    // Use findIndexOf to return the index of the first item that matches the
    // condition specified in the closure.
    assertEquals(1, components.findIndexOf { it.section.code.code == '10153-2' })

    // Use collect to iterate through an object transforming each value into a
    // new value using the closure as a transformer, returning a list of transformed values. 
    assertEquals([
            'History of Present Illness',
            'Past Medical History',
            'Medications',
            'Allergies and Adverse Reactions',
            'Family history',
            'Social History',
            'Physical Examination',
            'Labs',
            'In-office Procedures',
            'Assessment',
            'Plan'],
            components.collect { it.section.title.text })

    // The spread operator parent*.action is equivalent to
    // parent.collect{ child -> child?.action }
    assertEquals([
            'History of Present Illness',
            'Past Medical History',
            'Medications',
            'Allergies and Adverse Reactions',
            'Family history',
            'Social History',
            'Physical Examination',
            'Labs',
            'In-office Procedures',
            'Assessment',
            'Plan'],
            components.section.title.text)

  }

  /*
   * This test reads an CDA document using Groovy's XMLSlurper and parses it
   * into a generic GPathResult. Again, you can use GPath expressions plus
   * GPathResult methods to extract individual parts. No custom extensions
   * of the OHT CDA model are available here. This is 20x faster, too.
   */
  @Test
  public void testExtractFromSlurpedDocument() throws IOException {
    InputStream is = getClass().getResourceAsStream(
            "/SampleCDADocument.xml");
    def clinicalDocument = new XmlSlurper().parse(is)

    def components = clinicalDocument.component.structuredBody.component

    // Simple navigation
    assertEquals('en-US', clinicalDocument.languageCode.@code.text())
    assertEquals('KP00017', clinicalDocument.author[0].assignedAuthor.id[0].@extension.text())

    // Avoid NullPointerException by with safe dereferencing using the ?. operator
    assertEquals('KP00017', clinicalDocument?.author[0].assignedAuthor.id[0].@extension.text())
    def clinicalDocument2 = null
    assertNull(clinicalDocument2?.languageCode?.@code?.text())


    // Use any(Closure) to check if the predicate is true at least once
    assertTrue(components.any { it.section.code.@code == '10164-2' })

    // Use every(Closure) to check if the predicate is always true
    assertTrue(components.every { it.section.code.@codeSystem == '2.16.840.1.113883.6.1' })

    // Use find(Closure) to return the first value matching the closure condition
    assertEquals('History of Present Illness',
            components.find { it.section.code.@code == '10164-2' }.section.title.text())

    // Use findAll to return all values matching the closure condition
    assertEquals(1, components.findAll { it.section.code.@code == '10164-2' }.size())

    // Use findIndexOf to return the index of the first item that matches
    // the condition specified in the closure.
    assertEquals(1, components.findIndexOf { it.section.code.@code == '10153-2' })

    // Use collect to iterate through an object transforming each value into a
    // new value using the closure as a transformer, returning a list of transformed values.
    assertEquals([
            'History of Present Illness',
            'Past Medical History',
            'Medications',
            'Allergies and Adverse Reactions',
            'Family history',
            'Social History',
            'Physical Examination',
            'Labs',
            'In-office Procedures',
            'Assessment',
            'Plan'],
            components.collect { it.section.title.text() })

    // The spread operator parent*.action is equivalent to
    // parent.collect{ child -> child?.action }
    assertEquals([
            'History of Present Illness',
            'Past Medical History',
            'Medications',
            'Allergies and Adverse Reactions',
            'Family history',
            'Social History',
            'Physical Examination',
            'Labs',
            'In-office Procedures',
            'Assessment',
            'Plan'],
            components.section.title*.text())

    // Use depthFirst (or '**') to search for elements anywhere in
    // the structure
    def drugCodes = clinicalDocument.depthFirst().findAll
      { it.name() == "manufacturedLabeledDrug" }.code*.@code
    
    assertEquals([
            '66493003',
            '91143003',
            '10312003',
            '376209006',
            '10312003',
            '331646005' ],
            drugCodes*.text())

    // Use of helper functions to encapsulate commonly used GPath expressions
    def drugCodes2 = findAllElements(clinicalDocument, "manufacturedLabeledDrug").code*.@code
    assertEquals(drugCodes, drugCodes2)
  }

  private Collection findAllElements(GPathResult result, String name) {
    return result.depthFirst().findAll { it.name() == name }
  }

}