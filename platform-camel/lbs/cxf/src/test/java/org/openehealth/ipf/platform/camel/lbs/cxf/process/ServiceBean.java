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
package org.openehealth.ipf.platform.camel.lbs.cxf.process;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.CxfConstants;
import org.apache.commons.io.IOUtils;

public class ServiceBean {
    private Processor checkProcessor;

    public Object[] processSOAP(Exchange exchange) throws Exception {
        callChecks(exchange);
        
        if (exchange.getIn().getHeader(CxfConstants.OPERATION_NAME).equals("greetMe")) {        
            return greetMe(exchange);
        }
        else if (exchange.getIn().getHeader(CxfConstants.OPERATION_NAME).equals("postMe")) {
            return postMe(exchange);
        }
        else if (exchange.getIn().getHeader(CxfConstants.OPERATION_NAME).equals("pingMe")) {
            return pingMe(exchange);
        }
        return null;
    }

    private void callChecks(Exchange exchange) throws Exception {
        if (checkProcessor != null) {
            checkProcessor.process(exchange);
        }
    }

    private Object[] postMe(Exchange exchange) throws Exception {
        List<?> params = exchange.getIn().getBody(List.class);
        Holder<String> name = (Holder<String>) params.get(0);
        Holder<DataHandler> attachInfo = (Holder<DataHandler>) params.get(1);
        DataHandler onewayAttach = (DataHandler) params.get(2);
        
        name.value = "Greetings from Apache Camel!!!! Request was " + name.value;
        
        String contentType = attachInfo.value.getContentType();
        String resultContent = toString(attachInfo.value) + toString(onewayAttach);
        
        DataSource outDataSource = new ByteArrayDataSource(resultContent.getBytes(), contentType);
        attachInfo.value = new DataHandler(outDataSource);
        
        return new Object[] { null, name, attachInfo };
    }

    private Object[] pingMe(Exchange exchange) throws Exception {
        List<?> params = exchange.getIn().getBody(List.class);
        return new Object[] { params.get(0) };
    }

    private String toString(DataHandler onewayAttach) throws IOException {
        InputStream inputStream = onewayAttach.getInputStream();
        try {
            return IOUtils.toString(inputStream);
        }
        finally {
            inputStream.close();
        }
    }   

    private Object[] greetMe(Exchange exchange) throws Exception {
        List<?> params = exchange.getIn().getBody(List.class);
        String name = (String) params.get(0);
        
        return new Object[] { "Greetings from Apache Camel!!!! Request was " + name };
    }
    
    public void setCheckProcessor(Processor checkProcessor) {
        this.checkProcessor = checkProcessor;
    }
}
