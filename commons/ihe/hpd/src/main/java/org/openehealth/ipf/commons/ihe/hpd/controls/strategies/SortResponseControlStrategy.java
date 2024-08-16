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
package org.openehealth.ipf.commons.ihe.hpd.controls.strategies;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import org.openehealth.ipf.commons.ihe.hpd.controls.sorting.SortResponseControl2;

import javax.naming.ldap.BasicControl;
import java.io.IOException;

/**
 * @author Dmytro Rud
 */
public class SortResponseControlStrategy implements ControlStrategy {

    @Override
    public BasicControl deserializeDsml2(byte[] berBytes, boolean criticality) throws IOException {
        return new SortResponseControl2(berBytes, criticality);
    }

    @Override
    public BasicControl deserializeJson(JsonNode node) throws IOException {
        return new SortResponseControl2(node.get("resultCode").intValue(), node.get("failedAttrId").asText(), node.get("critical").booleanValue());
    }

    @Override
    public void serializeJson(BasicControl control, JsonGenerator gen) throws IOException {
        SortResponseControl2 sortResponseControl = (SortResponseControl2) control;
        gen.writeNumberField("resultCode", sortResponseControl.getResultCode());
        if (sortResponseControl.getFailedAttributeName() != null) {
            gen.writeStringField("failedAttrId", sortResponseControl.getFailedAttributeName());
        }
    }

}
