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
package org.openehealth.ipf.tutorials.ref;

import junit.framework.Test;

import com.clarkware.junitperf.LoadTest;

/**
 * @author Martin Krasser
 */
public class TutorialRouteBuilderLoadTest {

    private static final int THREADS = 5;

    private static final int LOOPS = 1000;
    
    public static Test suite() throws Exception {
        TutorialRouteBuilderIntegrationTest.setUpBeforeClass();
        Test test = new TutorialRouteBuilderIntegrationTest("testSendOrders");
        return new LoadTest(test, THREADS, LOOPS);
    }

    public static void main(String[] args) throws Exception {
        junit.textui.TestRunner.run(suite());
    }

}
