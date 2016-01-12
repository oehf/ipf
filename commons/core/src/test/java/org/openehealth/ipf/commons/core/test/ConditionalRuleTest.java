/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.core.test;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 *
 */
public class ConditionalRuleTest {

    private static final String currentArchitecture = System.getProperty("os.name");

    @Rule public ConditionalRule rule = new ConditionalRule()
            .ifSystemPropertyIs("os.name", currentArchitecture);

    @Test
    public void testRun() {
        rule.verify();
    }

    @Test
    public void testIgnore() {
        rule.negate().verify();
        fail("Should not be executed");
    }

}
