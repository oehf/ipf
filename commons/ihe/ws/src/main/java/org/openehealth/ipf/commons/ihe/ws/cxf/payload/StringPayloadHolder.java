/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.payload;

import java.util.EnumMap;

/**
 * Holder for various types of String message payloads.
 * @author Dmytro Rud
 */
public class StringPayloadHolder {

    /**
     * Types of supported payload:
     * <ul>
     *     <li>HTTP (probably multi-part, i.e. with all attachments),
     *     <li>SOAP Body.
     * </ul>
     */
    public static enum PayloadType {
        HTTP, SOAP_BODY
    }


    // not synchronized, because parallel access is not expected
    private final EnumMap<PayloadType, String> map =
            new EnumMap<PayloadType, String>(PayloadType.class);


    public String get(PayloadType payloadType) {
        return map.get(payloadType);
    }

    public void put(PayloadType payloadType, String payload) {
        map.put(payloadType, payload);
    }

    public void remove(PayloadType payloadType) {
        map.remove(payloadType);
    }
}
