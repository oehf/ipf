/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import org.apache.camel.Processor;
import org.openehealth.ipf.gazelle.validation.profile.pcd.PcdTransactions;
import org.openehealth.ipf.platform.camel.hl7.HL7v2;
import org.openehealth.ipf.platform.camel.hl7.validation.ConformanceProfileValidators;

/**
 * @author Mitko Kolev
 *
 */
public class Hl7v2WsCamelValidators {

    public static Processor pcdValidator() {
        return HL7v2.validatingProcessor();
    }

    /**
     * Returns a validating processor for PCD-01 request messages
     * (Communicate Patient Care Device (PCD) data).
     */
    public static Processor pcd01RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1);
    }

    /**
     * Returns a validating processor for PCD-01 response messages
     * (Communicate Patient Care Device (PCD) data).
     */
    public static Processor pcd01ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1);
    }

    /**
     * Returns a validating processor for Continua WAN - conform request messages.
     */
    public static Processor continuaWanRequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1);
    }

    /**
     * Returns a validating processor for Continua WAN - conform response messages.
     */
    public static Processor continuaWanResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1);
    }
    
}
