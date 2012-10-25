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
package org.openehealth.ipf.modules.hl7.parser.groovytest.hl7v2.def.v24.message

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.parser.ModelClassFactory

/**
* This custom message is compiled and loaded at runtime (unlike Java custom
* messages, which are already available as .class file. Take care to load required
* custom groups and segments in the same way. For this purpose the HapiModelExtension provides
* addSegment and addGroup extension methods, that take a String instead of a Class as
* first parameter.
*/
public class MDM_T02 extends ca.uhn.hl7v2.model.v24.message.MDM_T02 {

   public MDM_T02() {
	   super()
   }

   public MDM_T02(ModelClassFactory factory) {
	   super(factory)
	   init(factory)
   }

   /**
	* Add the ZBE segment at the end of the structure
	*
	* @param factory
	*/
   private void init(ModelClassFactory factory) {
	   try {
		   addSegment('ZBE', false, false)
	   } catch (HL7Exception e) {
           log.error("Unexpected error creating message structure", e)
	   }
   }

   public def getZBE() {
	   try {
		   return get('ZBE')
	   } catch (HL7Exception e) {
		   throw new RuntimeException(e)
	   }
   }
   
   // Defined by ModelClassExtension, too.
   
   private String addSegment(String name, boolean required, boolean repeating) {
	   add(getModelClassFactory().getSegmentClass(name, getVersion()), required, repeating)
   }

   private String addGroup(String name, boolean required, boolean repeating) {
	   add(getModelClassFactory().getGroupClass(name, getVersion()), required, repeating)
   }

}