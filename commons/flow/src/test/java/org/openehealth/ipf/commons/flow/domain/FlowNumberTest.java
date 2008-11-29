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
package org.openehealth.ipf.commons.flow.domain;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author Mitko Kolev
 */
public class FlowNumberTest extends TestCase {

	@Test
	public void testConstructor() {
		FlowNumber number = new FlowNumber();
		assertTrue(number.getValue() == 0L);
		assertTrue(number.getSequence().equals(FlowNumber.DEFAULT_SEQUENCE));

		FlowNumber numberNewSequence = new FlowNumber("newSequence");
		assertTrue(numberNewSequence.getValue() == 0L);
		assertTrue(numberNewSequence.getSequence().equals("newSequence"));
	}

	@Test
	public void testIncrementAndGet() {
		FlowNumber number = new FlowNumber();
		assertTrue(number.getValue() == 0L);

		Long value = number.incrementAndGet();
		assertTrue(value == 1L);
		assertTrue(number.getValue() == 1L);

		for (int t = 1; t < 10; t++) {
			assertTrue(number.incrementAndGet() == (1 + t));
		}
	}

	@Test
	public void testGetSetValue() {
		FlowNumber number = new FlowNumber();
		number.setValue(Long.MAX_VALUE);
		assertTrue(number.getValue() == Long.MAX_VALUE);
	}
}
