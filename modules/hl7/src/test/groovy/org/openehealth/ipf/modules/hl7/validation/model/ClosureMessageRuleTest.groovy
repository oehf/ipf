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
package org.openehealth.ipf.modules.hl7.validation.model

import org.junit.Assert
import org.junit.Test
import ca.uhn.hl7v2.parser.*
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.validation.ValidationException

/**
 * @author Christian Ohr
 */
public class ClosureMessageRuleTest {
	
		
	@Test
	void testTest(){
		def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
		def msg = new GenericParser().parse(msgText)        
		
		// Event time must start with '20'
		def rule = new ClosureMessageRule( {
		    if (!it.EVN.dateTimeOfEvent.timeOfAnEvent.value.startsWith('20')) {
		        def e = new ValidationException("baah")
		        return [e] as ValidationException[]
		    } else {
		        return [] as ValidationException[]
		    }
		})
		ValidationException[] ve = rule.test(msg)
		assert ve.size() == 0
		
		// Event time must start with '30' - fails.
		rule = new ClosureMessageRule( {  
		    if (!it.EVN.dateTimeOfEvent.timeOfAnEvent.value.startsWith('30')) {
		        def e = new ValidationException("baah")
		        return [e] as ValidationException[]		        
		    } else {
		        return [] as ValidationException[]
		    }
		})
		ve = rule.test(msg)
		assert ve.size() == 1
		assert ve[0].message == "baah"
	}
	
}
