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
package org.openehealth.ipf.platform.camel.lbs.cxf.builder;

import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.lbs.core.process.ResourceHandler;
import org.openehealth.ipf.platform.camel.lbs.cxf.process.AbstractLbsCxfTest;

import java.util.List;

import static org.openehealth.ipf.platform.camel.lbs.core.builder.RouteHelper.store;

/**
 * @author Jens Riemschneider
 */
public class LbsCxfRouteBuilderJava extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        List<ResourceHandler> handlers = lookup("resourceHandlers", List.class);
        
        from("cxf:bean:soapEndpointNoExtract?dataFormat=PAYLOAD") 
            .to("bean:serviceBean?method=processSOAP");
        
        from("cxf:bean:soapEndpointExtract?dataFormat=POJO")
            .process(store().with(handlers))
            .to("bean:serviceBean?method=processSOAP")
            .process(store().with(handlers))
            .process(new AbstractLbsCxfTest.CheckOutputDataSource());
        
        from("cxf:bean:soapEndpointExtractRouter?dataFormat=POJO")
            .process(store().with(handlers))
            .to("cxf:bean:soapEndpointExtract?dataFormat=POJO&headerFilterStrategy=#routerHeaderFilterStrategy")
            .process(store().with(handlers))
            .process(new AbstractLbsCxfTest.CheckOutputDataSource());

        from("cxf:bean:soapEndpointExtractRouterRealServer?dataFormat=POJO")
            .process(store().with(handlers))
            .to("cxf:bean:soapEndpointRealServer?dataFormat=POJO&headerFilterStrategy=#routerHeaderFilterStrategy")
            .process(store().with(handlers))
            .process(new AbstractLbsCxfTest.CheckOutputDataSource());
        
        from("direct:cxflbs")
            .process(store().with(handlers))
            .to("mock:mock")
            .process(store().with(handlers));

        from("cxf:bean:soapEndpointExtractSwA?dataFormat=POJO")
            .process(store().with(handlers))
            .to("bean:serviceBean?method=processSOAP")
            .process(store().with(handlers))
            .process(new AbstractLbsCxfTest.CheckOutputDataSource());
    }
}
