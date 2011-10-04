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

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.databinding.AbstractDataBinding;
import org.apache.cxf.databinding.DataReader;
import org.apache.cxf.databinding.DataWriter;
import org.apache.cxf.service.Service;

/**
 * Special CXF data binding for plain XML transactions (e.g. HL7 v3). 
 * @author Dmytro Rud
 */
public class PlainXmlDataBinding extends AbstractDataBinding {

    @SuppressWarnings("unchecked")
    @Override
    public <T> DataReader<T> createReader(Class<T> cls) {
        return (DataReader<T>) new PlainXmlReader();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> DataWriter<T> createWriter(Class<T> cls) {
        return (DataWriter<T>) new PlainXmlWriter();
    }

    @Override
    public Class<?>[] getSupportedReaderFormats() {
        return new Class[] {XMLStreamReader.class};
    }

    @Override
    public Class<?>[] getSupportedWriterFormats() {
        return new Class[] {XMLStreamWriter.class};
    }

    @Override
    public void initialize(Service service) {
        // nop
    }
}
