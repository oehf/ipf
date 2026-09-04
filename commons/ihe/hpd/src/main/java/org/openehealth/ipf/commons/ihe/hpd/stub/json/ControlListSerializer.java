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
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.io.UncheckedIOException;
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
    public void serialize(List list, JsonGenerator gen, SerializationContext provider) {
        gen.writeStartArray(list);
        for (var object : list) {
            var dsmlcontrol = (Control) object;
            var strategy = ControlUtils.getStrategies().get(dsmlcontrol.getType());
            if (strategy != null) {
                try {
                    var control = strategy.deserializeDsml2((byte[]) dsmlcontrol.getControlValue(), dsmlcontrol.isCriticality());
                    gen.writeStartObject();
                    gen.writeStringProperty("type", control.getID());
                    gen.writeBooleanProperty("critical", control.isCritical());
                    strategy.serializeJson(control, gen);
                    gen.writeEndObject();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            } else {
                throw new NotImplementedException("Cannot handle control type " + dsmlcontrol.getType());
            }
        }
        gen.writeEndArray();
    }

}
