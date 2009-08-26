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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import javax.xml.transform.Source;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.openehealth.ipf.commons.core.modules.api.ParseException;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openhealthtools.ihe.common.cdar2.CDAR2Package;
import org.openhealthtools.ihe.common.cdar2.DocumentRoot;
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ClinicalDocument;
import org.openhealthtools.ihe.common.cdar2.util.CDAR2ResourceFactoryImpl;

/**
 * @author Christian Ohr
 */
public class CDAR2Parser implements Parser<POCDMT000040ClinicalDocument> {

    static {
        @SuppressWarnings("unused")
        CDAR2Package packageInstance = CDAR2Package.eINSTANCE;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(java.lang.String, java.lang.Object[])
     */
    public POCDMT000040ClinicalDocument parse(String s, Object... options) {
        try {
            return parse(new ByteArrayInputStream(s.getBytes()), options);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(java.io.InputStream, java.lang.Object[])
     */
    public POCDMT000040ClinicalDocument parse(InputStream is, Object... options)
            throws IOException {
        Resource resources = new CDAR2ResourceFactoryImpl().createResource(URI.createURI(CDAR2Package.eNS_URI));
        if (options != null && options.length > 0 && options[0] instanceof Map<?, ?>) {
            resources.load(is, (Map<?,?>)options[0]);
        } else {
            resources.load(is, null);
        }
        EList<EObject> list = resources.getContents();
        DocumentRoot root = (DocumentRoot)list.get(0);
        return root.getClinicalDocument();
    }

    public POCDMT000040ClinicalDocument parse(Source source, Object... options) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public POCDMT000040ClinicalDocument parse(Reader reader, Object... options) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
