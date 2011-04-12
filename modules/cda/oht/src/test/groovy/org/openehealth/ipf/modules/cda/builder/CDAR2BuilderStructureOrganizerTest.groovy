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
package org.openehealth.ipf.modules.cda.builder

import org.openhealthtools.ihe.common.cdar2.*
import org.junit.Test
import org.junit.Assert

/**
 * @author Stefan Ivanov
 */
public class CDAR2BuilderStructureOrganizerTest extends AbstractCDAR2BuilderTest {
	
	
	/**
	 * Test simple Organizer
	 */
	@Test
	public void testOrganizer() {
		def entry = builder.build {
			entry{
				organizer(moodCode:'EVN'){ statusCode(code:'completed') }
			}//entry
		}
		// println entry.organizer
	}
	
	/**
	 * Test organizer defaults
	 */
	@Test
	public void testOrganizertDefaultValues() {
		def entry = builder.build {
			entry{
				organizer(moodCode:'EVN'){ statusCode(code:'completed') }
			}//entry
		}
		// println entry.organizer
	}	
}