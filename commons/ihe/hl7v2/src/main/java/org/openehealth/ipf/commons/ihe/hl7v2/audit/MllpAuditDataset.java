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
package org.openehealth.ipf.commons.ihe.hl7v2.audit;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.util.Collections;
import java.util.List;


/**
 * Generic audit dataset for MLLP-based IHE transactions.
 *
 * @author Dmytro Rud
 */
public abstract class MllpAuditDataset extends AuditDataset {

    private static final long serialVersionUID = -4427222097816361541L;

    /**
     * Sending application from MSH-3
     */
    @Getter @Setter
    private String sendingApplication;

    /**
     * Sending facility from MSH-4
     */
    @Getter @Setter
    private String sendingFacility;

    /**
     * Receiving application from MSH-5
     */
    @Getter @Setter
    private String receivingApplication;

    /**
     * Receiving facility from MSH-6
     */
    @Getter @Setter
    private String receivingFacility;

    /**
     * Message type from MSH-9
     */
    @Getter @Setter
    private String messageType;

    /**
     * Message control ID from MSH-10
     */
    @Getter @Setter
    private String messageControlId;

    /**
     * Local address
     */
    @Setter
    private String localAddress;

    /**
     * Remote address
     */
    @Setter @Getter
    private String remoteAddress;


    /**
     * Constructor.
     *
     * @param serverSide Where we are&nbsp;&mdash; server side
     *                   ({@code true}) or client side ({@code false}).
     */
    public MllpAuditDataset(boolean serverSide) {
        super(serverSide);
    }


    /**
     * The identity of the Source Actor facility and sending application
     * from the HL7 message; concatenated together, separated by the | character.
     *
     * @return identity of the Source Actor facility
     */
    @Override
    public String getSourceUserId() {
        return String.format("%s|%s", sendingFacility, sendingApplication);
    }

    /**
     * The identity of the Destination Actor facility and receiving application
     *
     * from the HL7 message; concatenated together, separated by the | character
     * @return identity of the Destination Actor facility
     */
    @Override
    public String getDestinationUserId() {
        return String.format("%s|%s", receivingFacility, receivingApplication);
    }

    /**
     * @return The machine name or IP address
     */
    @Override
    public String getLocalAddress() {
        return localAddress != null ? localAddress : AuditUtils.getLocalIPAddress();
    }

    @Override
    public List<HumanUser> getHumanUsers() {
        return Collections.emptyList();
    }
}
