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
package org.openehealth.ipf.commons.ihe.ws.cxf;

import org.apache.cxf.attachment.AttachmentSerializer;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.AttachmentOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

/**
 * Workaround for a compatibility issue with Axis 2 0.9x.
 * <p>
 * Older versions of Axis 2 only accept our message if the content-type
 * contains a type of "text/xml". The CXF {@link AttachmentSerializer}
 * does not set this field. Therefore, we add it with this interceptor.
 * @author Jens Riemschneider
 */
public class FixContentTypeOutInterceptor extends AbstractSoapInterceptor {
    /**
     * Constructs the interceptor.
     */
    public FixContentTypeOutInterceptor() {
        super(Phase.PRE_STREAM);
        addAfter(AttachmentOutInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        // Cool stuff for Axis 0.9
        var ct  = (String) message.get(Message.CONTENT_TYPE);
        if (!ct.contains("type=")) {
            message.put(Message.CONTENT_TYPE, ct + "; type=\"text/xml\"");
        }
    }
}

