/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2;

import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.HL7Exception;

/**
 * An exception class for HL7v2 acceptance checks.
 *
 * @author Dmytro Rud
 */
public class Hl7v2AcceptanceException extends HL7Exception {
    private static final long serialVersionUID = -954901285020747868L;

    public Hl7v2AcceptanceException(String message, ErrorCode code) {
        super(message, code);
    }

    public Hl7v2AcceptanceException(String message) {
        super(message);
    }
}
