/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration;

/**
 * NAK factory for PDQ, PDVQ and PIX Query.
 *
 * @author Dmytro Rud
 * @deprecated moved to {@link org.openehealth.ipf.commons.ihe.hl7v2.QpdAwareNakFactory}
 */
public class QpdAwareNakFactory extends org.openehealth.ipf.commons.ihe.hl7v2.QpdAwareNakFactory {

    public QpdAwareNakFactory(Hl7v2TransactionConfiguration config, String messageType, String triggerEvent) {
        super(config, messageType, triggerEvent);
    }
}
