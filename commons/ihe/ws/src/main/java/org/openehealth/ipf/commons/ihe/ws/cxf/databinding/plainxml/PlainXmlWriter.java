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

import org.apache.commons.io.input.XmlStreamReader;
import org.apache.cxf.databinding.DataWriter;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.staxutils.StaxUtils;
import org.w3c.dom.Document;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;

/**
 * A special writer for some transactions (e.g. HL7 v3) whose    
 * expected input data is some representation of an XML element.
 * Following data types are supported:
 * <ul>
 *     <li>{@link String}</li>
 *     <li><tt>byte[]</tt></li>
 *     <li>{@link InputStream}</li>
 *     <li>{@link Reader}</li>
 *     <li>DOM {@link Document}</li>
 *     <li>{@link Source}</li>
 * </ul>
 *
 * @author Dmytro Rud
 */
public class PlainXmlWriter implements DataWriter<XMLStreamWriter> {

    @Override
    public void write(Object obj, MessagePartInfo part, XMLStreamWriter writer) {
        try {
            if (obj instanceof String) {
                obj = ((String) obj).getBytes();
            }
            if (obj instanceof byte[]) {
                obj = new ByteArrayInputStream((byte[]) obj);
            }
            if (obj instanceof InputStream) {
                obj = new XmlStreamReader((InputStream) obj);
            }
            if (obj instanceof Reader) {
                StaxUtils.copy(StaxUtils.createXMLStreamReader((Reader) obj), writer);
                return;
            }

            if (obj instanceof Document) {
                StaxUtils.copy((Document) obj, writer);
                return;
            }

            if (obj instanceof Source) {
                StaxUtils.copy((Source) obj, writer);
                return;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException("Cannot handle instance of type " +
                ((obj == null) ? "<null>" : obj.getClass().getName()));
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
