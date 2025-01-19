/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs.core.audit;

import lombok.*;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import java.io.Serial;

/**
 * The audit dataset for SVS transactions.
 *
 * @author Quentin Ligier
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SvsAuditDataset extends WsAuditDataset {

    @Serial
    private static final long serialVersionUID = -8950614248765744542L;

    /**
     * The retrieved value set ID (OID).
     */
    private String valueSetId;

    /**
     * The retrieved value set display name, if any.
     */
    private String valueSetName;

    /**
     * The retrieved value set version, if any.
     */
    private String valueSetVersion;

    public SvsAuditDataset(boolean serverSide) {
        super(serverSide);
        this.setSourceUserIsRequestor(false);
    }
}