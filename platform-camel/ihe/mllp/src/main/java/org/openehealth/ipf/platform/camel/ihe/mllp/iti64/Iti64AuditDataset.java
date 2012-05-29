/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti64;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditDataset;

/**
 * @author Dmytro Rud
 */
public class Iti64AuditDataset extends MllpAuditDataset {

    @Getter @Setter private String sourcePatientId;
    @Getter @Setter private String newPatientId;
    @Getter @Setter private String oldPatientId;

    public Iti64AuditDataset(boolean serverSide) {
        super(serverSide);
    }
}
