/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp;

import org.apache.camel.Processor;
import org.openehealth.ipf.gazelle.validation.profile.ItiPixPdqProfile;
import org.openehealth.ipf.gazelle.validation.profile.PixPdqTransactions;
import org.openehealth.ipf.platform.camel.hl7.HL7v2;
import org.openehealth.ipf.platform.camel.hl7.validation.ConformanceProfileValidators;

/**
 * Validating processors for MLLP-based IPF IHE components.
 *
 * @author Dmytro Rud
 */
abstract public class PixPdqCamelValidators {


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
     * Returns a validating processor for ITI-8 request messages
     * (Patient Identity Feed).
     */
    public static Processor iti8RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(PixPdqTransactions.ITI8);
    }

    /**
     * Returns a validating processor for ITI-8 response messages
     * (Patient Identity Feed).
     */
    public static Processor iti8ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_8_ACK);
    }

    /**
     * Returns a validating processor for ITI-9 request messages.
     * (Patient Identity Query).
     */
    public static Processor iti9RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_9_QBP_Q23);
    }

    /**
     * Returns a validating processor for ITI-9 response messages
     * (Patient Identity Query).
     */
    public static Processor iti9ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_9_RSP_K23);
    }

    /**
     * Returns a validating processor for ITI-10 request messages
     * (PIX Update Notification).
     */
    public static Processor iti10RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_10_ADT_A31);
    }

    /**
     * Returns a validating processor for ITI-10 response messages
     * (PIX Update Notification).
     * FIXME I think this is the wrong profile
     */
    public static Processor iti10ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_10_ACK);
    }

    /**
     * Returns a validating processor for ITI-21 request messages
     * (Patient Demographics Query).
     */
    public static Processor iti21RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_21_QBP_Q22);
    }

    /**
     * Returns a validating processor for ITI-21 response messages
     * (Patient Demographics Query).
     */
    public static Processor iti21ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_21_RSP_K22);
    }

    /**
     * Returns a validating processor for ITI-22 request messages
     * (Patient Demographics and Visit Query).
     */
    public static Processor iti22RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_22_QBP_ZV1);
    }

    /**
     * Returns a validating processor for ITI-22 response messages
     * (Patient Demographics and Visit Query).
     */
    public static Processor iti22ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_22_RSP_ZV2);
    }

    /**
     * Returns a validating processor for ITI-64 request messages
     * (XAD-PID Change Management).
     */
    public static Processor iti64RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_64_ADT_A43);
    }

    /**
     * Returns a validating processor for ITI-64 response messages
     * (XAD-PID Change Management).
     */
    public static Processor iti64ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(ItiPixPdqProfile.ITI_64_ACK_A43);
    }

}
