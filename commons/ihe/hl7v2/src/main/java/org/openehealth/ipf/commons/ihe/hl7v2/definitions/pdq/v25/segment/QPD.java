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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v25.datatype.*;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;


/**
 * <p>Represents an HL7 QPD message segment used in IHE PDQ.
 * The fields contained in this segment:</p>
 * <ul>
 * <li>QPD-1: Message Query Name (CE) </li>
 * <li>QPD-2: Query Tag (ST)</li>
 * <li>QPD-3: Query Input Parameter List (QIP)</li>
 * <li>QPD-4: Parameters for Fuzzy Search (NM)</li>
 * <li>QPD-5: Algorithm Name</li>
 * <li>QPD-6. Algorithm Version</li>
 * <li>QPD-7: Algorithm Description</li>
 * <li>QPD-8: What Domains Returned (CX)</li>
 * </ul>
 */
@SuppressWarnings("serial")
public class QPD extends AbstractSegment {

    /**
     * Creates a QPD (Query Parameter Definition) segment object
     * which belongs to the given message
     */
    public QPD(Group parentGroup, ModelClassFactory modelFactory) {
        super(parentGroup, modelFactory);
        var msg = getMessage();
        try {
            add(CE.class, true, 1, 250, new Object[]{msg}, "Message Query Name");
            add(ST.class, true, 1, 32, new Object[]{msg}, "Query Tag");
            add(QIP.class, true, 0, 256, new Object[]{msg}, "Demographics Fields");
            add(NM.class, false, 1, 16, new Object[]{msg}, "Search Confidence Threshold");
            add(ST.class, false, 1, 199, new Object[]{msg}, "Algorithm Name");
            add(ST.class, false, 1, 199, new Object[]{msg}, "Algorithm Version");
            add(ST.class, false, 1, 199, new Object[]{msg}, "Algorithm Description");
            add(CX.class, false, 0, 256, new Object[]{msg}, "What domains returned");
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }
    }

    /**
     * Returns Message Query Name (QPD-1).
     */
    public CE getMessageQueryName() {
        return getTypedField(1, 0);
    }

    /**
     * Returns Message Query Name (QPD-1).
     */
    public CE getQpd1_MessageQueryName() {
        return getTypedField(1, 0);
    }

    /**
     * Returns Query Tag (QPD-2).
     */
    public ST getQueryTag() {
        return getTypedField(2, 0);
    }

    /**
     * Returns Query Tag (QPD-2).
     */
    public ST getQpd2_QueryTag() {
        return getTypedField(2, 0);
    }

    /**
     * Returns a specific repetition of Demographics Fields (QPD-3).
     */
    public QIP getDemographicsFields(int rep) {
        return getTypedField(3, rep);
    }

    /**
     * Returns a specific repetition of Demographics Fields (QPD-3).
     */
    public QIP getQpd3_DemographicsFields(int rep) {
        return getTypedField(3, rep);
    }

    /**
     * Returns Demographics Fields (QPD-3).
     */
    public QIP[] getDemographicsFields() {
        return getTypedField(3, new QIP[0]);
    }

    /**
     * Returns Demographics Fields (QPD-3).
     */
    public QIP[] getQpd3_DemographicsFields() {
        return getTypedField(3, new QIP[0]);
    }

    /**
     * Returns count of Demographics Fields (QPD-3).
     */
    public int getDemographicsFieldsReps() {
        return getReps(3);
    }

    /**
     * Returns count of Demographics Fields (QPD-3).
     */
    public int getQpd3_DemographicsFieldsReps() {
        return getReps(3);
    }

    /**
     * Inserts a repetition of Demographics Fields (QPD-3).
     */
    public QIP insertDemographicsFieldsReps(int rep) throws HL7Exception {
        return (QIP) super.insertRepetition(3, rep);
    }

    /**
     * Inserts a repetition of Demographics Fields (QPD-3).
     */
    public QIP insertQpd3_DemographicsFieldsReps(int rep) throws HL7Exception {
        return (QIP) super.insertRepetition(3, rep);
    }

    /**
     * Removes a repetition of Demographics Fields (QPD-3).
     */
    public QIP removeDemographicsFieldsReps(int rep) throws HL7Exception {
        return (QIP) super.removeRepetition(3, rep);
    }

    /**
     * Removes a repetition of Demographics Fields (QPD-3).
     */
    public QIP removeQpd3_DemographicsFieldsReps(int rep) throws HL7Exception {
        return (QIP) super.removeRepetition(3, rep);
    }

    /**
     * Returns Search Confidence Threshold (QPD-4).
     */
    public NM getSearchConfidenceThreshold() {
        return getTypedField(4, 0);
    }

    /**
     * Returns Search Confidence Threshold (QPD-4).
     */
    public NM getQpd4_SearchConfidenceThreshold() {
        return getTypedField(4, 0);
    }

    /**
     * Returns Algorithm Name (QPD-5).
     */
    public ST getAlgorithmName() {
        return getTypedField(5, 0);
    }

    /**
     * Returns Algorithm Name (QPD-5).
     */
    public ST getQpd5_AlgorithmName() {
        return getTypedField(5, 0);
    }

    /**
     * Returns Algorithm Version (QPD-6).
     */
    public ST getAlgorithmVersion() {
        return getTypedField(6, 0);
    }

    /**
     * Returns Algorithm Version (QPD-6).
     */
    public ST getQpd6_AlgorithmVersion() {
        return getTypedField(6, 0);
    }

    /**
     * Returns Algorithm Description (QPD-7).
     */
    public ST AlgorithmDescription() {
        return getTypedField(7, 0);
    }

    /**
     * Returns Algorithm Description (QPD-7).
     */
    public ST getQpd7_AlgorithmDescription() {
        return getTypedField(7, 0);
    }


    /**
     * Returns a specific repetition of What Domains to be returned (QPD-8).
     */
    public CX getWhatDomainsReturned(int rep) {
        return getTypedField(8, rep);
    }

    /**
     * Returns a specific repetition of What Domains to be returned (QPD-8).
     */
    public CX getQpd8_WhatDomainsReturned(int rep) {
        return getTypedField(8, rep);
    }

    /**
     * Returns What Domains to be returned (QPD-8).
     */
    public CX[] getWhatDomainsReturned() {
        return getTypedField(8, new CX[0]);
    }

    /**
     * Returns What Domains to be returned (QPD-8).
     */
    public CX[] getQpd8_WhatDomainsReturned() {
        return getTypedField(8, new CX[0]);
    }

    /**
     * Returns count of What Domains to be returned (QPD-8).
     */
    public int getWhatDomainsReturnedReps() {
        return getReps(8);
    }

    /**
     * Returns count of What Domains to be returned (QPD-8).
     */
    public int getQpd8_WhatDomainsReturnedReps() {
        return getReps(8);
    }

    /**
     * Inserts a repetition of What Domains to be returned (QPD-8).
     */
    public CX insertWhatDomainsReturnedReps(int rep) throws HL7Exception {
        return (CX) super.insertRepetition(8, rep);
    }

    /**
     * Inserts a repetition of What Domains to be returned (QPD-8).
     */
    public CX insertQpd8_WhatDomainsReturnedReps(int rep) throws HL7Exception {
        return (CX) super.insertRepetition(8, rep);
    }

    /**
     * Removes a repetition of What Domains to be returned (QPD-8).
     */
    public CX removeWhatDomainsReturnedReps(int rep) throws HL7Exception {
        return (CX) super.removeRepetition(8, rep);
    }

    /**
     * Removes a repetition of What Domains to be returned (QPD-8).
     */
    public CX removeQpd8_WhatDomainsReturnedReps(int rep) throws HL7Exception {
        return (CX) super.removeRepetition(8, rep);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected Type createNewTypeWithoutReflection(int field) {
        switch (field) {
            case 0:
                return new CE(getMessage());
            case 1:
            case 4:
            case 5:
            case 6:
                return new ST(getMessage());
            case 2:
                return new QIP(getMessage());
            case 3:
                return new NM(getMessage());
            case 7:
                return new CX(getMessage());
            default:
                return null;
        }
    }

}