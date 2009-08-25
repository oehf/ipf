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
public class CDAR2BuilderStructureObservationMediaTest extends AbstractCDAR2BuilderTest {
	
	
	/**
	 * Test observation media defaults
	 */
	@Test
	public void testObservationMediaDefaultValues() {
		def entry = builder.build {
			entry {
				observationMedia {
					value(mediaType: 'image/gif') { reference(value: 'lefthand.gif') }//value
				}//observation media
			}//entry
		}
		
		Assert.assertEquals 'EVN', entry.observationMedia.moodCode.name
		Assert.assertEquals null, entry.observationMedia.classCode
		Assert.assertEquals 0, entry.observationMedia.entryRelationship.size
		Assert.assertEquals 'image/gif', entry.observationMedia.value.mediaType
	}
	
	
	/**
	 * Test simple ObservationMedia
	 */
	@Test
	public void testSimpleObservationMedia() {
		def entry = builder.build(getClass().getResource('/builders/content/entry/ObservationMediaExample1.groovy'))		
		Assert.assertEquals 'OBS', entry.observationMedia.classCode.name
		Assert.assertEquals 'EVN', entry.observationMedia.moodCode.name
		Assert.assertNotNull entry.observationMedia.id
	}
	
	/**
	 *
	 */
	@Test
	public void testObservationMediaAsEntryRelationship() {
		def entry = builder.build(getClass().getResource('/builders/content/entry/ObservationMediaExample2.groovy'))		
		Assert.assertNotNull entry.observation
		Assert.assertNotNull entry.observation.entryRelationship
		Assert.assertEquals 1, entry.observation.entryRelationship.size
		Assert.assertEquals 8, entry.observation.entryRelationship.get(0).regionOfInterest.value.size
	}
}
