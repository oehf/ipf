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
package org.openehealth.ipf.platform.camel.ihe.hl7v2;

import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration;


/**
 * Basic ACK and NAK factory for HL7v2-based transactions.
 * @author Dmytro Rud
 *
 * @deprecated moved to {@link org.openehealth.ipf.commons.ihe.hl7v2.NakFactory}
 */
public class NakFactory extends org.openehealth.ipf.commons.ihe.hl7v2.NakFactory {

    public NakFactory(org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration config,
                      boolean useCAckTypeCodes, String defaultNakMsh9) {
        super(config, useCAckTypeCodes, defaultNakMsh9);
    }

    public NakFactory(Hl7v2TransactionConfiguration config) {
        super(config);
    }
}
