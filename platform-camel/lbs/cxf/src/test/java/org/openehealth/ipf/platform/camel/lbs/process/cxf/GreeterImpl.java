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
package org.openehealth.ipf.platform.camel.lbs.process.cxf;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.ws.Holder;

import org.apache.commons.io.IOUtils;

/**
 * @author Jens Riemschneider
 */
public class GreeterImpl implements Greeter {

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.process.cxf.Greeter#greetMe(java.lang.String)
     */
    @Override
    public String greetMe(String requestType) {
        return "Hello from the real world. Request was " + requestType;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.process.cxf.Greeter#postMe(javax.xml.ws.Holder, javax.xml.ws.Holder, javax.activation.DataHandler)
     */
    @Override
    public void postMe(Holder<String> name, Holder<DataHandler> attachinfo, DataHandler onewayattach) {
        name.value = "Hello from the real world. Request was " + name.value;

        try {
            String content = toString(attachinfo.value);
            String onewayContent = toString(onewayattach);
            String contentType = attachinfo.value.getContentType();
            String resultContent = "I received: " + content + "/" + onewayContent;
            attachinfo.value = new DataHandler(new ByteArrayDataSource(resultContent, contentType));
        } 
        catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.process.cxf.Greeter#pingMe(javax.activation.DataHandler)
     */
    @Override
    public DataHandler pingMe(DataHandler attachinfo) {
        return attachinfo;
    }   
}
