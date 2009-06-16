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
package org.openehealth.ipf.modules.cda;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import javax.xml.transform.Result;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.openehealth.ipf.commons.core.modules.api.RenderException;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openhealthtools.ihe.common.cdar2.CDAR2Factory;
import org.openhealthtools.ihe.common.cdar2.CDAR2Package;
import org.openhealthtools.ihe.common.cdar2.DocumentRoot;
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ClinicalDocument;
import org.openhealthtools.ihe.common.cdar2.util.CDAR2ResourceFactoryImpl;

/**
 * @author Christian Ohr
 */
public class CDAR2Renderer implements Renderer<POCDMT000040ClinicalDocument> {

    public String render(POCDMT000040ClinicalDocument doc, Object... options) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            render(doc, bos, options);
            return bos.toString();
        } catch (IOException e) {
            throw new RenderException(e);
        }

    }

    /**
     * Renders the clinical document as XML. Options can be specified for
     * dynamic configuration of the rendering process. 
     * See {@link http://help.eclipse.org/stable/index.jsp?topic=/org.eclipse.emf.doc/references/javadoc/org/eclipse/emf/ecore/xmi/XMLResource.html} 
     * for details.
     * 
     * @param doc the clinical document
     * @param os the stream to write the XML to
     * @param options may contains a map with options for rendering.
     */
    @SuppressWarnings("unchecked")
    public OutputStream render(POCDMT000040ClinicalDocument doc,
            OutputStream os, Object... options) throws IOException {
        XMLResource resources = (XMLResource) new CDAR2ResourceFactoryImpl()
                .createResource(URI.createURI(CDAR2Package.eNS_URI));
        // set to true if want the XML declaration printed, set to FALSE if you
        // don't. TODO merge this with dynamic options.
        resources.getDefaultSaveOptions().put(XMLResource.OPTION_DECLARE_XML,
                Boolean.TRUE);
        
        if (options != null && options.length > 0 && options[0] instanceof Map) {
            resources.getDefaultSaveOptions().putAll((Map)options[0]);
        }
        
        // Create document root
        DocumentRoot root = CDAR2Factory.eINSTANCE.createDocumentRoot();
        root.getXMLNSPrefixMap().put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.getXMLNSPrefixMap().put("", "urn:hl7-org:v3");
        root.getXSISchemaLocation().put("urn:hl7-org:v3", "CDA.xsd");
        root.setClinicalDocument(doc);
        resources.getContents().add(root);
        // Write to stream
        resources.save(os, resources.getDefaultSaveOptions());
        return os;
    }

    public Result render(POCDMT000040ClinicalDocument doc, Result result,
            Object... options) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Writer render(POCDMT000040ClinicalDocument doc, Writer writer,
            Object... options) throws IOException {
        // TODO avoid temporary string object
        String s = render(doc, options);
        writer.write(s);
        return writer;
    }

}
