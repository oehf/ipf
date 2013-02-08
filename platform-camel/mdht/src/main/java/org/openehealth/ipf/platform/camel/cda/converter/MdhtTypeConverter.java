/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.cda.converter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.camel.Converter;
import org.openehealth.ipf.modules.cda.CDAR2Renderer;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;

@Converter
public class MdhtTypeConverter {

    private final static CDAR2Renderer renderer = new CDAR2Renderer();

    @Converter
    public static InputStream toInputStream(
            ClinicalDocument document) {
        return new ByteArrayInputStream(renderer.render(document,
                (Object[]) null).getBytes());
    }

}
