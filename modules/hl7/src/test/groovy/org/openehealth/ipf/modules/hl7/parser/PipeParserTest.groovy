/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.parser

import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.util.Terser;

/**
 * @author Marek V�clav�k
 * @author Christian Ohr
 */
public class PipeParserTest extends GroovyTestCase {

  def msgText = this.class.classLoader.getResource('msg-08.hl7')?.text
  def customPackageVersion = '2.5'
  def customPackageName = 'org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25' 
  def customGroovyPackageName = 'org.openehealth.ipf.modules.hl7.parser.groovytest.hl7v2.def.v25'
     
  void testParseWithCustomClasses() {
      def customModelClasses = [(customPackageVersion ): [customPackageName]]
      def customFactory = new CustomModelClassFactory(customModelClasses)
      def parser = new PipeParser(customFactory)      
      def hapiMessage = parser.parse(msgText)      
      assert hapiMessage.get('ZBE').getClass().getName().contains(customPackageName)
  }
    
  void testParseWithoutCustomClasses() {
      def customFactory = new CustomModelClassFactory([:])
      def parser = new PipeParser(customFactory)
      try {
    	  def hapiMessage = parser.parse(msgText)
    	  println hapiMessage.get('ZBE').getClass().getName()
      } catch (Exception e) { 
    	  assert e.getMessage().contains('ZBE does not exist')
      }     
  }
  
  void testParseWithNullCustomClasses() {
      def customFactory = new CustomModelClassFactory()
      def parser = new PipeParser(customFactory)
      try {
    	  def hapiMessage = parser.parse(msgText)
    	  println hapiMessage.get('ZBE').getClass().getName()
      } catch (Exception e) { 
    	  assert e.getMessage().contains('ZBE does not exist')
      }     
  }
  
  void testParseWithCustomGroovyClasses() {
	  def customModelClasses = [(customPackageVersion ): [customGroovyPackageName]]
	  def customFactory = new GroovyCustomModelClassFactory(customModelClasses)
	  def parser = new PipeParser(customFactory)
	  def hapiMessage = parser.parse(msgText)
	  Segment s = hapiMessage.get('ZBE')
	  assert s.class.name.contains(customGroovyPackageName)
	  assert '1234' == Terser.get(s, 1, 0, 1, 1)
  }
	
  void testParseWithoutCustomGroovyClasses() {
	  def customFactory = new GroovyCustomModelClassFactory([:])
	  def parser = new PipeParser(customFactory)
	  try {
		  def hapiMessage = parser.parse(msgText)
		  println hapiMessage.get('ZBE').getClass().getName()
	  } catch (Exception e) {
		  assert e.getMessage().contains('ZBE does not exist')
	  }
  }
}
