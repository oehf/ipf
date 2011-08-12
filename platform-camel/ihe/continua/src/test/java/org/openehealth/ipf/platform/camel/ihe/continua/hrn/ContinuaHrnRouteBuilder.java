/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;

import javax.activation.DataHandler;

/**
 * Test Continua HRN Route Builder.
 * 
 * @author Stefan Ivanov
 */
public class ContinuaHrnRouteBuilder extends RouteBuilder {
    
    private static final Processor CHECK_PROCESSOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            Object body = exchange.getIn().getBody();
            if (! (body instanceof ProvideAndRegisterDocumentSet)) {
                throw new Exception("wrong body type");
            }

            Class<?>[] expectedContentTypes = new Class<?>[] {
                    DataHandler.class,
                    byte[].class,
                    String.class
            };
            Document document = exchange.getIn().getBody(ProvideAndRegisterDocumentSet.class).getDocuments().get(0);
            for (Class<?> contentType : expectedContentTypes) {
                if (! document.hasContent(contentType)) {
                    throw new Exception("Missing content type: " + contentType);
                }
            }

            if (document.getContentsCount() != expectedContentTypes.length) {
                throw new Exception("Expected " + expectedContentTypes.length +
                        " content types, found " + document.getContentsCount());
            }
        }
    };


    @Override
    public void configure() throws Exception {
        
        from("xds-iti41:continuaHRNService")
            .onException(Exception.class)
                .maximumRedeliveries(0)
                .end()
            .process(ContinuaHrnCamelProcessors.continuaHrnRequestTransformerAndValidator())
            .process(CHECK_PROCESSOR)
            .setBody(constant(new Response(Status.SUCCESS)))
            .process(ContinuaHrnCamelProcessors.continuaHrnResponseValidator());
    }

}
