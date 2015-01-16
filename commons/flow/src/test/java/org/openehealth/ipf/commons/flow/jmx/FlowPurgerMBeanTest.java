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
package org.openehealth.ipf.commons.flow.jmx;

import static org.junit.Assert.assertEquals;
import static org.openehealth.ipf.commons.flow.util.Flows.createFlow;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowStatus;
import org.openehealth.ipf.commons.flow.repository.FlowFinderCriteria;
import org.openehealth.ipf.commons.flow.repository.FlowRepositoryImpl;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * 
 * @author Boris Stanojevic
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "/test-purger.xml" })
public class FlowPurgerMBeanTest {

    @Autowired
    FlowPurgerMBean flowPurgerMBean;
    
    @Autowired
    private TestTransactionManager testTransactionManager;
    
    @Autowired
    private FlowRepositoryImpl flowRepository;    

    private final FlowFinderCriteria allFlows = new FlowFinderCriteria(new Date(0), null, "test");

    private static final String PURGE_SCHEDULE_EXPRESSION = "0/2 * * * * ?";
    
    private static final long EIGHT_DAYS = TimeUnit.DAYS.toMillis(8);
    
    private static final long WAIT_TIMEOUT = 5;

    private final CountDownLatch latch = new CountDownLatch(1);
    
    @Before
    public void setUp() throws Exception {
        testTransactionManager.beginTransaction();
    }

    @After
    public void tearDown() throws Exception {
        removeAll();
        testTransactionManager.endTransaction();
    }    

    @Test
    public void testConfigurePurgerScheduler() throws Exception{
        assertEquals("test", flowPurgerMBean.getApplication());
        Map<String, String> metadata = flowPurgerMBean.getSchedulerMetaData();
        assertEquals("1", metadata.get("Thread Pool Size"));
        assertEquals("FlowPurgerScheduler", metadata.get("Scheduler Name"));
    }
    
    @Test
    public void testDoNotPurgeErrorFlows() throws Exception {
        persistFlowHistory();
        assertEquals(6, flowRepository.findFlows(allFlows).size());
        flowPurgerMBean.getScheduler().getListenerManager().addJobListener(new PurgeJobListener(latch));
        flowPurgerMBean.setPurgeSchedule(PURGE_SCHEDULE_EXPRESSION);
        flowPurgerMBean.setDoNotPurgeErrorFlows(true);
        flowPurgerMBean.setPurgeFlowsOlderThan("9d");
        flowPurgerMBean.schedule();
        latch.await(WAIT_TIMEOUT, TimeUnit.SECONDS);
        assertEquals(2, flowRepository.findFlows(allFlows).size());
        flowPurgerMBean.unschedule();
    }

    @Test
    public void testPurgeFlowsOlderThan() throws Exception {
        persistFlowHistory();
        assertEquals(6, flowRepository.findFlows(allFlows).size());
        flowPurgerMBean.getScheduler().getListenerManager().addJobListener(new PurgeJobListener(latch));
        flowPurgerMBean.setPurgeSchedule(PURGE_SCHEDULE_EXPRESSION);
        flowPurgerMBean.setDoNotPurgeErrorFlows(false);
        flowPurgerMBean.setPurgeFlowsOlderThan("7d");
        flowPurgerMBean.schedule();        
        latch.await(WAIT_TIMEOUT, TimeUnit.SECONDS);
        assertEquals(0, flowRepository.findFlows(allFlows).size());
        flowPurgerMBean.unschedule();
    }

    @Test
    public void testExecute() throws Exception {
        persistFlowHistory();
        assertEquals(6, flowRepository.findFlows(allFlows).size());
        flowPurgerMBean.getScheduler().getListenerManager().addJobListener(new PurgeJobListener(latch));
        flowPurgerMBean.setDoNotPurgeErrorFlows(false);
        flowPurgerMBean.setPurgeFlowsOlderThan("9d");
        flowPurgerMBean.execute();
        latch.await(WAIT_TIMEOUT, TimeUnit.SECONDS);        
        assertEquals(1, flowRepository.findFlows(allFlows).size());
        flowPurgerMBean.unschedule();
    }
    
    private void removeAll(){
        List<Flow> flows = flowRepository.findFlows(allFlows);
        for (Flow flow: flows){
            flowRepository.remove(flow);
        }
    }

    private void persistFlowHistory() throws IOException {
        Flow flow1 = createFlow("f1");
        Flow flow2 = createFlow("f2");
        Flow flow3 = createFlow("f3");
        Flow flow4 = createFlow("f4");
        Flow flow5 = createFlow("f5");
        Flow flow6 = createFlow("f6");
        flow1.setCreationTime(new Date(1));
        flow2.setCreationTime(new Date(2));
        flow3.setCreationTime(new Date(3));
        flow4.setCreationTime(new Date(4));
        flow5.setCreationTime(new Date(5));
        flow6.setCreationTime(new Date(System.currentTimeMillis() - EIGHT_DAYS));
        flow3.getPart("0.0").setStatus(FlowStatus.ERROR);
        flow3.setDerivedStatus(FlowStatus.ERROR);
        flowRepository.persist(flow1);
        flowRepository.persist(flow2);
        flowRepository.persist(flow3);
        flowRepository.persist(flow4);
        flowRepository.persist(flow5);
        flowRepository.persist(flow6);        
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
    }


    private static class PurgeJobListener implements JobListener {
        CountDownLatch startLatch;

        PurgeJobListener(CountDownLatch startLatch){
            this.startLatch = startLatch;
        }

        @Override
        public String getName() {
            return "test";
        }

        @Override
        public void jobExecutionVetoed(JobExecutionContext arg0) {
            }

        @Override
        public void jobToBeExecuted(JobExecutionContext arg0) {
        }

        @Override
        public void jobWasExecuted(JobExecutionContext arg0,
            JobExecutionException arg1) {
            startLatch.countDown();
        }
    }

}
