/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.stub.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.LDAPResult;

import jakarta.xml.bind.JAXBElement;
import java.io.IOException;
import java.util.List;

/**
 * JSON serializer for {@link List}&lt;{@link JAXBElement}&gt;.
 *
 * @author Dmytro Rud
 */
public class JaxbElementListSerializer extends StdSerializer<List> {

    public JaxbElementListSerializer() {
        super(List.class);
    }

    @Override
    public void serialize(List list, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartArray(list);
        for (var object : list) {
            var jaxbElement = (JAXBElement<?>) object;
            var value = jaxbElement.getValue();
            if (value instanceof LDAPResult) {
                ((LDAPResult) value).setElementName(jaxbElement.getName().getLocalPart());
            }
            var valueSerializer = provider.findValueSerializer(value.getClass());
            var typeSerializer = provider.findTypeSerializer(SimpleType.constructUnsafe(value.getClass()));
            valueSerializer.serializeWithType(value, gen, provider, typeSerializer);
        }
        gen.writeEndArray();
    }

}
