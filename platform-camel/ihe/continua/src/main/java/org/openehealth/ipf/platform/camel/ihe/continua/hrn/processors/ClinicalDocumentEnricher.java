/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;

/**
 * Enriches the Document instance with a ClinicalDocument instance.
 * 
 * @author Stefan Ivanov
 * 
 */
public class ClinicalDocumentEnricher implements Processor {
    
    @Override
    public void process(Exchange exchange) throws Exception {
        ProvideAndRegisterDocumentSet set= exchange.getIn().getBody(ProvideAndRegisterDocumentSet.class);
        ClinicalDocument cdaInstance = exchange.getIn().getBody(ClinicalDocument.class);
        set.getDocuments().get(0).addContents(ClinicalDocument.class, cdaInstance);
    }
    
}
