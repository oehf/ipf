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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.CLEAN;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.ERROR;

import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowPart;
import org.openehealth.ipf.commons.flow.domain.FlowStatus;
import org.openehealth.ipf.commons.flow.repository.search.DefaultSearchCallback;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.openehealth.ipf.commons.flow.util.Flows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Martin Krasser
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class FlowRepositoryFulltextTest {

    @Autowired
    private TestTransactionManager testTransactionManager;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private HibernateTemplate hibernateTemplate;
    
    @Autowired
    private DefaultSearchCallback searchCallback;

    @Before
    public void setUp() throws Exception {
        testTransactionManager.beginTransaction();
    }

    @After
    public void tearDown() throws Exception {
        testTransactionManager.endTransaction();
    }

    @Test
    public void testIndexAndSearchFlowMessage() throws Exception {
        String searchKey = "testIndexAndSearchFlowMessage";
        Long id = persistFlowWithText(searchKey);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        Flow flow = flowRepository.find(id);
        String text = flow.getFlowMessageText();
        assertTrue(text.contains(searchKey));
        flow = searchFlow(searchKey);
        assertEquals(id, flow.getIdentifier());
    }

    @Test
    public void testCleanupFlowMessage() throws Exception {
        String searchKey = "testCleanupFlowMessage";
        Long id = persistFlowWithText(searchKey);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        assertNotNull(searchFlow(searchKey));
        Flow flow = flowRepository.find(id);
        flow.setFlowMessageText(null);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        assertNull(flow.getFlowMessageText());
        assertNull(searchFlow(searchKey));
    }

    @Test
    public void testReplayFlowWithMessage() throws Exception {
        String searchKey = "testReplayFlowWithMessage";
        Long id = persistFlowWithText(searchKey);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        Flow flow = flowRepository.find(id);
        flow.setAckCountExpected(1);
        flow.prepareReplay();
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        assertNotNull(searchFlow(searchKey));
    }

    @Test
    public void testAcknowledgeAndGetMessage() throws Exception {
        String searchKey = "testAcknowledgeAndGetMessage";
        Long id = persistFlowWithText(searchKey);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        Flow persisted = flowRepository.find(id);
        persisted.setAckCountExpected(1);
        persisted.acknowledge("0", false);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        assertNotNull(searchFlow(searchKey));
    }

    @Test
    public void testAcknowledgeWithCleanupAndGetMessage() throws Exception {
        String searchKey = "testAcknowledgeWithCleanupAndGetMessage";
        Long id = persistFlowWithText(searchKey);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        Flow persisted = flowRepository.find(id);
        persisted.setAckCountExpected(1);
        persisted.acknowledge("0", true);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        assertNotNull(flowRepository.find(id));
        assertNull(searchFlow(searchKey));
    }

    @Test
    public void testFlowPartCleanError() throws Exception {
        String searchKey = "testFlowPartCleanError";
        persistFlowWithText(searchKey, CLEAN, ERROR);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        List<FlowPart> parts = searchFlowParts(searchKey);
        assertEquals(2, parts.size());
        assertNotNull(searchFlow(searchKey));
    }

    @Test
    public void testFlowPartCleanClean() throws Exception {
        String searchKey = "testFlowPartCleanClean";
        Long id = persistFlowWithText(searchKey, CLEAN, CLEAN);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        List<FlowPart> parts = searchFlowParts(searchKey);
        assertEquals(2, parts.size());
        assertEquals(id, parts.get(0).getFlowId());
        assertEquals(id, parts.get(1).getFlowId());
    }

    @Test
    public void testFlowPartReplay() throws Exception {
        String searchKey = "testFlowPartReplay";
        Long id = persistFlowWithText(searchKey, CLEAN, ERROR);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        List<FlowPart> parts = searchFlowParts(searchKey);
        assertEquals(2, parts.size());
        Flow flow = flowRepository.find(id);
        flow.setAckCountExpected(1);
        flow.prepareReplay();
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        parts = searchFlowParts(searchKey);
        // error flow has been cleared
        assertEquals(1, parts.size());
        assertNotNull(searchFlow(searchKey));
    }

    @SuppressWarnings("unchecked")
    private Flow searchFlow(final String content) {
        List<Flow> results = (List)hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) {
                return searchCallback.findFlowsByMessageQuery(session, content);
            }
        });
        if (results.isEmpty()) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        }
        fail("more than one flow returned");
        return null; // never reached
    }
    
    @SuppressWarnings("unchecked")
    private List<FlowPart> searchFlowParts(final String content) {
        return (List)hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) {
                return searchCallback.findFlowPartsByMessageQuery(session, content);
            }
        });
    }
    
    private Long persistFlowWithText(String content, FlowStatus... status) throws Exception {
        Flow flow = Flows.createFlowWithText(content, status);
        flowRepository.persist(flow);
        return flow.getIdentifier();
    }
    
}
