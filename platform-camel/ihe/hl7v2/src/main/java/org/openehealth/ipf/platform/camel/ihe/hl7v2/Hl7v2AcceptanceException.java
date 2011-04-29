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
package org.openehealth.ipf.platform.camel.ihe.hl7v2;

import org.openehealth.ipf.modules.hl7.HL7v2Exception;

/**
 * An exception class for message acceptance checks in MLLP consumer and producer.
 * @author Dmytro Rud
 */
public class Hl7v2AcceptanceException extends HL7v2Exception {
    private static final long serialVersionUID = 8061724688826230547L;

    public Hl7v2AcceptanceException(String message, int code) {
        super(message, code);
    }

    public Hl7v2AcceptanceException(String message) {
        super(message);
    }
}
