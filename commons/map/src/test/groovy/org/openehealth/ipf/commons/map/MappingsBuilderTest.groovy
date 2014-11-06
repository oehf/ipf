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
package org.openehealth.ipf.commons.map

import org.junit.Test

/**
 * @author Christian Ohr
 */
public class MappingsBuilderTest {

    @Test
	void testBuilder() {
		def builder = new MappingsBuilder()
		def map = builder.mappings {
		      encounterType(['2.16.840.1.113883.12.4','2.16.840.1.113883.5.4'],
		    	      I:'IMP',
		    	      O:'AMB',
		    	      E:'EMER'
		      )
		      vip(
		    		  Y:'VIP',
			    	  (ELSE): { it }
		      )

		}
		assert map != null
		assert map.encounterType.I == 'IMP'
		assert map.encounterType.'_%KEYSYSTEM%_' == '2.16.840.1.113883.12.4'
		assert map.vip.'_%ELSE%_'.call("HOHOH") == 'HOHOH'
		
	}
}
