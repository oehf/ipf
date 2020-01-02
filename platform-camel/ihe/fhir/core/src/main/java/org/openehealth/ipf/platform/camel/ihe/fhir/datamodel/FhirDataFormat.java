/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.datamodel;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.support.ExchangeHelper;
import org.apache.camel.support.service.ServiceSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.Constants;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 */
abstract class FhirDataFormat extends ServiceSupport implements DataFormat  {

    private FhirContext defaultFhirContext = FhirContext.forDstu3();
    private Charset defaultCharset = StandardCharsets.UTF_8;

    public void setDefaultFhirContext(FhirContext context) {
        defaultFhirContext = context;
    }

    public void setDefaultCharset(String charset) {
        defaultCharset = Charset.forName(charset);
    }

    @Override
    public void marshal(Exchange exchange, Object body, OutputStream stream) throws Exception {
        IBaseResource resource = ExchangeHelper.convertToMandatoryType(exchange, IBaseResource.class, body);
        Writer writer = new OutputStreamWriter(stream, getCharset(exchange));
        getParser(getFhirContext(exchange))
                .setPrettyPrint(true)
                .encodeResourceToWriter(resource, writer);
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) {
        Reader reader = new InputStreamReader(stream, getCharset(exchange));
        return getParser(getFhirContext(exchange)).parseResource(reader);
    }

    protected FhirContext getFhirContext(Exchange exchange) {
        FhirContext context = exchange.getIn().getHeader(Constants.FHIR_CONTEXT, FhirContext.class);
        if (context == null) context = defaultFhirContext;
        return context;
    }

    protected Charset getCharset(Exchange exchange) {
        Charset charset = defaultCharset;
        String charsetName = exchange.getProperty(Exchange.CHARSET_NAME, String.class);
        if (charsetName != null) charset = Charset.forName(charsetName);
        return charset;
    }

    protected abstract IParser getParser(FhirContext context);

    @Override
    protected void doStart() {
    }

    @Override
    protected void doStop() {
    }
}
