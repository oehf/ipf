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

import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import org.openehealth.ipf.commons.core.DomBuildersThreadLocal;
import org.springframework.core.convert.converter.Converter;
import org.w3c.dom.Document;

/**
 * @author Stefan Ivanov
 */
public class ByteArrayToDomConverter implements Converter<byte[], Document> {

    private static final DomBuildersThreadLocal DOM_BUILDERS = new DomBuildersThreadLocal();

    @Override
    public Document convert(byte[] source) {
        try {
            DocumentBuilder builder = DOM_BUILDERS.get();
            return builder.parse(new ByteArrayInputStream(source));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
