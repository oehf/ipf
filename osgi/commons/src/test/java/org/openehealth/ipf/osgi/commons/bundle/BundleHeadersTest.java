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
package org.openehealth.ipf.osgi.commons.bundle;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.openehealth.ipf.osgi.commons.bundle.BundleHeaders.*;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.springframework.osgi.mock.MockBundle;

/**
 * @author Martin Krasser
 */
public class BundleHeadersTest {

    private Bundle bundle;
    
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        bundle = new MockBundle();
        bundle.getHeaders().put(EXTENSION_CLASSES_HEADER, "test.X, test.Y");
        bundle.getHeaders().put(EXTENSION_BEANS_HEADER, "beanX, beanY");
    }

    @Test
    public void testExtensionClasses() {
        assertEquals(asList("test.X", "test.Y"), extensionClasses(bundle));
    }
    
    @Test
    public void testExtensionBeans() {
        assertEquals(asList("beanX", "beanY"), extensionBeans(bundle));
    }
    
}
