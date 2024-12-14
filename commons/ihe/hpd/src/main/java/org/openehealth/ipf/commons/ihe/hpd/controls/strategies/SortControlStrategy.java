/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.controls.strategies;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import org.openehealth.ipf.commons.ihe.hpd.controls.sorting.SortControl2;

import javax.naming.ldap.BasicControl;
import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Dmytro Rud
 */
public class SortControlStrategy implements ControlStrategy {

    @Override
    public BasicControl deserializeDsml2(byte[] berBytes, boolean criticality) throws IOException {
        return new SortControl2(berBytes, criticality);
    }

    @Override
    public BasicControl deserializeJson(JsonNode node) throws IOException {
        var keys = new ArrayList<SortKey>();
        var keysNode = node.get("keys");
        var keyNodes = keysNode.elements();
        while (keyNodes.hasNext()) {
            var keyNode = keyNodes.next();
            keys.add(new SortKey(keyNode.get("attrId").textValue(), keyNode.get("ascending").booleanValue(), keyNode.get("matchingRuleId").textValue()));
        }
        return new SortControl2(node.get("critical").asBoolean(), keys.toArray(new SortKey[0]));
    }

    @Override
    public void serializeJson(BasicControl control, JsonGenerator gen) throws IOException {
        var sortControl = (SortControl2) control;
        gen.writeArrayFieldStart("keys");
        for (var sortKey : sortControl.getKeys()) {
            gen.writeStartObject();
            gen.writeStringField("attrId", sortKey.getAttributeID());
            gen.writeStringField("matchingRuleId", sortKey.getMatchingRuleID());
            gen.writeBooleanField("ascending", sortKey.isAscending());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

}
