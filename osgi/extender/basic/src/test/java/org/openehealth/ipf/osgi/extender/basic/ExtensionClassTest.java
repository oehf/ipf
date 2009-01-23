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
import org.openehealth.ipf.osgi.extender.basic.ExtensionsCount;
import org.openehealth.ipf.osgi.extender.basic.ExtensionClass;
import org.osgi.framework.Bundle;
import org.springframework.osgi.mock.MockBundle;

/**
 * @author Martin Krasser
 */
public class ExtensionClassTest {
    
    private static Bundle bundle;
    
    @BeforeClass
    @SuppressWarnings("unchecked")
    public static void setUpBeforeClass() throws Exception {
        bundle = new MockBundle();
        bundle.getHeaders().put(EXTENSION_CLASSES_HEADER, ExtensionsCount.class.getName());
    }
    
    @Before
    public void setUp() throws Exception {
        ExtensionsCount.reset();
    }

    @Test
    public void testLoadAll() {
        assertEquals(1, ExtensionClass.loadAll(bundle).size());
        assertEquals(0, ExtensionsCount.getValue());
    }

    @Test
    public void testActivateAll() {
        ExtensionClass.activateAll(bundle);
        assertEquals(1, ExtensionsCount.getValue());
    }

}
