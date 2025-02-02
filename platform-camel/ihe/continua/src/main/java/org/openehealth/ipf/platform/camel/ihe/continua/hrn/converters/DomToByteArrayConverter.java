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

import lombok.NonNull;
import org.openehealth.ipf.commons.xml.XmlUtils;
import org.springframework.core.convert.converter.Converter;
import org.w3c.dom.Document;

/**
 * @author Dmytro Rud
 */
public class DomToByteArrayConverter implements Converter<Document, byte[]> {

    @Override
    public byte[] convert(@NonNull Document document) {
        try {
            return XmlUtils.serialize(document);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
