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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.config.ApplicationConfig;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class ConfigRepositoryImplTest {
    
    @Autowired
    private TestTransactionManager testTransactionManager;
    
    @Autowired
    private ConfigRepository configRepository;

    @Before
    public void setUp() throws Exception {
        testTransactionManager.beginTransaction();
    }

    @After
    public void tearDown() throws Exception {
        testTransactionManager.endTransaction();
    }

    @Test
    public void testValidApplicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        config.setApplication("test");
        configRepository.merge(config);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        config = configRepository.find("wrong");
        assertNull(config);
        config = configRepository.find("test");
        assertNotNull(config);
        assertTrue(config.isFlowFilterEnabled());
        assertFalse(config.isFlowCleanupEnabled());
        config.setFlowFilterEnabled(false);
        config.setFlowCleanupEnabled(true);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        config = configRepository.find("test");
        assertFalse(config.isFlowFilterEnabled());
        assertTrue(config.isFlowCleanupEnabled());
    }
    
    @Test
    public void testInvalidApplicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        try {
            configRepository.merge(config);
            fail("persisted config without application name");
        } catch (RuntimeException e) {
            // test passed
        }
    }

    @Test
    public void testDuplicateApplicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        config.setApplication("blah");
        configRepository.merge(config);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        config = new ApplicationConfig();
        config.setApplication("blah");
        configRepository.persist(config);
        try {
            testTransactionManager.commitTransaction();
            fail("persisted equal config twice");
        } catch (RuntimeException e) {
            // test passed
        }
        
    }
    
}
