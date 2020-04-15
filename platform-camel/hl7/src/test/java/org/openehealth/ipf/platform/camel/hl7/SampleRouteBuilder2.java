/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.hl7;

import org.apache.camel.spring.SpringRouteBuilder;

import static org.openehealth.ipf.platform.camel.hl7.HL7v2.*;

/**
 * This route is the Java version of {@link org.openehealth.ipf.platform.camel.hl7.extend.SampleRouteBuilder}
 * using expressions and predicates defined by {@link org.openehealth.ipf.platform.camel.hl7.HL7v2}.
 */
public class SampleRouteBuilder2 extends SpringRouteBuilder {

    @Override
    public void configure() {

        from("direct:input1")
                // create a message adapter from an HL7 string
                .unmarshal().hl7()
                .transform(set("MSH-5", header("foo")))
                .choice()
                    .when(get("MSH-5").isEqualTo("blah"))
                        .convertBodyTo(String.class) // adapter -> string
                        .to("mock:output1")
                    .when(get("MSH-5").isEqualTo("blub"))
                        .convertBodyTo(String.class) // adapter -> string
                        .to("mock:output2");
    }

}
