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

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jasypt.encryption.StringEncryptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
 * Test for the FlowMessage domain object
 * 
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class FlowMessageTest extends TestCase {

    @Autowired
    private TestTransactionManager testTransactionManager;

    @Autowired
    private FlowRepository flowRepository;

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

    @Test
    public void testIndexAndQueryFlowMessage() throws Exception {
        String searchKey = "testIndexAndQueryFlowMessage";
        Long flowId = createFlowWithMessage(searchKey);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        Flow persisted = flowRepository.find(flowId);

        String text = persisted.getFlowMessageText();
        assertTrue(text.contains(searchKey));
        doFullTextSearch(searchKey);
    }

    /**
     * Tests whether the database stores encrypted values of the messages. For
     * this case to pass, in the spring context an encryptor must be defined.
     * Example:
     * 
     * &lt;bean id="stringEncryptor"
     * class="org.jasypt.encryption.pbe.StandardPBEStringEncryptor"&gt;
     * &lt;property name="password"&gt; &lt;value&gt;test_password&lt;/value&gt;
     * &lt;/property&gt; &lt;/bean&gt;
     * 
     * 
     * @throws IOException
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testDatabaseEncryptionFlowMessage() throws IOException {
        String key = "testDatabaseEncryptionFlowMessage";
        Long id = createFlowWithMessage(key);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        Flow persisted = flowRepository.find(id);
        String message = persisted.getFlowMessageText();

        JdbcTemplate template = new JdbcTemplate(hibernateSessionFactory
                .getDataSource());
        template.setDataSource(hibernateSessionFactory.getDataSource());

        List objects = template
                .queryForList("SELECT C_TEXT from PLATFORM.T_FLOW_MESSAGE");
        assertTrue(objects.size() > 0);
        for (Object object : objects) {
            if (object instanceof HashMap) {
                HashMap map = (HashMap) object;
                String tableValue = (String) map.get("C_TEXT");
                if (tableValue != null) {
                    if (stringEncryptor == null) {// if encryptor is not defined
                        assertTrue(tableValue.equals(message));
                    } else {
                        assertFalse(tableValue.equals(message));
                    }
                }
            }
        }
    }

    /**
     * Test exception thrown when there are no results in the search
     * 
     * @see FlowMessageTest#doFullTextSearch
     */
    static class NoResultsFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

    }

    @Test
    @ExpectedException(value = NoResultsFoundException.class)
    public void testCleanupFlowMessage() throws Exception {
        String searchKey = "testCleanupFlowMessage";
        Long id = createFlowWithMessage(searchKey);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        Flow persisted = flowRepository.find(id);
        String text = persisted.getFlowMessageText();

        assertTrue(text.contains(searchKey));
        doFullTextSearch(searchKey);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        persisted = flowRepository.find(id);
        persisted.setFlowMessageText(null);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        doFullTextSearch(searchKey);

    }

    @Test
    public void testReplayFlowWithMessage() throws Exception {
        String searchKey = "testReplayFlowWithMessage";
        Long id = createFlowWithMessage(searchKey);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        Flow persisted = flowRepository.find(id);
        persisted.setAckCountExpected(1);
        persisted.prepareReplay();
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        persisted = flowRepository.find(id);
        doFullTextSearch(searchKey);

    }

    @Test
    public void testAcknowledgeAndGetMessage() throws Exception {
        String searchKey = "testAcknowledgeAndGetMessage";
        Long id = createFlowWithMessage(searchKey);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        Flow persisted = flowRepository.find(id);
        persisted.setAckCountExpected(1);
        // do not clean
        persisted.acknowledge("0", false);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        persisted = flowRepository.find(id);
        doFullTextSearch(searchKey);

    }

    @Test
    @ExpectedException(value = NoResultsFoundException.class)
    public void testAcknowledgeWithCleanAndGetMessage() throws Exception {
        String searchKey = "testAcknowledgeWithCleanAndGetMessage";
        Long id = createFlowWithMessage(searchKey);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        Flow persisted = flowRepository.find(id);
        persisted.setAckCountExpected(1);
        // do clean
        persisted.acknowledge("0", true);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        persisted = flowRepository.find(id);
        doFullTextSearch(searchKey);
    }

    @Test
    public void testEncryptorInversiveness() {
        if (stringEncryptor != null) {
            String text = ";oij\u1235 \u3490 \u6664 klfkff;akljdl;kjagi0399akmk asdfkl;  akjdo0w  a9u8934j ;kjasd;fklj iojasdiofj %^ALdk;,ADFASDF";
            String encrypted = stringEncryptor.encrypt(text);
            assertFalse(encrypted.equals(text));
            String decrypted = stringEncryptor.decrypt(encrypted);
            assertTrue(decrypted.equals(text));
            assertFalse(decrypted.equals(encrypted));
        }
    }

    /**
     * Creates a Flow with a text message. Puts the searchKey in the text
     * 
     * @param searchKey
     * @return
     * @throws IOException
     */
    private Long createFlowWithMessage(String searchKey) throws IOException {
        Long flowId;
        StringBuffer packetBuffer = new StringBuffer();
        packetBuffer.append("Created: " + new Date().toString() + "\n");

        packetBuffer.append("Hello, i have a flow Packet:" + "\n");
        packetBuffer.append("My packet has some distinguieshed text: "
                + searchKey + " .");
        packetBuffer.append("It also has another line of text!\n");
        String pacektBuffer = packetBuffer.toString();

        Flow flow = createFlow(pacektBuffer.getBytes(), false);
        flow.setFlowMessageText(pacektBuffer);

        flowRepository.persist(flow);
        flowId = new Long(flow.getIdentifier());
        return flowId;
    }

    @SuppressWarnings("unchecked")
    private void doFullTextSearch(String searchKey) throws ParseException {
        Session session = hibernateTemplate.getSessionFactory()
                .getCurrentSession();

        // log.info(" ================ Search Begin ================ ");
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(
                new String[] { "flowMessage.text" }, new StandardAnalyzer());
        Query query = parser.parse(searchKey);
        org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(
                query, Flow.class);

        List results = hibQuery.list();

        // throw exception when the results are not found.
        if (results.size() == 0) {
            throw new NoResultsFoundException();
        }
        for (int t = 0; t < results.size(); t++) {
            Object result = results.get(t);
            if (result instanceof Flow) {
                Flow flow = (Flow) result;
                // log.info(" ================ Result ================ ");
                // log.info("Text:" + message.getFlowMessageText());
                String message = flow.getFlowMessageText();
                assertTrue(message.contains(searchKey));
            }

        }
    }
}
