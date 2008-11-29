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
package org.openehealth.ipf.commons.flow.repository;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class SequenceRepositoryImplTest {

	@Autowired
	private TestTransactionManager testTransactionManager;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	private SequenceRepositoryImpl sequenceRepository;

	@Before
	public void setUp() throws Exception {
		sequenceRepository = new SequenceRepositoryImpl();
		sequenceRepository.setHibernateTemplate(hibernateTemplate);
		testTransactionManager.beginTransaction();
	}

	@After
	public void tearDown() throws Exception {
		testTransactionManager.endTransaction();
	}

	@Test
	public void testNextNumber() throws Exception {

		sequenceRepository.initSequence();
		testTransactionManager.commitTransaction();
		testTransactionManager.beginTransaction();
		for (long t = 1; t < 10; t++) {
			Long number = sequenceRepository.nextNumber();
			assertTrue(number.equals(t));
		}
	}

	@Test
	public void testNextNumberWithTransaction() throws Exception {
		sequenceRepository.initSequence();
		Long number = sequenceRepository.nextNumber();
		testTransactionManager.rollbackTransaction();

		testTransactionManager.beginTransaction();
		sequenceRepository.initSequence();
		Long newNumber = sequenceRepository.nextNumber();
		assertTrue(newNumber.equals(number));

	}
}
