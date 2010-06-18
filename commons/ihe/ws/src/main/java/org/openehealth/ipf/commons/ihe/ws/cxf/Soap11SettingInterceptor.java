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

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapPreProtocolOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.ws.addressing.JAXWSAConstants;

/**
 * SOAP interceptor that changes SOAP version of outgoing messages from 1.2 to 1.1.
 * <p>
 * Should be used on producer side only.
 * 
 * @author Dmytro Rud
 */
public class Soap11SettingInterceptor extends AbstractSoapInterceptor {

    public Soap11SettingInterceptor() {
        super(Phase.POST_LOGICAL);
        addBefore(SoapPreProtocolOutInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        message.setVersion(Soap11.getInstance());
        
        // create HTTP header "SOAPAction:"
        MessageInfo messageInfo = message.get(MessageInfo.class);
        QName action = (QName) messageInfo.getExtensionAttribute(JAXWSAConstants.WSAW_ACTION_QNAME);
        message.put(SoapBindingConstants.SOAP_ACTION, action.getLocalPart());
    }
}
