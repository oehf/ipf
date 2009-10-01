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
package org.openehealth.ipf.osgi.extender.basic;


import static org.junit.Assert.assertEquals;
import static org.openehealth.ipf.osgi.commons.bundle.BundleHeaders.EXTENSION_CLASSES_HEADER;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.springframework.osgi.mock.MockBundle;

/**
 * @author Martin Krasser
 */
public class ExtenderActivatorTest {

    private static BundleEvent bundleEvent;
    
    private static ExtenderActivator bundleListener;
    
    @BeforeClass
    @SuppressWarnings("unchecked")
    public static void setUpBeforeClass() throws Exception {
        Bundle bundle = new MockBundle();
        bundle.getHeaders().put(EXTENSION_CLASSES_HEADER, ExtensionsCount.class.getName());
        bundleEvent = new BundleEvent(BundleEvent.STARTED, bundle);
        bundleListener = new ExtenderActivator();
    }

    @Before
    public void setUp() throws Exception {
        ExtensionsCount.reset();
    }

    @Test
    public void testBundleChanged() {
        bundleListener.bundleChanged(bundleEvent);
        assertEquals(1, ExtensionsCount.getValue());
    }

}
