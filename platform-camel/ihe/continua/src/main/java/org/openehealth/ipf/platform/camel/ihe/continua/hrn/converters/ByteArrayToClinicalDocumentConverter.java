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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters;

import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.springframework.core.convert.converter.Converter;

import java.io.ByteArrayInputStream;

/**
 * Special document content converter for Continua HRN request.
 * Retrieves the CDA/CCD content from the Document and parses it with
 * the MDHT CDAR2 parser.
 * 
 * @author Stefan Ivanov
 */
public class ByteArrayToClinicalDocumentConverter implements Converter<byte[], ClinicalDocument> {

    @Override
    public ClinicalDocument convert(byte[] source) {
        try {
            return CDAUtil.load(new ByteArrayInputStream(source));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}

