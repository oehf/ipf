/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.platform.camel.cda.dataformat;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatName;
import org.apache.camel.spi.annotations.Dataformat;
import org.apache.camel.support.service.ServiceSupport;
import org.openehealth.ipf.modules.cda.CDAR2Parser;
import org.openehealth.ipf.modules.cda.CDAR2Renderer;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;

@Dataformat("mdht")
public class MdhtDataFormat extends ServiceSupport implements DataFormat, DataFormatName {

    private final CDAR2Renderer renderer = new CDAR2Renderer();
    private final CDAR2Parser parser = new CDAR2Parser();

    public MdhtDataFormat() {
    }

    @Override
    public String getDataFormatName() {
        return "mdht";
    }

    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream)
            throws Exception {
        renderer.render((ClinicalDocument) graph, stream,
                (Object[]) null);
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) {
        return parser.parse(stream, (Object[]) null);
    }

    @Override
    public void doStart() {
    }

    @Override
    public void doStop() {
    }
}
