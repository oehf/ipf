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
import org.openehealth.ipf.commons.flow.domain.FlowStatus;

/**
 * @author Martin Krasser
 */
public class Flows {

    private static final char NEWLINE = '\n';
    
    public static Flow createFlow(String packet) throws IOException {
        return createFlow(packet.getBytes(), true);
    }

    public static Flow createFlow(byte[] packet) throws IOException {
        return createFlow(packet, true);
    }

    public static Flow createFlow(byte[] packet, boolean createParts) {
        return createFlow(packet, createParts ? 2 : 0);
    }

    public static Flow createFlow(String packet, int numParts) {
        return createFlow(packet.getBytes(), numParts);
    }

    public static Flow createFlow(byte[] packet, int numParts) {
        Flow flow = new Flow();
        flow.setApplication("test");
        flow.setCreationTime(new Date());
        flow.setPacket(packet);
        for (int i = 0; i < numParts; i++) {
            flow.getParts().add(createFlowPart(i, CLEAN));
        }
        return flow;
    }

    public static Flow createFlowWithText(String uniquePart, FlowStatus... status) throws Exception {
        String text = createMessageText(uniquePart);
        Flow flow = createFlow(text.getBytes(), false);
        flow.setFlowMessageText(text);
        if (status != null) {
            for (int i = 0; i < status.length; i++) {
                FlowPart part = createFlowPart(i, status[i]);
                part.setFlowPartMessageText(text);
                flow.getParts().add(part);
    
            }
            flow.setAckCountExpected(status.length);
        }
        return flow;
    }

    public static FlowPart createFlowPart(String path) {
        return createFlowPart(path, CLEAN);
    }

    public static FlowPart createFlowPart(int index, FlowStatus status) {
        return createFlowPart("0." + index, status);
    }
    
    public static FlowPart createFlowPart(String path, FlowStatus status) {
        FlowPart flowPart = new FlowPart();
        flowPart.setContributionTime(new Date());
        flowPart.setStatus(status);
        flowPart.setPath(path);
        return flowPart;
    }

    public static String createMessageText(String searchKey) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Created: ").append(new Date()).append(NEWLINE);
        buffer.append("Content: ").append(searchKey).append(NEWLINE);
        return buffer.toString();

    }
    
}
