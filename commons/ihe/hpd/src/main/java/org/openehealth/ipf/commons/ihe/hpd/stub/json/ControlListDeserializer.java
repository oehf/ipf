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

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.NotImplementedException;
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils;
import org.openehealth.ipf.commons.ihe.hpd.controls.sorting.SortControl2;
import org.openehealth.ipf.commons.ihe.hpd.controls.sorting.SortResponseControl2;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.Control;

import javax.naming.ldap.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * JSON deserializer for {@link List}&lt;{@link Control}&gt;.
 *
 * @author Dmytro Rud
 */
public class ControlListDeserializer extends JsonDeserializer<List> {

    @Override
    public List deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode arrayNode = jsonParser.readValueAsTree();
        if (!arrayNode.isArray()) {
            throw new IllegalArgumentException("'controls' shall be a JSON array");
        }
        List<Control> result = new ArrayList<>();
        Iterator<JsonNode> controlNodes = arrayNode.elements();
        while (controlNodes.hasNext()) {
            result.add(ControlUtils.toDsmlv2(deserializeControl(controlNodes.next())));
        }
        return result;
    }

    private static BasicControl deserializeControl(JsonNode node) throws IOException {
        switch (node.get("type").textValue()) {
            case PagedResultsControl.OID:
                return new PagedResultsControl(node.get("size").intValue(), node.get("cookie").binaryValue(), node.get("critical").booleanValue());
            case SortControl.OID:
                ArrayList<SortKey> keys = new ArrayList<>();
                JsonNode keysNode = node.get("keys");
                Iterator<JsonNode> keyNodes = keysNode.elements();
                while (keyNodes.hasNext()) {
                    JsonNode keyNode = keyNodes.next();
                    keys.add(new SortKey(keyNode.get("attrId").textValue(), keyNode.get("ascending").booleanValue(), keyNode.get("matchingRuleId").textValue()));
                }
                return new SortControl2(node.get("critical").asBoolean(), keys.toArray(new SortKey[0]));
            case SortResponseControl.OID:
                return new SortResponseControl2(node.get("resultCode").intValue(), node.get("failedAttrId").asText(), node.get("critical").booleanValue());
            default:
                throw new NotImplementedException("Cannot handle control type " + node.get("type").asText());
        }
    }

}
