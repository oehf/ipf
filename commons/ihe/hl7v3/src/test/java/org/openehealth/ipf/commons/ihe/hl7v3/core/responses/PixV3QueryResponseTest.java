/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v3.core.responses;

import net.ihe.gazelle.hl7v3.datatypes.II;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Device;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PixV3QueryResponseTest {

    @Test
    public void testFromQuery() {
        final var query = new PixV3QueryRequest();
        query.getDataSourceOids().add("7.8.9");
        query.setQueryPatientId(new II("123", "1.2.3"));
        query.setQueryId(new II("abc", "4.5.6"));
        query.setReceiver(new Device());
        query.getReceiver().getIds().add(new II("receiver", "1.2"));
        query.setSender(new Device());
        query.getSender().getIds().add(new II("sender", "1.3"));
        query.setMessageId(new II("m1", "1.3.5"));

        final var response = PixV3QueryResponse.fromQuery(query);
        assertEquals(query.getDataSourceOids(), response.getDataSourceOids());
        assertEquals(query.getQueryPatientId(), response.getQueryPatientId());
        assertEquals(query.getQueryId(), response.getQueryId());
        assertEquals(query.getReceiver(), response.getSender());
        assertEquals(query.getSender(), response.getReceiver());
        assertEquals(query.getMessageId(), response.getTargetMessageId());
    }
}