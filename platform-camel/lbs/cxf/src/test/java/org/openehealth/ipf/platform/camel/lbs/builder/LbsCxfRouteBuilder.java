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
package org.openehealth.ipf.platform.camel.lbs.builder;

import java.util.List;

import org.openehealth.ipf.platform.camel.lbs.process.ResourceHandler;
import org.openehealth.ipf.platform.camel.lbs.process.cxf.AbstractLbsCxfTest;

/**
 * @author Jens Riemschneider
 */
public class LbsCxfRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        List<ResourceHandler> handlers = bean(List.class, "resourceHandlers");
        
        from("cxf:bean:soapEndpointNoExtract?dataFormat=POJO") 
            .to("bean:serviceBean?methodName=processSOAP");
        
        from("cxf:bean:soapEndpointExtract?dataFormat=POJO")
            .intercept(new AbstractLbsCxfTest.CheckOutputDataSource())
            .intercept(store().with(handlers))
            .to("bean:serviceBean?methodName=processSOAP")
            .intercept(store().with(handlers));
        
        from("cxf:bean:soapEndpointExtractRouter?dataFormat=POJO")
            .intercept(new AbstractLbsCxfTest.CheckOutputDataSource())
            .intercept(store().with(handlers))
            .to("cxf:bean:soapEndpointExtract?dataFormat=POJO")
            .intercept(store().with(handlers));

        from("cxf:bean:soapEndpointExtractRouterRealServer?dataFormat=POJO")
            .intercept(new AbstractLbsCxfTest.CheckOutputDataSource())
            .intercept(store().with(handlers))
            .to("cxf:bean:soapEndpointRealServer?dataFormat=POJO")
            .intercept(store().with(handlers));
        
        from("direct:cxflbs")
            .intercept(store().with(handlers))
            .to("mock:mock")
            .intercept(store().with(handlers));

        from("cxf:bean:soapEndpointExtractSwA?dataFormat=POJO")
            .intercept(new AbstractLbsCxfTest.CheckOutputDataSource())
            .intercept(store().with(handlers))
            .to("bean:serviceBean?methodName=processSOAP")
            .intercept(store().with(handlers));
    }
}
