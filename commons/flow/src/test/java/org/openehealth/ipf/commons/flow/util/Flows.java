/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.flow.util;

import static org.openehealth.ipf.commons.flow.domain.FlowStatus.CLEAN;

import java.io.IOException;
import java.util.Date;

import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowPart;

/**
 * @author Martin Krasser
 */
public class Flows {

    public static Flow createFlow(String packet) throws IOException {
        return createFlow(packet.getBytes(), true);
    }

    public static Flow createFlow(byte[] packet) throws IOException {
        return createFlow(packet, true);
    }

    public static Flow createFlowWithParts(String packet, int numberOfParts) {
        Flow flow = new Flow();
        flow.setApplication("test");
        flow.setCreationTime(new Date());
        flow.setPacket(packet.getBytes());
        for (int t = 0; t < numberOfParts; t++) {
            flow.getParts().add(createFlowPart("0." + String.valueOf(t)));
        }
        return flow;

    }

    public static Flow createFlow(byte[] packet, boolean createParts) throws IOException {
        Flow flow = new Flow();
        flow.setApplication("test");
        flow.setCreationTime(new Date());
        flow.setPacket(packet);
        if (createParts) {
            flow.getParts().add(createFlowPart("0.0"));
            flow.getParts().add(createFlowPart("0.1"));
        }
        return flow;
    }

    public static FlowPart createFlowPart(String path) {
        FlowPart flowPart = new FlowPart();
        flowPart.setContributionTime(new Date());
        flowPart.setStatus(CLEAN);
        flowPart.setPath(path);
        return flowPart;
    }

    /**
     * Compares all fields of flow1 with all fields of flow 2 for equality
     * 
     * @param flow1
     * @param flow2
     * @return
     */
    public static boolean deepEquals(Flow flow1, Flow flow2) {
        int fieldsCount = Flow.class.getDeclaredFields().length;

        int checkedFields = 0;
        if (flow1 == flow2)
            return true;
        if (flow1 == null) {
            if (flow2 != null)
                return false;
        } else if (flow1 != null && flow2 == null)
            return false;

        if (!(flow1.getAckCountExpected() == flow2.getAckCountExpected()))
            return false;
        checkedFields++;

        if (flow1.getApplication() == null) {
            if (flow2.getApplication() != null)
                return false;
        } else if (!flow1.getApplication().equals(flow2.getApplication()))
            return false;
        checkedFields++;

        if (flow1.getCreationTime() == null) {
            if (flow2.getCreationTime() != null)
                return false;
        } else if (flow1.getCreationTime().equals(flow2.getCreationTime()))
            return false;
        checkedFields++;

        if (flow1.getIdentifier() == null) {
            if (flow2.getIdentifier() != null)
                return false;
        } else if (!flow1.getIdentifier().equals(flow2.getIdentifier()))
            return false;
        checkedFields++;

        // check if the message is ok.
        byte[] flow1Packet = flow1.getPacket();
        byte[] flow2Packet = flow2.getPacket();
        if (flow1Packet == null) {
            if (flow2Packet != null)
                return false;
        } else {
            if (flow1Packet.length != flow2Packet.length) {
                return false;
            }
            for (int t = 0; t < flow1Packet.length; t++) {
                if (flow1Packet[t] != flow2Packet[t])
                    return false;
            }
        }
        checkedFields++;
        if (flow1.getParts() == null) {
            if (flow2.getParts() != null)
                return false;
            // perfomres deep equals
        } else if (!flow1.getParts().equals(flow2.getParts()))
            return false;
        checkedFields++;

        if (flow1.getReplayCount() != flow2.getReplayCount())
            return false;
        checkedFields++;

        if (flow1.getReplayTime() == null) {
            if (flow2.getReplayTime() != null)
                return false;
        } else if (!flow1.getReplayTime().equals(flow2.getReplayTime()))
            return false;
        checkedFields++;

        if (checkedFields != fieldsCount)
            throw new RuntimeException(
                    "New defined fields in the Flow class have to be checked here!");

        return true;
    }
}
