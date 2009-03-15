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
package org.openehealth.ipf.platform.camel.core.junit;

import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.runner.notification.RunNotifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/**
 * This test runner dirties a Spring application context after all test methods
 * of a test class has been run.
 * This allows test methods to use the same application context but mark it as
 * dirty to ensure that another test class recreates the application context.
 * <p><b>
 * ATTENTION: Using this test runner will slow down overall test execution
 *            significantly!!! Do not use this runner if you don't really need 
 *            it. Use {@link SpringJUnit4ClassRunner} instead.
 * </b><p>
 * Usage:
 * <blockquote><code><pre>
 * &#064;RunWith(DirtySpringContextJUnit4ClassRunner.class)
 * public class MyTest { ...
 * </pre></code></blockquote>
 * For this runner to work it is important the the {@link DirtiesContextTestExecutionListener}
 * is used. By default this is the case when using this runner. If you specify
 * the listeners explicitly via the {@link TestExecutionListeners} annotation,
 * make sure that the {@link DirtiesContextTestExecutionListener} is included. E.g.:
 * <blockquote><code><pre>
 * &#064;RunWith(DirtySpringContextJUnit4ClassRunner.class)
 * &#064;TestExecutionListeners({DirtiesContextTestExecutionListener.class, ...})
 * public class MyTest { ...
 * </pre></code></blockquote>
 * @author Jens Riemschneider
 */
public class DirtySpringContextJUnit4ClassRunner extends SpringJUnit4ClassRunner {

    /**
     * Standard constructor
     * @param clazz
     *          see {@link SpringJUnit4ClassRunner}
     * @throws InitializationError
     *          see {@link SpringJUnit4ClassRunner}
     */
    public DirtySpringContextJUnit4ClassRunner(Class<?> clazz)
            throws InitializationError {
        super(clazz);
    }

    @Override
    protected void runMethods(RunNotifier notifier) {
        super.runMethods(notifier);
        dirtyContext();
    }

    private void dirtyContext() throws AssertionError {
        ensureDirtyContextListenerIsUsed();
        
        // run the standard after test method listeners from the Spring JUnit4
        // runner on a dummy test method that defines the @DirtiesContext 
        // annotation. This ensures that the DirtiesContextTestExecutionListener
        // will be triggered and dirties the application context.
        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(DirtiesContext.class)) {
                try {
                    getTestContextManager().afterTestMethod(this, method, null);
                } catch (Exception e) {
                    throw new AssertionError(e);
                }
            }
        }
    }
    
    private void ensureDirtyContextListenerIsUsed() {
        for (TestExecutionListener listener : getTestContextManager().getTestExecutionListeners()) {
            if (listener instanceof DirtiesContextTestExecutionListener) {
                return;
            }
        }
        
        throw new AssertionError(
                "DirtiesContextTestExecutionListener must be added when using DirtySpringContextJUnit4ClassRunner");
    }
    
    @DirtiesContext
    public void dirtyContextDummy() {        
        // Does nothing. But ensures that the annotation is interpreted by
        // afterTestMethod
    }
}
