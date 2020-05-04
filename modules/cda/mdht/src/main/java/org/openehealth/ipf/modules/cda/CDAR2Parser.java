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

import org.openehealth.ipf.commons.core.modules.api.ParseException;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author Stefan Ivanov
 */
public class CDAR2Parser implements Parser<ClinicalDocument> {


    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(java.lang.String, java.lang.Object[])
     */
    public ClinicalDocument parse(String s, Object... options) {
        return parse(new ByteArrayInputStream(s.getBytes()), options);
    }

    /**
     * Parses a {@link ClinicalDocument} from an {@link InputStream}. This parser is not vulnerable to XXE injections.
     * Parsing an XML document with a Doctype will throw a {@link ParseException} and all
     * &lt;include xmlns="http://www.w3.org/2001/XInclude"/gt; tags will be striped.
     *
     * <p>The MDHT parser is unsafe, so the clinical document is parsed to a {@link org.w3c.dom.Document} here
     * before being passed to MDHT.
     *
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(java.io.InputStream, java.lang.Object[])
     */
    public ClinicalDocument parse(InputStream is, Object... options) {
        try {
            final var documentBuilder = this.newSafeDocumentBuilder();
            final var document = documentBuilder.parse(is);

            var nodeList = document.getElementsByTagNameNS("http://www.w3.org/2001/XInclude", "include");
            for (var i = nodeList.getLength() - 1; i >= 0; --i) {
                if (nodeList.item(i) != null && nodeList.item(i).getParentNode() != null) {
                    nodeList.item(i).getParentNode().removeChild(nodeList.item(i));
                }
            }

            return CDAUtil.load(document);

        } catch (final Exception exception) {
            throw new ParseException(exception);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(javax.xml.transform.Source, java.lang.Object[])
     */
    public ClinicalDocument parse(Source source, Object... options) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /*
     * (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(java.io.Reader, java.lang.Object[])
     */
    public ClinicalDocument parse(Reader reader, Object... options) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Initiliazes and configures a {@link DocumentBuilder} that is not vulnerable to XXE injections (XInclude,
     * Billions Laugh Attack, ...).
     *
     * @return A configured DocumentBuilder.
     * @throws ParserConfigurationException If the parser is not Xerces2 compatible.
     */
    private DocumentBuilder newSafeDocumentBuilder() throws ParserConfigurationException {
        final var factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://apache.org/xml/features/xinclude", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        return factory.newDocumentBuilder();
    }

}
