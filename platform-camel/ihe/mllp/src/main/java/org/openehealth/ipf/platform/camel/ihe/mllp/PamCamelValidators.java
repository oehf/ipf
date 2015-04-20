/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp;

import org.apache.camel.Processor;
import org.openehealth.ipf.gazelle.validation.profile.pam.ItiPamProfile;
import org.openehealth.ipf.gazelle.validation.profile.pam.PamTransactions;
import org.openehealth.ipf.platform.camel.hl7.HL7v2;
import org.openehealth.ipf.platform.camel.hl7.validation.ConformanceProfileValidators;

/**
 * Validating processors for MLLP-based IPF IHE components.
 *
 * @author Christian Ohr
 */
abstract public class PamCamelValidators {


    /**
     * Returns a validating processor that obtains the validation rules from the currently
     * used {@link ca.uhn.hl7v2.HapiContext HapiContext} and the type of the message contained
     * in the exchange body. As such there is no real need to use the explicitly transaction-dependent
     * validators.
     *
     * @return all-purpose validating processor
     */
    public static Processor itiValidator() {
        return HL7v2.validatingProcessor();
    }

    /**
     * Returns a validating processor for ITI-30 request messages
     * (Patient Identity Management).
     */
    public static Processor iti30RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(PamTransactions.ITI30);
    }

    /**
     * Returns a validating processor for ITI-30 response messages
     * (Patient Identity Management).
     */
    public static Processor iti30ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPamProfile.ITI_30_ACK);
    }

    /**
     * Returns a validating processor for ITI-31 request messages
     * (Patient Encounter Management).
     */
    public static Processor iti31RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(PamTransactions.ITI31);
    }

    /**
     * Returns a validating processor for ITI-31 response messages
     * (Patient Encounter Management).
     */
    public static Processor iti31ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPamProfile.ITI_31_ACK);
    }

}
