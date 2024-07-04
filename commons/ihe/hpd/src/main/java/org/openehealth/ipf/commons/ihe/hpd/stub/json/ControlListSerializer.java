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
import org.openehealth.ipf.commons.ihe.hpd.controls.sorting.SortControl2;
import org.openehealth.ipf.commons.ihe.hpd.controls.sorting.SortResponseControl2;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.Control;

import javax.naming.ldap.*;
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
            BasicControl control = ControlUtils.extractControl((byte[]) dsmlcontrol.getControlValue(), dsmlcontrol.getType(), dsmlcontrol.isCriticality());
            gen.writeStartObject();
            gen.writeStringField("type", control.getID());
            gen.writeBooleanField("critical", control.isCritical());
            switch (dsmlcontrol.getType()) {
                case PagedResultsControl.OID:
                    PagedResultsResponseControl paginationControl = (PagedResultsResponseControl) control;
                    gen.writeNumberField("size", paginationControl.getResultSize());
                    if (paginationControl.getCookie() != null) {
                        gen.writeBinaryField("cookie", paginationControl.getCookie());
                    }
                    break;
                case SortControl.OID:
                    SortControl2 sortControl = (SortControl2) control;
                    gen.writeArrayFieldStart("keys");
                    for (SortKey sortKey : sortControl.getKeys()) {
                        gen.writeStartObject();
                        gen.writeStringField("attrId", sortKey.getAttributeID());
                        gen.writeStringField("matchingRuleId", sortKey.getMatchingRuleID());
                        gen.writeBooleanField("ascending", sortKey.isAscending());
                        gen.writeEndObject();
                    }
                    gen.writeEndArray();
                    break;
                case SortResponseControl.OID:
                    SortResponseControl2 sortResponseControl = (SortResponseControl2) control;
                    gen.writeNumberField("resultCode", sortResponseControl.getResultCode());
                    if (sortResponseControl.getFailedAttributeName() != null) {
                        gen.writeStringField("failedAttrId", sortResponseControl.getFailedAttributeName());
                    }
                    break;
                default:
                    throw new NotImplementedException("Cannot handle control type " + dsmlcontrol.getType());
            }
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

}
