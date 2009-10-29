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
package org.openehealth.ipf.commons.test.performance.processingtime;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.apache.commons.math.stat.descriptive.StatisticalSummary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import static org.openehealth.ipf.commons.test.performance.PerformanceMeasurementTestUtils.createMeasurementHistory;

/**
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-processingtime-descriptive-statistics.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class ProcessingTimeDescriptiveStatisticsTest {

    @Autowired
    ProcessingTimeDescriptiveStatistics statistics;

    @Autowired
    ProcessingTimeDescriptiveStatisticsRenderer renderer;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        statistics.reset();
    }

    @Test
    public void testUpdateOneContext() {
        statistics.update(createMeasurementHistory());
        assertEquals(1, statistics.getMeasurementNames().size());
    }

    @Test
    public void testNamesTwoContexts() {
        // generate data with random location
        statistics.update(createMeasurementHistory());
        statistics.update(createMeasurementHistory(3));
        List<String> names = statistics.getMeasurementNames();
        assertEquals(2, names.size());
    }

    @Test
    public void testTwoContextsRefined() {
        // generate data with random location
        statistics.update(createMeasurementHistory());
        statistics.update(createMeasurementHistory(3));
        List<String> names = statistics.getMeasurementNames();

        StatisticalSummary summary0 = statistics
                .getStatisticalSummaryByName(names.get(0));
        StatisticalSummary summary1 = statistics
                .getStatisticalSummaryByName(names.get(1));

        assertEquals(2l, summary0.getN());
        assertEquals(1l, summary1.getN());

    }

    @Test
    public void testReportsContainNumberOfUpdates() {
        int maxUpdates = 30;
        for (int updates = 0; updates < maxUpdates; updates++) {
            statistics.update(createMeasurementHistory());
        }
        String report = renderer.render(statistics);
        System.out.println(report);

        String[] measurements = report.split(",");
        // the report contains maxUpdates number of measurements, separated by
        // commas
        assertEquals(maxUpdates, measurements.length);

    }

    @Test
    public void testCreatesCSVFile() throws Exception {
        int maxUpdates = 30;
        for (int updates = 0; updates < maxUpdates; updates++) {
            statistics.update(createMeasurementHistory());
        }
        renderer.setCreateMeasurementLocationDataFiles(true);
        //do the rendering
        renderer.render(statistics);
        
        File currentDir = new File(".");
        File[] files = currentDir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                if (name.contains("measurement-location") && name.contains(".csv")) {
                    return true;
                } else {
                    return false;
                }
            }

        });
        assertNotSame(0, files.length);
        //clean up
        for(File f : files){
            f.delete();
        }
        //restore the statistics state
        renderer.setCreateMeasurementLocationDataFiles(false);
    }

}
