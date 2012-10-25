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
package org.openehealth.ipf.commons.flow;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.impl.TestMessage;
import org.openehealth.ipf.commons.flow.repository.FlowRepository;
import org.openehealth.ipf.commons.flow.task.FlowInsertTask;
import org.openehealth.ipf.commons.flow.task.FlowLifecycleTask;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "/test-tx-explicit.xml",
        "/test-tx-declared.xml" 
})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class FlowManagerImplLoadTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(FlowManagerImplLoadTest.class);
    
    private static final int NUM_THREADS = 10;
    private static final int NUM_LOOPS = 100;
    
    private List<Thread> threads;
    
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private FlowManager flowManager;
    
    @Autowired
    private FlowRepository flowRepository;
    
    @Before
    public void setUp() throws Exception {
        this.threads = new ArrayList<Thread>(NUM_THREADS);
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testInsert() throws Exception {
        for (int i = 0; i < NUM_THREADS; i++) {
            FlowInsertTask task = new FlowInsertTask();
            task.setMessage(createMessage());
            task.setFlowManager(flowManager);
            task.setNumLoops(NUM_LOOPS);
            threads.add(task);
        }
        startThreadsAndJoin();
    }
    
    @Test
    public void testLifecycle() throws Exception {
        for (int i = 0; i < NUM_THREADS; i++) {
            FlowLifecycleTask task = new FlowLifecycleTask();
            task.setMessage(createMessage());
            task.setFlowManager(flowManager);
            task.setNumLoops(NUM_LOOPS);
            task.setFlowRepository(flowRepository);
            task.setTransactionManager(createTestTransactionManager());
            threads.add(task);
        }
        startThreadsAndJoin();
    }
    
    private void startThreadsAndJoin() throws InterruptedException {
        for (Thread thread : threads) {
            thread.start();
            LOG.info("thread {} started", thread.getName());
        }
        for (Thread thread : threads) {
            thread.join();
            LOG.info("join on {} returned", thread.getName());
        }
    }

    private TestTransactionManager createTestTransactionManager() {
        return new TestTransactionManager(platformTransactionManager);
    }
    
    private ManagedMessage createMessage() {
        return new TestMessage("egal");
    }
    
}
