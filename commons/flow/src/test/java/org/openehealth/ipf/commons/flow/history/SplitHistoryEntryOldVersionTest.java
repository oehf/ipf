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
package org.openehealth.ipf.commons.flow.history;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Krasser
 */
public class SplitHistoryEntryOldVersionTest {

	private SplitHistoryEntry entry;

	@Before
	public void setUp() throws Exception {
		entry = new SplitHistoryEntry(2, 3);
	}

	@Test
	public void testParse() {
		assertEquals(entry, SplitHistoryEntry.parse("(2/3)"));
	}

	@Test(expected = SplitHistoryFormatException.class)
	public void testParseFailNumber() {
		SplitHistoryEntry.parse("(m/3)");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseFailCount() {
		SplitHistoryEntry.parse("(3/3)");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewFailIndex() {
		new SplitHistoryEntry(-1, 3);
	}

	@Test(expected = SplitHistoryFormatException.class)
	public void testParseFailIndex() {
		SplitHistoryEntry.parse("(-1/3)");
	}

}
