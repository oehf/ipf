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
package org.openehealth.ipf.tutorials.osgi.route.file

import org.apache.camel.builder.RouteBuilder

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig
/**
 * @author Martin Krasser
 */
public class SampleRouteConfig implements RouteBuilderConfig {

     void apply(RouteBuilder builder) {

         builder
             .from('file:workspace/input?delete=true')
             .initFlow('file').application('osgi-file')
             .unmarshal().ghl7()
             .transmogrify(context.admissionTransmogrifier)
             .marshal().ghl7()
             .to('file:workspace/output?append=false&autoCreate=false')
             .ackFlow()

     }
    
}
