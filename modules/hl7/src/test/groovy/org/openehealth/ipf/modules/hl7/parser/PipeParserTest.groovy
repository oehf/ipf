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

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.ModelClassFactory
import ca.uhn.hl7v2.parser.Parser
import org.junit.Test;

import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.util.Terser;

/**
 * @author Marek Vaclavik
 * @author Christian Ohr
 */
public class PipeParserTest {

  def msgText = this.class.classLoader.getResource('msg-08.hl7')?.text
  def customPackageVersion = '2.5'
  def customPackageName = 'org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25' 
  def customGroovyPackageName = 'org.openehealth.ipf.modules.hl7.parser.groovytest.hl7v2.def.v25'
     
  @Test
  void testParseWithCustomClasses() {
      Map<String, String[]> customModelClasses = [(customPackageVersion): [customPackageName] as String[]]
      ModelClassFactory customFactory = new CustomModelClassFactory(customModelClasses)
      Parser parser = new DefaultHapiContext(customFactory).getPipeParser()
      Message hapiMessage = parser.parse(msgText)
      assert hapiMessage.ZBE.class.name.contains(customPackageName)
  }
  
  @Test
  void testParseWithoutCustomClasses() {
      ModelClassFactory customFactory = new CustomModelClassFactory([:])
      Parser parser = new DefaultHapiContext(customFactory).getPipeParser()
      try {
          Message hapiMessage = parser.parse(msgText)
    	  println hapiMessage.ZBE.class.name
      } catch (Exception e) { 
    	  assert e.getMessage().contains('ZBE does not exist')
      }     
  }
  
  @Test
  void testParseWithNullCustomClasses() {
      ModelClassFactory customFactory = new CustomModelClassFactory()
      Parser parser = new DefaultHapiContext(customFactory).getPipeParser()
      try {
          Message hapiMessage = parser.parse(msgText)
          println hapiMessage.ZBE.class.name
      } catch (Exception e) { 
    	  assert e.getMessage().contains('ZBE does not exist')
      }     
  }
  
  @Test
  void testParseWithCustomGroovyClasses() {
	  def customModelClasses = [(customPackageVersion ): [customGroovyPackageName]]
	  ModelClassFactory customFactory = new GroovyCustomModelClassFactory(customModelClasses)
      Parser parser = new DefaultHapiContext(customFactory).getPipeParser()
	  Message hapiMessage = parser.parse(msgText)
	  Segment s = hapiMessage.ZBE
	  assert s.class.name.contains(customGroovyPackageName)
	  assert '1234' == Terser.get(s, 1, 0, 1, 1)
  }
	
  @Test
  void testParseWithoutCustomGroovyClasses() {
      ModelClassFactory customFactory = new GroovyCustomModelClassFactory([:])
      Parser parser = new DefaultHapiContext(customFactory).getPipeParser()
	  try {
          Message hapiMessage = parser.parse(msgText)
          println hapiMessage.ZBE.class.name
	  } catch (Exception e) {
		  assert e.getMessage().contains('ZBE does not exist')
	  }
  }
}
