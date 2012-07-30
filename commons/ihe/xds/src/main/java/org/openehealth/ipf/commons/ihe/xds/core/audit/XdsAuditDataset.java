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
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import java.util.ArrayList;
import java.util.List;

/**
 * A data structure that contains XDS-specific ATNA audit information pieces
 * in addition to common IHE Web Service-related ones.
 * @author Dmytro Rud
 */
abstract public class XdsAuditDataset extends WsAuditDataset {
    private static final long serialVersionUID = 652866992858926778L;

    // patient ID as HL7 CX datatype, e.g. "1234^^^&1.2.3.4&ISO"
    @Getter private final List<String> patientIds = new ArrayList<String>();

    /**
     * Constructor.
     * 
     * @param serverSide
     *            specifies whether this audit dataset will be used on the
     *            server side (<code>true</code>) or on the client side (
     *            <code>false</code>)
     */
    public XdsAuditDataset(boolean serverSide) {
        super(serverSide);
    }

    /**
     * @return the first present patient ID as HL7 CX string, e.g. "1234^^^&1.2.3.4&ISO",
     *      or <code>null</code> when no patient IDs have been collected.
     */
    public String getPatientId() {
        return patientIds.isEmpty() ? null : patientIds.get(0);
    }
}
