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
package org.openehealth.ipf.commons.ihe.core.atna;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

import java.io.Serializable;

/**
 * A generic data structure used to store information pieces needed for ATNA auditing.
 * 
 * @author Dmytro Rud
 */
public class AuditDataset implements Serializable {
    private static final long serialVersionUID = -2919172035448943710L;

    // whether we audit on server (true) or on client (false)
    private final boolean serverSide;

    /**
     * Event outcome code as defined in RFC 3881.
     */
    private RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcomeCode;

    /**
     * Constructor.
     * 
     * @param serverSide
     *            specifies whether this audit dataset will be used on the
     *            server side (<code>true</code>) or on the client side 
     *            (<code>false</code>)
     */
    public AuditDataset(boolean serverSide) {
        this.serverSide = serverSide;
    }

    public boolean isServerSide() {
        return serverSide;
    }

    public RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCode() {
        return eventOutcomeCode;
    }

    public void setEventOutcomeCode(RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcomeCode) {
        this.eventOutcomeCode = eventOutcomeCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
