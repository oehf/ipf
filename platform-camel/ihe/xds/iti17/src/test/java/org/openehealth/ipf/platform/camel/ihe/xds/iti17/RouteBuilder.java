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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17;

import org.apache.camel.spring.SpringRouteBuilder;

/**
 * @author Jens Riemschneider
 */
public class RouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("xds-iti17:xds-iti17-service1")
            .process(new RetrieveDocumentProcessor("service 1: "));

        from("xds-iti17:xds-iti17-service2")
            .process(new RetrieveDocumentProcessor("service 2: "));
    }
}