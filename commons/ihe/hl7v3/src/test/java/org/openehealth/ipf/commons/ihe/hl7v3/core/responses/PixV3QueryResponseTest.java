package org.openehealth.ipf.commons.ihe.hl7v3.core.responses;

import net.ihe.gazelle.hl7v3.datatypes.II;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.Device;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryQuery;

import static org.junit.Assert.assertEquals;

public class PixV3QueryResponseTest {

    @Test
    public void testFromQuery() {
        final PixV3QueryQuery query = new PixV3QueryQuery();
        query.getDataSourceOids().add("7.8.9");
        query.setQueryPatientId(new II("123", "1.2.3"));
        query.setQueryId(new II("abc", "4.5.6"));
        query.setReceiver(new Device());
        query.getReceiver().getIds().add(new II("receiver", "1.2"));
        query.setSender(new Device());
        query.getSender().getIds().add(new II("sender", "1.3"));
        query.setMessageId(new II("m1", "1.3.5"));

        final PixV3QueryResponse response = PixV3QueryResponse.fromQuery(query);
        assertEquals(query.getDataSourceOids(), response.getDataSourceOids());
        assertEquals(query.getQueryPatientId(), response.getQueryPatientId());
        assertEquals(query.getQueryId(), response.getQueryId());
        assertEquals(query.getReceiver(), response.getSender());
        assertEquals(query.getSender(), response.getReceiver());
        assertEquals(query.getMessageId(), response.getTargetMessageId());
    }
}