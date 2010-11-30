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
package org.openehealth.ipf.platform.camel.cda.builder;

import org.junit.Test;
import org.openehealth.ipf.modules.cda.builder.CDAR2Builder;

/**
 * Ensures that the modules-cda artifact contains the required builder resources
 * (modules-cda.jar/builders/.../*.groovy)
 * 
 * @author Mitko Kolev
 */
public class CDAArtifactTest {

    /**
     * Without the builder resources the CDAR2Builder can not be instantiated.
     */
    @Test
    public void testModulesArtifactContainsCDABuilderResources() {
        new CDAR2Builder(getClass().getClassLoader());
    }

}
