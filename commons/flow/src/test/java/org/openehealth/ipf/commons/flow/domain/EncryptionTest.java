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
package org.openehealth.ipf.commons.flow.domain;


import static org.junit.Assert.assertFalse;
import static org.openehealth.ipf.commons.flow.util.Flows.createFlowWithText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.repository.FlowRepository;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class EncryptionTest {

    @Autowired
    private TestTransactionManager testTransactionManager;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Before
    public void setUp() throws Exception {
        testTransactionManager.beginTransaction();
    }

    @After
    public void tearDown() throws Exception {
        testTransactionManager.commitTransaction();
    }

    @Test
    public void testEncryptFlowMessageText() throws Exception {
        String searchKey = "testEncryptFlowMessageText";
        Flow flow = createFlowWithText(searchKey);
        flowRepository.persist(flow);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(flow.getIdentifier());
        String id = flow.getFlowMessage().getIdentifier();
        String text = flow.getFlowMessage().getText();
        String query = "SELECT C_TEXT FROM PLATFORM.T_FLOW_MESSAGE WHERE C_ID = ?";
        Object crypt = jdbcTemplate.queryForObject(query, new Object[] {id}, String.class);
        assertFalse(text.equals(crypt));
    }
    
    @Test
    public void testEncryptFlowPartMessageText() throws Exception {
        String searchKey = "testEncryptFlowPartMessageText";
        Flow flow = createFlowWithText(searchKey, FlowStatus.CLEAN);
        flowRepository.persist(flow);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(flow.getIdentifier());
        FlowPart part = flow.getParts().iterator().next();
        String id = part.getFlowPartMessage().getIdentifier();
        String text = part.getFlowPartMessage().getText();
        String query = "SELECT C_TEXT FROM PLATFORM.T_FLOW_PART_MESSAGE WHERE C_ID = ?";
        Object crypt = jdbcTemplate.queryForObject(query, new Object[] {id}, String.class);
        assertFalse(text.equals(crypt));
    }
    
}
