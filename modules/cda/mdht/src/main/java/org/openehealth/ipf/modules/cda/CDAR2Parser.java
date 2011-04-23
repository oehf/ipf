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

import org.eclipse.emf.common.util.URI;
import org.openehealth.ipf.commons.core.modules.api.ParseException;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openhealthtools.mdht.uml.cda.CDAPackage;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.DocumentRoot;
import org.openhealthtools.mdht.uml.cda.ccd.CCDPackage;
import org.openhealthtools.mdht.uml.cda.internal.resource.CDAResource;

/**
 * @author Stefan Ivanov
 */
public class CDAR2Parser implements Parser<ClinicalDocument> {

    static {
        @SuppressWarnings("unused")
        CDAPackage cdaPackageInstance = CDAPackage.eINSTANCE;
        @SuppressWarnings("unused")
        CCDPackage ccdPackageInstance = CCDPackage.eINSTANCE;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(java.lang.String, java.lang.Object[])
     */
    public ClinicalDocument parse(String s, Object... options) {
        try {
            return parse(new ByteArrayInputStream(s.getBytes()), options);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(java.io.InputStream, java.lang.Object[])
     */
    public ClinicalDocument parse(InputStream is, Object... options)
            throws IOException {
        CDAResource resource = (CDAResource) CDAResource.Factory.INSTANCE.createResource(URI
            .createURI(CDAPackage.eNS_URI));
        if (options != null && options.length > 0 && options[0] instanceof Map<?, ?>) {
            resource.load(is, (Map<?, ?>) options[0]);
        } else {
            resource.load(is, null);
        }
        DocumentRoot root = (DocumentRoot) resource.getContents().get(0);
        return root.getClinicalDocument();
    }

    /*
     * (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(javax.xml.transform.Source, java.lang.Object[])
     */
    public ClinicalDocument parse(Source source, Object... options) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /*
     * (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.modules.api.Parser#parse(java.io.Reader, java.lang.Object[])
     */
    public ClinicalDocument parse(Reader reader, Object... options) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
