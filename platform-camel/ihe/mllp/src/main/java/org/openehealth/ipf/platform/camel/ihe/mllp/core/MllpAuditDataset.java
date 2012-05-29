/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;


/**
 * Generic audit dataset for IHE PIX/PDQ transactions.
 * 
 * @author Dmytro Rud
 */
abstract public class MllpAuditDataset extends AuditDataset {
    private static final long serialVersionUID = -4427222097816361541L;

    /** Sending application from MSH-3 */
    @Getter @Setter private String sendingApplication;
    
    /** Sending facility from MSH-4 */
    @Getter @Setter private String sendingFacility;

    /** Receiving application from MSH-5 */
    @Getter @Setter private String receivingApplication;

    /** Receiving facility from MSH-6 */
    @Getter @Setter private String receivingFacility;
    
    /** Message type from MSH-9 */
    @Getter @Setter private String messageType;
    
    /** Message control ID from MSH-10 */
    @Getter @Setter private String messageControlId;

    /** Local address from MINA session */
    @Getter @Setter private String localAddress;

    /** Remote address from MINA session */
    @Getter @Setter private String remoteAddress;


    public static final String[] GENERIC_NECESSARY_AUDIT_FIELDS = new String[] {
        "SendingApplication",
        "SendingFacility",
        "ReceivingApplication",
        "ReceivingFacility",
        "MessageControlId",
        "LocalAddress",
        "RemoteAddress"
    };
    
    
    /**
     * Constructor.
     * @param serverSide
     *      Where we are&nbsp;&mdash; server side
     *      ({@code true}) or client side ({@code false}).
     */
    public MllpAuditDataset(boolean serverSide) {
        super(serverSide);
    }

}
