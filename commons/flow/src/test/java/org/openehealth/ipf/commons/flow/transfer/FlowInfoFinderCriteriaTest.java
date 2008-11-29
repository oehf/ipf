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
package org.openehealth.ipf.commons.flow.transfer;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author Mitko Kolev
 */
public class FlowInfoFinderCriteriaTest extends TestCase {

	@Test
	public void testConstructFlowFinderInfoCriteriaWithDefaultArgsForMaxFlows() {
		Date to = new Date(System.currentTimeMillis());
		Date from = new Date(System.currentTimeMillis() - 10000000);
		FlowInfoFinderCriteria criteria = new FlowInfoFinderCriteria(from, to,
				"application");

		assertTrue(criteria.getApplication().equals("application"));
		assertTrue(criteria.getMaxResults() == FlowInfoFinderCriteria.DEFAULT_MAX_RESULTS);
		assertTrue(criteria.getTo().equals(to));
		assertTrue(criteria.getFrom().equals(from));
	}

	@Test
	public void testConstructFlowFinderInfoCriteriaWithArgsForMaxFlows() {
		Date to = new Date();
		Date from = new Date(System.currentTimeMillis() - 10000000);
		FlowInfoFinderCriteria criteria = new FlowInfoFinderCriteria(from, to,
				"application", 100);

		assertTrue(criteria.getApplication().equals("application"));
		assertTrue(criteria.getTo().equals(to));
		assertTrue(criteria.getFrom().equals(from));
		assertTrue(criteria.getMaxResults() == 100);
	}

}
