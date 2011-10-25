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

import org.apache.cxf.databinding.DataWriter;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.staxutils.StaxUtils;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.validation.Schema;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;

/**
 * A special writer for some transactions (e.g. HL7 v3) whose
 * expected input data is String representation of an XML element.
 *
 * @author Dmytro Rud
 */
public class PlainXmlWriter implements DataWriter<XMLStreamWriter> {

    @Override
    public void write(Object obj, MessagePartInfo part, XMLStreamWriter writer) {
        try {
            String s = (String) obj;
            Reader reader = new StringReader(s);
            StaxUtils.copy(StaxUtils.createXMLStreamReader(reader), writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void write(Object obj, XMLStreamWriter writer) {
        write(obj, null, writer);
    }

    @Override
    public void setAttachments(Collection<Attachment> attachments) {
        // nop
    }

    @Override
    public void setProperty(String key, Object value) {
        // nop
    }

    @Override
    public void setSchema(Schema s) {
        // nop
    }
}
