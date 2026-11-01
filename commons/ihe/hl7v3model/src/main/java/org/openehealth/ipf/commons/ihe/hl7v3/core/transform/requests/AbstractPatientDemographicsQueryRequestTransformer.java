/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests;

import net.ihe.gazelle.hl7v3.datatypes.CD;
import net.ihe.gazelle.hl7v3.datatypes.CE;
import net.ihe.gazelle.hl7v3.datatypes.CS;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.INT;
import net.ihe.gazelle.hl7v3.datatypes.IVLTS;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02LivingSubjectBirthTime;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02LivingSubjectId;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02LivingSubjectName;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchAlgorithm;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchCriterionList;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MinimumDegreeMatch;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02ParameterList;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02PatientAddress;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02QueryByParameter;
import net.ihe.gazelle.hl7v3.voc.ActClassControlAct;
import net.ihe.gazelle.hl7v3.voc.XActMoodIntentEvent;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientDemographicsQueryRequest;

import java.util.List;

import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.*;

/**
 * Base transformer between the simplified patient demographics query models and {@link PRPAIN201305UV02Type}.
 * Subclasses handle the transaction-specific parts (PDQv3 ITI-47, XCPD ITI-55).
 * <p>
 * The mapping of the {@code queryByParameter} element is public, because it is reused by the response transformers
 * (the query parameters are echoed in PRPA_IN201306UV02).
 *
 * @param <T> type of the simplified query model
 * @author Christian Ohr
 * @since 6.1
 */
public abstract class AbstractPatientDemographicsQueryRequestTransformer<T extends PatientDemographicsQueryRequest> {

    /**
     * Transforms a simplified query into the full PRPA model.
     *
     * @param simpleQuery The simplified query to transform.
     * @return the PRPA model query or {@code null} if the input was {@code null}.
     */
    public PRPAIN201305UV02Type toPrpa(final T simpleQuery) {
        if (simpleQuery == null) {
            return null;
        }

        final var query = new PRPAIN201305UV02Type();
        query.setITSVersion("XML_1.0");
        query.setId(simpleQuery.getMessageId());
        query.setCreationTime(toTs(simpleQuery.getCreationTime()));
        query.setInteractionId(new II("2.16.840.1.113883.1.6", "PRPA_IN201305UV02"));
        query.setProcessingCode(new CS("P", null, null));
        query.setProcessingModeCode(new CS("T", null, null));
        query.setAcceptAckCode(new CS("AL", null, null));
        if (simpleQuery.getReceiver() != null) {
            query.setReceiver(List.of(toMcciMt000100Receiver(simpleQuery.getReceiver())));
        }
        query.setSender(toMcciMt000100Sender(simpleQuery.getSender()));

        final var controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(new CD("PRPA_TE201305UV02", null, "2.16.840.1.113883.1.6"));
        controlActProcess.setQueryByParameter(toQueryByParameter(simpleQuery));
        query.setControlActProcess(controlActProcess);

        toPrpaSpecific(simpleQuery, query);
        return query;
    }

    /**
     * Transforms a full PRPA query into the simplified model. Data loss can occur, as the simplified model is not
     * complete.
     *
     * @param query The full PRPA query to transform.
     * @return the simplified query or {@code null} if the input was {@code null}.
     */
    public T fromPrpa(final PRPAIN201305UV02Type query) {
        if (query == null) {
            return null;
        }

        final var simpleQuery = newSimpleQuery();
        simpleQuery.setMessageId(query.getId());
        simpleQuery.setCreationTime(fromTs(query.getCreationTime()));
        simpleQuery.setSender(fromMcciMt000100Sender(query.getSender()));
        if (!query.getReceiver().isEmpty()) {
            simpleQuery.setReceiver(fromMcciMt000100Receiver(query.getReceiver().get(0)));
        }
        if (query.getControlActProcess() != null) {
            fromQueryByParameter(query.getControlActProcess().getQueryByParameter(), simpleQuery);
        }
        fromPrpaSpecific(query, simpleQuery);
        return simpleQuery;
    }

    /**
     * Creates the {@code queryByParameter} element from the query ID and query parameters of a simplified query.
     *
     * @param simpleQuery the simplified query (or response, which extends the query).
     * @return the {@code queryByParameter} element.
     */
    public PRPAMT201306UV02QueryByParameter toQueryByParameter(final T simpleQuery) {
        final var queryByParameter = new PRPAMT201306UV02QueryByParameter();
        queryByParameter.setQueryId(simpleQuery.getQueryId());
        queryByParameter.setStatusCode(new CS("new", null, null));
        queryByParameter.setResponseModalityCode(new CS("R", null, null));
        queryByParameter.setResponsePriorityCode(new CS("I", null, null));

        if (simpleQuery.getMinimumDegreeMatch() != null || simpleQuery.getMatchAlgorithm() != null) {
            final var matchCriterionList = new PRPAMT201306UV02MatchCriterionList();
            if (simpleQuery.getMatchAlgorithm() != null) {
                final var matchAlgorithm = new PRPAMT201306UV02MatchAlgorithm();
                matchAlgorithm.setValue(toSt(simpleQuery.getMatchAlgorithm()));
                matchAlgorithm.setSemanticsText(toSt("MatchAlgorithm"));
                matchCriterionList.setMatchAlgorithm(matchAlgorithm);
            }
            if (simpleQuery.getMinimumDegreeMatch() != null) {
                final var minimumDegreeMatch = new PRPAMT201306UV02MinimumDegreeMatch();
                minimumDegreeMatch.setValue(new INT(simpleQuery.getMinimumDegreeMatch()));
                minimumDegreeMatch.setSemanticsText(toSt("MinimumDegreeMatch"));
                matchCriterionList.setMinimumDegreeMatch(minimumDegreeMatch);
            }
            queryByParameter.setMatchCriterionList(matchCriterionList);
        }

        final var parameterList = new PRPAMT201306UV02ParameterList();
        if (simpleQuery.getLivingSubjectAdministrativeGenderCode() != null) {
            final var gender = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
            gender.setValue(List.of(new CE(simpleQuery.getLivingSubjectAdministrativeGenderCode(), null, null)));
            gender.setSemanticsText(toSt("LivingSubject.administrativeGender"));
            parameterList.getLivingSubjectAdministrativeGender().add(gender);
        }
        if (simpleQuery.getLivingSubjectBirthTime() != null) {
            final var birthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
            final var ivlts = new IVLTS();
            ivlts.setValue(simpleQuery.getLivingSubjectBirthTime());
            birthTime.setValue(List.of(ivlts));
            birthTime.setSemanticsText(toSt("LivingSubject.birthTime"));
            parameterList.getLivingSubjectBirthTime().add(birthTime);
        }
        for (final var id : simpleQuery.getLivingSubjectIds()) {
            final var livingSubjectId = new PRPAMT201306UV02LivingSubjectId();
            livingSubjectId.setValue(List.of(id));
            livingSubjectId.setSemanticsText(toSt("LivingSubject.id"));
            parameterList.getLivingSubjectId().add(livingSubjectId);
        }
        for (final var name : simpleQuery.getLivingSubjectNames()) {
            final var livingSubjectName = new PRPAMT201306UV02LivingSubjectName();
            livingSubjectName.setValue(List.of(name));
            livingSubjectName.setSemanticsText(toSt("LivingSubject.name"));
            parameterList.getLivingSubjectName().add(livingSubjectName);
        }
        if (simpleQuery.getPatientAddress() != null) {
            final var patientAddress = new PRPAMT201306UV02PatientAddress();
            patientAddress.setValue(List.of(simpleQuery.getPatientAddress()));
            patientAddress.setSemanticsText(toSt("Patient.addr"));
            parameterList.getPatientAddress().add(patientAddress);
        }
        queryByParameter.setParameterList(parameterList);

        toQueryByParameterSpecific(simpleQuery, queryByParameter);
        return queryByParameter;
    }

    /**
     * Reads the query ID and query parameters from a {@code queryByParameter} element into a simplified query.
     *
     * @param queryByParameter the {@code queryByParameter} element, may be {@code null}.
     * @param simpleQuery      the simplified query (or response, which extends the query) to fill.
     */
    public void fromQueryByParameter(final PRPAMT201306UV02QueryByParameter queryByParameter, final T simpleQuery) {
        if (queryByParameter == null) {
            return;
        }
        simpleQuery.setQueryId(queryByParameter.getQueryId());

        final var matchCriterionList = queryByParameter.getMatchCriterionList();
        if (matchCriterionList != null) {
            if (matchCriterionList.getMatchAlgorithm() != null) {
                simpleQuery.setMatchAlgorithm(fromMixed(matchCriterionList.getMatchAlgorithm().getValue()));
            }
            if (matchCriterionList.getMinimumDegreeMatch() != null
                    && matchCriterionList.getMinimumDegreeMatch().getValue() instanceof INT minimumDegreeMatch) {
                simpleQuery.setMinimumDegreeMatch(minimumDegreeMatch.getValue());
            }
        }

        final var parameterList = queryByParameter.getParameterList();
        if (parameterList != null) {
            parameterList.getLivingSubjectAdministrativeGender().stream()
                    .flatMap(parameter -> parameter.getValue().stream())
                    .findFirst()
                    .ifPresent(value -> simpleQuery.setLivingSubjectAdministrativeGenderCode(value.getCode()));
            parameterList.getLivingSubjectBirthTime().stream()
                    .flatMap(parameter -> parameter.getValue().stream())
                    .findFirst()
                    .ifPresent(value -> simpleQuery.setLivingSubjectBirthTime(value.getValue()));
            parameterList.getLivingSubjectId().forEach(parameter ->
                    simpleQuery.getLivingSubjectIds().addAll(parameter.getValue()));
            parameterList.getLivingSubjectName().forEach(parameter ->
                    simpleQuery.getLivingSubjectNames().addAll(parameter.getValue()));
            parameterList.getPatientAddress().stream()
                    .flatMap(parameter -> parameter.getValue().stream())
                    .findFirst()
                    .ifPresent(simpleQuery::setPatientAddress);
        }

        fromQueryByParameterSpecific(queryByParameter, simpleQuery);
    }

    /**
     * @return a new instance of the simplified query model.
     */
    protected abstract T newSimpleQuery();

    /**
     * Hook for transaction-specific parts of the message (outside of {@code queryByParameter}).
     */
    protected void toPrpaSpecific(final T simpleQuery, final PRPAIN201305UV02Type query) {
    }

    /**
     * Hook for transaction-specific parts of the message (outside of {@code queryByParameter}).
     */
    protected void fromPrpaSpecific(final PRPAIN201305UV02Type query, final T simpleQuery) {
    }

    /**
     * Hook for transaction-specific parts of {@code queryByParameter}. The parameter list has already been created.
     */
    protected void toQueryByParameterSpecific(final T simpleQuery,
                                              final PRPAMT201306UV02QueryByParameter queryByParameter) {
    }

    /**
     * Hook for transaction-specific parts of {@code queryByParameter}.
     */
    protected void fromQueryByParameterSpecific(final PRPAMT201306UV02QueryByParameter queryByParameter,
                                                final T simpleQuery) {
    }
}
