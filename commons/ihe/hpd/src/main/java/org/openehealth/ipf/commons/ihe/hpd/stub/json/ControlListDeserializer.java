/*
 * Copyright 2024 the original author or authors.
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

import org.apache.commons.lang3.NotImplementedException;
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.Control;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import javax.naming.ldap.BasicControl;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON deserializer for {@link List}&lt;{@link Control}&gt;.
 *
 * @author Dmytro Rud
 */
public class ControlListDeserializer extends ValueDeserializer<List> {

    @Override
    public List deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        JsonNode arrayNode = jsonParser.readValueAsTree();
        if (!arrayNode.isArray()) {
            throw new IllegalArgumentException("'controls' shall be a JSON array");
        }
        List<Control> result = new ArrayList<>();
        try {
            for (var controlNode : arrayNode.values()) {
                result.add(ControlUtils.toDsmlv2(deserializeControl(controlNode)));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    private static BasicControl deserializeControl(JsonNode node) throws IOException {
        var strategy = ControlUtils.getStrategies().get(node.get("type").textValue());
        if (strategy != null) {
            return strategy.deserializeJson(node);
        } else {
            throw new NotImplementedException("Cannot handle control type " + node.get("type").asText());
        }
    }

}
