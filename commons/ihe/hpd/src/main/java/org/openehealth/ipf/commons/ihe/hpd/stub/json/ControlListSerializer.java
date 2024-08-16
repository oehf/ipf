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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.NotImplementedException;
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils;
import org.openehealth.ipf.commons.ihe.hpd.controls.strategies.ControlStrategy;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.Control;

import javax.naming.ldap.BasicControl;
import java.io.IOException;
import java.util.List;

/**
 * JSON serializer for {@link List}&lt;{@link Control}&gt;.
 *
 * @author Dmytro Rud
 */
public class ControlListSerializer extends StdSerializer<List> {

    public ControlListSerializer() {
        super(List.class);
    }

    @Override
    public void serialize(List list, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartArray(list);
        for (Object object : list) {
            Control dsmlcontrol = (Control) object;
            ControlStrategy strategy = ControlUtils.getStrategies().get(dsmlcontrol.getType());
            if (strategy != null) {
                BasicControl control = strategy.deserializeDsml2((byte[]) dsmlcontrol.getControlValue(), dsmlcontrol.isCriticality());
                gen.writeStartObject();
                gen.writeStringField("type", control.getID());
                gen.writeBooleanField("critical", control.isCritical());
                strategy.serializeJson(control, gen);
                gen.writeEndObject();
            } else {
                throw new NotImplementedException("Cannot handle control type " + dsmlcontrol.getType());
            }
        }
        gen.writeEndArray();
    }

}
