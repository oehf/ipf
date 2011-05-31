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
package org.openehealth.ipf.platform.camel.test.performance.extend;

import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.platform.camel.test.performance.model.MeasureDefinition;

/**
 * Performance DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} using the {@code use} keyword.
 *
 * @DSL
 *
 * @author Jens Riemschneider
 */
class PerformanceExtension {
    /**
     * Defines a performance measurement location in the route 
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Performance+measurement#Performancemeasurement-dslextensions
     */
    public static MeasureDefinition measure(ProcessorDefinition self) { 
        MeasureDefinition answer = new MeasureDefinition();
        self.addOutput(answer);
        return answer;
    }
}
