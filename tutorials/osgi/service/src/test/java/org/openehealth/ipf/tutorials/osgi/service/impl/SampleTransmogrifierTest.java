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
package org.openehealth.ipf.tutorials.osgi.service.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Krasser
 */
public class SampleTransmogrifierTest {

    private SampleTransmogrifier transmogrifier;
    
    @Before
    public void setUp() throws Exception {
        transmogrifier = new SampleTransmogrifier();
    }

    @Test
    public void testStandardResponse() {
        assertEquals("Prefix: test", transmogrifier.zap("test", (Object[])null));
    }
    
    @Test
    public void testCustomResponse() {
        transmogrifier.setPrefix("Blah: ");
        assertEquals("Blah: test", transmogrifier.zap("test", (Object[])null));
    }
    
}
