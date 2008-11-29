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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Krasser
 */
public class SplitHistoryTest {

	private SplitHistory history;

	private SplitHistory[] historySplit;

	@Before
	public void setUp() throws Exception {
		history = new SplitHistory();
		historySplit = new SplitHistory().split(3);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToString() {
		assertEquals("[(0L)]", history.toString());
		assertEquals("[(0L),(0)]", historySplit[0].toString());
		assertEquals("[(0L),(1)]", historySplit[1].toString());
		assertEquals("[(0L),(2L)]", historySplit[2].toString());
	}

	@Test
	public void testParse() {
		assertEquals(history, SplitHistory.parse("[(0/1)]"));
		assertEquals(historySplit[0], SplitHistory.parse("[(0/1),(0/3)]"));
		assertEquals(historySplit[1], SplitHistory.parse("[(0/1),(1/3)]"));
		assertEquals(historySplit[2], SplitHistory.parse("[(0/1),(2/3)]"));
	}

	@Test
	public void testEquals() {
		SplitHistory history1 = SplitHistory.parse("[(0/1)]");
		SplitHistory history2 = SplitHistory.parse("[(0/1),(1/3)]");
		assertFalse(history1.equals(history2));
		assertTrue(history1.equals(history1));
		assertFalse(history1.equals(history1.toString()));
		assertFalse(history1.equals(null));
		assertTrue(history1.equals(new SplitHistory()));
	}

	@Test
	public void testHashcode() {
		SplitHistory history1 = SplitHistory.parse("[(0/1)]");
		SplitHistory history2 = SplitHistory.parse("[(0/1),(1/3)]");

		assertFalse(history1.hashCode() == history2.hashCode());
		assertTrue(history1.hashCode() == history1.hashCode());
		assertTrue(history1.hashCode() == history.hashCode());
	}

	@Test(expected = SplitHistoryFormatException.class)
	public void testParseFailFormatMissingBracket() {
		assertEquals(historySplit[2], SplitHistory.parse("[(0/1),(2/3)"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void isFirstUpTo() {
		int level = 3;
		assertTrue(historySplit[2].size() == 2);
		historySplit[2].isFirstUpTo(level);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isLastUpTo() {
		int level = 3;
		assertTrue(historySplit[2].size() == 2);
		historySplit[2].isLastUpTo(level);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseFailValueInvalidRoot() {
		assertEquals(historySplit[2], SplitHistory.parse("[(1/2),(2/3)]"));
	}

	@Test(expected = SplitHistoryFormatException.class)
	public void testParseValueInvalidValues() {
		assertEquals(historySplit[2], SplitHistory.parse("[(0/1),(-1/3)]"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseValueInvalidValues2() {
		assertEquals(historySplit[2], SplitHistory.parse("[(0/0)]"));
	}

	@Test
	public void testIsPredecessor() {
		// tests reflexivity
		String id = "[(0/1),(0/2),(0/4),(0/2)]";
		assertFalse(SplitHistory.parse(id)
				.isPredecessor(SplitHistory.parse(id)));
		assertFalse(SplitHistory.parse(id).isSuccessor(SplitHistory.parse(id)));
		id = "[(0/1),(1/2),(0/1234)]";
		assertFalse(SplitHistory.parse(id)
				.isPredecessor(SplitHistory.parse(id)));
		assertFalse(SplitHistory.parse(id).isSuccessor(SplitHistory.parse(id)));

		// tests also symmetry for the immediate parts
		String p1 = "[(0/1),(0/2),(1/3)]";
		String p2 = "[(0/1),(0/2),(2/3)]";
		assertTrue(SplitHistory.parse(p1).isPredecessor(SplitHistory.parse(p2)));
		assertFalse(SplitHistory.parse(p2)
				.isPredecessor(SplitHistory.parse(p1)));
		p1 = "[(0/1),(0/2),(2/3)]";
		p2 = "[(0/1),(1/2),(0/2),(0/4)]";
		assertTrue(SplitHistory.parse(p1).isPredecessor(SplitHistory.parse(p2)));
		assertFalse(SplitHistory.parse(p2)
				.isPredecessor(SplitHistory.parse(p1)));
		p1 = "[(0/1),(1/2),(0/2),(2/4)]";
		p2 = "[(0/1),(1/2),(0/2),(3/4),(0/3)]";
		assertTrue(SplitHistory.parse(p1).isPredecessor(SplitHistory.parse(p2)));
		assertFalse(SplitHistory.parse(p2)
				.isPredecessor(SplitHistory.parse(p1)));
		p1 = "[(0/1),(1/2),(0/2),(2/4)]";
		p2 = "[(0/1),(1/2),(0/2),(1/4),(0/3)]";
		assertFalse(SplitHistory.parse(p1)
				.isPredecessor(SplitHistory.parse(p2)));
		assertFalse(SplitHistory.parse(p2)
				.isPredecessor(SplitHistory.parse(p1)));
		p1 = "[(0/1),(1/2),(0/2),(2/4)]";
		p2 = "[(0/1),(1/2),(1/2),(0/4),(0/3)]";
		assertFalse(SplitHistory.parse(p1)
				.isPredecessor(SplitHistory.parse(p2)));
		assertFalse(SplitHistory.parse(p2)
				.isPredecessor(SplitHistory.parse(p1)));
		p1 = "[(0/1),(1/2),(0/2),(3/4),(2/3)]";
		p2 = "[(0/1),(1/2),(1/2)]";
		assertTrue(SplitHistory.parse(p1).isPredecessor(SplitHistory.parse(p2)));
		assertFalse(SplitHistory.parse(p2)
				.isPredecessor(SplitHistory.parse(p1)));
		p1 = "[(0/1)]";
		p2 = "[(0/1)]";
		assertFalse(SplitHistory.parse(p1)
				.isPredecessor(SplitHistory.parse(p2)));
		assertFalse(SplitHistory.parse(p2)
				.isPredecessor(SplitHistory.parse(p1)));
		p1 = "[(0/1),(0/2)]";
		p2 = "[(0/1),(0/2)]";
		assertFalse(SplitHistory.parse(p1)
				.isPredecessor(SplitHistory.parse(p2)));
		assertFalse(SplitHistory.parse(p2)
				.isPredecessor(SplitHistory.parse(p1)));
		p1 = "[(0/1),(0/2)]";
		p2 = "[(0/1),(0/2),(0/2)]";
		assertFalse(SplitHistory.parse(p1)
				.isPredecessor(SplitHistory.parse(p2)));
		assertFalse(SplitHistory.parse(p2)
				.isPredecessor(SplitHistory.parse(p1)));
	}

	@Test
	public void testIsSuccessor() {
		String p1 = "[(0/1),(0/2),(1/3)]";
		String p2 = "[(0/1),(0/2),(2/3)]";
		assertTrue(SplitHistory.parse(p2).isSuccessor(SplitHistory.parse(p1)));
		assertFalse(SplitHistory.parse(p1).isSuccessor(SplitHistory.parse(p2)));
		p1 = "[(0/1),(0/2),(2/3)]";
		p2 = "[(0/1),(1/2),(0/2),(0/4)]";
		assertTrue(SplitHistory.parse(p2).isSuccessor(SplitHistory.parse(p1)));
		assertFalse(SplitHistory.parse(p1).isSuccessor(SplitHistory.parse(p2)));
		p1 = "[(0/1),(1/2),(0/2),(2/4)]";
		p2 = "[(0/1),(1/2),(0/2),(3/4),(0/3)]";
		assertTrue(SplitHistory.parse(p2).isSuccessor(SplitHistory.parse(p1)));
		assertFalse(SplitHistory.parse(p1).isSuccessor(SplitHistory.parse(p2)));
		p1 = "[(0/1),(1/2),(0/2),(2/4)]";
		p2 = "[(0/1),(1/2),(0/2),(1/4),(0/3)]";
		assertFalse(SplitHistory.parse(p2).isSuccessor(SplitHistory.parse(p1)));
		assertFalse(SplitHistory.parse(p1).isSuccessor(SplitHistory.parse(p2)));
		p1 = "[(0/1),(1/2),(0/2),(2/4)]";
		p2 = "[(0/1),(1/2),(1/2),(0/4),(0/3)]";
		assertFalse(SplitHistory.parse(p2).isSuccessor(SplitHistory.parse(p1)));
		assertFalse(SplitHistory.parse(p1).isSuccessor(SplitHistory.parse(p2)));
		p1 = "[(0/1),(1/2),(0/2),(3/4),(2/3)]";
		p2 = "[(0/1),(1/2),(1/2)]";
		assertTrue(SplitHistory.parse(p2).isSuccessor(SplitHistory.parse(p1)));
		assertFalse(SplitHistory.parse(p1).isSuccessor(SplitHistory.parse(p2)));
		p1 = "[(0/1)]";
		p2 = "[(0/1)]";
		assertFalse(SplitHistory.parse(p2).isSuccessor(SplitHistory.parse(p1)));
		assertFalse(SplitHistory.parse(p1).isSuccessor(SplitHistory.parse(p2)));
		p1 = "[(0/1),(0/2)]";
		p2 = "[(0/1),(0/2)]";
		assertFalse(SplitHistory.parse(p2).isSuccessor(SplitHistory.parse(p1)));
		assertFalse(SplitHistory.parse(p1).isSuccessor(SplitHistory.parse(p2)));
		p1 = "[(0/1),(0/2)]";
		p2 = "[(0/1),(0/2),(0/2)]";
		assertFalse(SplitHistory.parse(p2).isSuccessor(SplitHistory.parse(p1)));
		assertFalse(SplitHistory.parse(p1).isSuccessor(SplitHistory.parse(p2)));
	}

	@Test
	public void testIsFirst() {
		String p = "[(0/1),(1/2),(0/3)]";
		assertFalse((SplitHistory.parse(p).isFirst()));
		p = "[(0/1),(0/2),(0/3)]";
		assertTrue((SplitHistory.parse(p).isFirst()));
		p = "[(0/1)]";
		assertTrue((SplitHistory.parse(p).isFirst()));
	}

	@Test
	public void testIsLast() {
		String p = "[(0/1),(0/2),(2/3)]";
		assertFalse((SplitHistory.parse(p).isLast()));
		p = "[(0/1),(1/2),(2/3)]";
		assertTrue((SplitHistory.parse(p).isLast()));
		p = "[(0/1)]";
		assertTrue((SplitHistory.parse(p).isLast()));
	}

	@Test
	public void testIndexPathString() {
		String p = "[(0/1),(0/2),(2/3)]";
		assertEquals("0.0.2", (SplitHistory.parse(p).indexPathString()));
		p = "[(0/1),(1/2),(2/3)]";
		assertEquals("0.1.2", (SplitHistory.parse(p).indexPathString()));
		p = "[(0/1)]";
		assertEquals("0", (SplitHistory.parse(p).indexPathString()));
	}

	@Test
	public void testSplit() {
		assertEquals("[(0L),(0),(0)]", new SplitHistory().split(3)[0]
				.split(2)[0].toString());
		assertEquals("[(0L),(1),(1L)]", new SplitHistory().split(3)[1]
				.split(2)[1].toString());
		assertEquals("[(0L),(2L),(0)]", new SplitHistory().split(3)[2]
				.split(2)[0].toString());
		assertEquals("[(0L),(2L),(1L)]", new SplitHistory().split(3)[2]
				.split(2)[1].toString());
	}
	
	@Test
	public void testSplitSingle() {
        assertEquals("[(0L),(0),(0)]", new SplitHistory().split(0, false)
               .split(0, false).toString());
       assertEquals("[(0L),(1),(1L)]", new SplitHistory().split(1, false)
               .split(1, true).toString());
       assertEquals("[(0L),(2L),(0)]", new SplitHistory().split(2, true)
               .split(0, false).toString());
       assertEquals("[(0L),(2L),(1L)]", new SplitHistory().split(2, true)
               .split(1, true).toString());
	}

	@Test
	public void testCompareTo() {
		String p1 = "[(0/1),(0/3),(0/2)]";
		String p2 = "[(0/1),(0/3),(0/2)]";
		assertTrue(SplitHistory.parse(p2).compareTo(SplitHistory.parse(p1)) == 0);
		p1 = "[(0/1),(0/3),(0/2)]";
		p2 = "[(0/1),(0/3),(1/2)]";
		assertTrue(SplitHistory.parse(p1).compareTo(SplitHistory.parse(p2)) < 0);
		assertTrue(SplitHistory.parse(p2).compareTo(SplitHistory.parse(p1)) > 0);
		p1 = "[(0/1),(0/3)]";
		p2 = "[(0/1),(0/3),(1/2)]";
		assertTrue(SplitHistory.parse(p1).compareTo(SplitHistory.parse(p2)) < 0);
		assertTrue(SplitHistory.parse(p2).compareTo(SplitHistory.parse(p1)) > 0);
		p1 = "[(0/1),(0/3),(0/2)]";
		p2 = "[(0/1),(1/3),(0/2)]";
		assertTrue(SplitHistory.parse(p1).compareTo(SplitHistory.parse(p2)) < 0);
		assertTrue(SplitHistory.parse(p2).compareTo(SplitHistory.parse(p1)) > 0);
	}

}
