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

import static org.openehealth.ipf.commons.flow.util.Flows.createFlow;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * Lock test to be executed interactively using a debugger. This test
 * demonstrates the row-level locking features of Apache Derby. Use breakpoints
 * to see how updates are serialized when operating on the same {@link Flow}
 * object.
 * 
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class FlowLockTest {

    private static final Log LOG = LogFactory.getLog(FlowLockTest.class);
    
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    
    @Autowired
    private TestTransactionManager testTransactionManager;
    
    @Autowired
    private FlowRepository flowRepository;
    
    @Test
    public void testLock() throws Exception {
        testTransactionManager.beginTransaction();
        Flow flow1 = persistFlow("blah");
        Flow flow2 = persistFlow("blub");
        testTransactionManager.commitTransaction();
        FlowWriter writer1 = new FlowWriter(flow1.getIdentifier());
        FlowWriter writer2 = new FlowWriter(flow1.getIdentifier()); // use flow 1 to test exclusion
        writer1.start();
        writer2.start();
        writer1.join();
        writer2.join();
        flow1 = flowRepository.find(flow1.getIdentifier());
        LOG.trace("flow 1: time (get) = " + flow1.getCreationTime().getTime());
        flow2 = flowRepository.find(flow2.getIdentifier());
        LOG.trace("flow 1: time (get) = " + flow2.getCreationTime().getTime());
    }
    
    private Flow persistFlow(String packet) throws IOException {
        Flow flow = createFlow(packet);
        flowRepository.persist(flow);
        return flow;
    }
    
    private class FlowWriter extends Thread {

        private TestTransactionManager tm;
        
        private Long id;
        
        public FlowWriter(Long id) {
            this.tm = new TestTransactionManager(platformTransactionManager); 
            this.id = id;
        }
        
        @Override
        public void run() {
            tm.beginTransaction();
            try {
                lockAndUpdate();
            } finally {
                tm.endTransaction(); 
            }
        }
        
        private void lockAndUpdate() {
            Flow flow = flowRepository.lock(id);
            flow.setCreationTime(new Date());
        }
        
    }

}
