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
package org.openehealth.ipf.commons.ihe.ws.cxf.databinding.plainxml;

import java.util.Collection;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;

import org.apache.cxf.databinding.DataReader;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.service.model.MessagePartInfo;

/**
 * Fake data reader which reads in the whole XML stream and returns 
 * a <code>null</code> value instead of the expected POJO. 
 * @author Dmytro Rud
 */
public class FakeReader implements DataReader<XMLStreamReader>{

    @Override
    public Object read(XMLStreamReader reader) {
        try {
            while(reader.hasNext()) { 
                reader.next();
            }
        } catch (XMLStreamException e) {
            // nobody cares
        }
        return null;
    }

    @Override
    public Object read(MessagePartInfo part, XMLStreamReader reader) {
        return read(reader);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object read(QName elementQName, XMLStreamReader reader, Class type) {
        return read(reader);
    }

    @Override
    public void setAttachments(Collection<Attachment> attachments) {
        // nop
    }

    @Override
    public void setProperty(String prop, Object value) {
        // nop
    }

    @Override
    public void setSchema(Schema s) {
        // nop
    }
}
