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

import java.util.ArrayList;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.cxf.attachment.AttachmentImpl;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.AttachmentOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxws.interceptors.HolderOutInterceptor;
import org.apache.cxf.jaxws.interceptors.WrapperClassOutInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * Interceptor to add provided outgoing attachments for SwA on the client side.
 * @author Jens Riemschneider
 */
public class ProvidedAttachmentOutInterceptor extends AbstractSoapInterceptor {
    /**
     * Context property that contains the provided attachments that need to be
     * added to the message.
     */
    public static final String ATTACHMENTS = 
        ProvidedAttachmentOutInterceptor.class.getName() + ".provided_attachments";

    /**
     * Constructs the interceptor.
     */
    public ProvidedAttachmentOutInterceptor() {
        super(Phase.PRE_LOGICAL);
        addAfter(HolderOutInterceptor.class.getName());
        addBefore(WrapperClassOutInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        var providedAttachments = (Map<?, ?>) message.getContextualProperty(ATTACHMENTS);
        if (providedAttachments.isEmpty()) {
            return;
        }

        var attachments = message.getAttachments();
        if (attachments == null) {
            attachments = new ArrayList<>();
            message.setAttachments(attachments);
        }
        
        for (Map.Entry<?, ?> entry : providedAttachments.entrySet()) {
            attachments.add(new AttachmentImpl((String)entry.getKey(), (DataHandler)entry.getValue()));
        }

        message.put(AttachmentOutInterceptor.WRITE_ATTACHMENTS, Boolean.TRUE);
    }
}
