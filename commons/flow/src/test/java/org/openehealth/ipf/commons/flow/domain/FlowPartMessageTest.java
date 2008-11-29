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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jasypt.encryption.StringEncryptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.domain.FlowMessageTest.NoResultsFoundException;
import org.openehealth.ipf.commons.flow.impl.TestMessage;
import org.openehealth.ipf.commons.flow.repository.FlowRepository;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.openehealth.ipf.commons.flow.util.Flows.createFlow;

/**
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class FlowPartMessageTest extends TestCase {

    @Autowired
    private TestTransactionManager testTransactionManager;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private FlowManager flowManager;

    @Autowired
    private LocalSessionFactoryBean hibernateSessionFactory;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired(required = false)
    private StringEncryptor stringEncryptor;

    @Override
    @Before
    public void setUp() throws Exception {
        testTransactionManager.beginTransaction();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        testTransactionManager.endTransaction();
    }

    /**
     * Make 2 flows, set no messages, make sure lucene index contains no
     * messages.
     */
    @Test
    @ExpectedException(value = NoResultsFoundException.class)
    public void testNoMessage() throws Exception {
        String searchKey = "testNoMessageKey";
        // creates a flow with 2 parts 0.0 and 0.1, which contain a message with
        // key searchKey
        Flow flow = createFlow("flowPacket".getBytes(), false);
        flow.setAckCountExpected(2);
        flowRepository.persist(flow);
        Long flowId = flow.getIdentifier();
        testTransactionManager.commitTransaction();

        // now acknowledge the 2 flow parts, with the option to be cleaned.
        testTransactionManager.beginTransaction();
        Flow persisted = flowRepository.find(flowId);
        persisted.acknowledge("0.0", false);
        persisted.acknowledge("0.1", false);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        // throw exception, because the parts have been deleted.
        // The lucene index must also be cleared.
        doFullTextSearchInFlowParts(searchKey);
    }

    /**
     * Acknowledge 1 flow, invalidate 1 flow and make sure they are in the index
     */
    @Test
    public void testMessageAcknowledgeAndInvalidate() throws Exception {
        String searchKey = "testMessageAcknowledgeAndInvalidate";
        // creates a flow with 2 parts 0.0 and 0.1, which contain a message with
        // key searchKey
        createFlowWithPartsAndMessage(searchKey, FlowStatus.CLEAN,
                FlowStatus.ERROR);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        // throw exception, because the parts have been deleted.
        // The lucene index must also be cleared.
        int hits = doFullTextSearchInFlowParts(searchKey);
        assertTrue(hits == 2);
    }

    /**
     * Acknowledge 2 flows and see if they are in the lucene index
     */
    @Test
    public void testIndexingMessage2Acknowledges() throws Exception {
        String flowPartMessageKey = "testIndexingMessage2Acknowledges";
        Long flowId = createFlowWithPartsAndMessage(flowPartMessageKey,
                FlowStatus.CLEAN, FlowStatus.CLEAN);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        checkKeyExistenceInPartMessage(flowId, flowPartMessageKey);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        int hits = doFullTextSearchInFlowParts(flowPartMessageKey);
        assertTrue(hits == 2);

    }

    /**
     * Assures that the part messages are cleared from the lucene index on
     * replay. The post-delete event listener assures that the message is
     * deleted in the DB as well
     * 
     */
    @Test
    @ExpectedException(value = NoResultsFoundException.class)
    public void testReplayFlowPartWithMessage() throws Exception {
        String flowPartMessageKey = "testClearMessage";

        TestMessage testMessage = new TestMessage("packet_message");
        Long flowId = flowManager.beginFlow(new TestMessage("packet_message"),
                "test");
        testMessage.setFlowId(flowId);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        Flow flow = flowRepository.find(flowId);
        flow.setFlowMessageText("InitialFlowMessageText");
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        // only invalidate
        flowManager.invalidateFlow(testMessage);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        flow = flowRepository.find(flowId);
        // keeps track how many
        for (FlowPart part : flow.getParts()) {
            part.setFlowPartMessageText(this
                    .createFlowPartMessageText(flowPartMessageKey));
        }
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        try {
            int hits = doFullTextSearchInFlowParts(flowPartMessageKey);
            assertTrue(hits == 1);
        } catch (NoResultsFoundException e) {
            fail();
        }
        flowManager.replayFlow(flowId);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        // throw the exception
        doFullTextSearchInFlowParts(flowPartMessageKey);
    }

    /**
     * Tests if the flow message is encrypted in the DB
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testFlowPartDatabaseEncryption() throws Exception {
        String flowPartMessageKey = "testFlowPartDatabaseEncryption";
        Long flowId = createFlowWithPartsAndMessage(flowPartMessageKey,
                FlowStatus.CLEAN);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        int hits = this.doFullTextSearchInFlowParts(flowPartMessageKey);
        assertTrue(hits == 1);
        Flow flow = flowRepository.find(flowId);
        FlowPart part = flow.getPart("0.0", FlowStatus.CLEAN);
        String messsage = part.getFlowPartMessageText();

        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();

        JdbcTemplate template = new JdbcTemplate(hibernateSessionFactory
                .getDataSource());
        template.setDataSource(hibernateSessionFactory.getDataSource());

        List objects = template
                .queryForList("SELECT C_TEXT from PLATFORM.T_FLOW_PART_MESSAGE");
        assertTrue(objects.size() > 0);
        for (Object object : objects) {
            if (object instanceof HashMap) {
                HashMap map = (HashMap) object;
                String tableValue = (String) map.get("C_TEXT");
                if (tableValue != null) {
                    if (stringEncryptor == null) {// if encryptor is not defined
                        assertTrue(tableValue.equals(messsage));
                    } else {
                        assertFalse(tableValue.equals(messsage));
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private int doFullTextSearchInFlowParts(String searchKey)
            throws ParseException {
        Session session = hibernateTemplate.getSessionFactory()
                .getCurrentSession();

        // log.info(" ================ Search Begin ================ ");
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(
                new String[] { "flowPartMessage.text" }, new StandardAnalyzer());
        Query query = parser.parse(searchKey);
        FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query,
                FlowPart.class);

        List results = hibQuery.list();

        int hitsCount = 0;

        // throw exception when the results are not found.
        if (results.size() == 0) {
            throw new NoResultsFoundException();
        }
        hitsCount = results.size();
        for (int t = 0; t < results.size(); t++) {
            Object result = results.get(t);
            if (result instanceof Flow) {
                fail();
            } else if (result instanceof FlowPart) {
                FlowPart part = (FlowPart) result;
                Long flowId = part.getFlowId();
                Flow flow = flowRepository.find(flowId);
                FlowPart partInFlow = flow.getPart(part.getPath(), part
                        .getStatus());
                assertTrue(partInFlow.equals(part));
                String text = part.getFlowPartMessageText();
                assertTrue(text.contains(searchKey));
            }

        }
        return hitsCount;
    }

    // checks whether the search key is contained in both flow parts.
    private void checkKeyExistenceInPartMessage(Long flowId, String searchKey)
            throws ParseException {
        Flow persisted = flowRepository.find(flowId);
        FlowPart part1 = persisted.getPart("0.0", FlowStatus.CLEAN);
        FlowPart part2 = persisted.getPart("0.1", FlowStatus.CLEAN);

        String text1 = part1.getFlowPartMessageText();
        assertTrue(text1.contains(searchKey));
        String text2 = part2.getFlowPartMessageText();
        assertTrue(text2.contains(searchKey));
        doFullTextSearchInFlowParts(searchKey);
    }

    private Long createFlowWithPartsAndMessage(String flowMessageSearchKey,
            FlowStatus... status) throws Exception {
        Long flowId;
        StringBuffer packetBuffer = new StringBuffer();
        packetBuffer.append("Created: " + new Date().toString() + "\n");

        packetBuffer.append("Hello, i have a flow Packet:" + "\n");
        packetBuffer.append("My packet has some distinguieshed text: "
                + flowMessageSearchKey + " .");
        packetBuffer.append("It also has another line of text!\n");
        String packetString = packetBuffer.toString();

        Flow flow = createFlow(packetString.getBytes(), false);
        for (int t = 0; t < status.length; t++) {

            flow.setFlowMessageText(packetString);
            if (status[t].equals(FlowStatus.CLEAN)) {
                flow.acknowledge("0." + t, false);
            } else {
                flow.invalidate("0." + t);
            }
            FlowPart part = flow.getPart("0." + t, status[t]);
            part
                    .setFlowPartMessageText(createFlowPartMessageText(flowMessageSearchKey));
        }
        flowRepository.persist(flow);
        flowId = new Long(flow.getIdentifier());
        flow.setAckCountExpected(status.length);
        return flowId;

    }

    private String createFlowPartMessageText(String key) {
        StringBuffer flowPartMessage = new StringBuffer();
        flowPartMessage.append(new Date().toString() + "\n");
        flowPartMessage.append("This is a flow Part Message with key: ");
        flowPartMessage.append(key);
        flowPartMessage.append("\n");
        return flowPartMessage.toString();
    }
}
