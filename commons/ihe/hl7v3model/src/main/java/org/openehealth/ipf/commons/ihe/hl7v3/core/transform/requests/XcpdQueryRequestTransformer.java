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

import net.ihe.gazelle.hl7v3.coctmt090300UV01.COCTMT090300UV01AssignedDevice;
import net.ihe.gazelle.hl7v3.datatypes.CS;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.TEL;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01EntityRsp;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01RespondTo;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02LivingSubjectBirthPlaceAddress;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02LivingSubjectBirthPlaceName;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MothersMaidenName;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02PatientTelecom;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02PrincipalCareProviderId;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02QueryByParameter;
import net.ihe.gazelle.hl7v3.quqimt021001UV01.QUQIMT021001UV01AuthorOrPerformer;
import net.ihe.gazelle.hl7v3.voc.CommunicationFunctionType;
import net.ihe.gazelle.hl7v3.voc.EntityClassRoot;
import net.ihe.gazelle.hl7v3.voc.EntityDeterminer;
import net.ihe.gazelle.hl7v3.voc.NullFlavor;
import net.ihe.gazelle.hl7v3.voc.RoleClassAssignedEntity;
import net.ihe.gazelle.hl7v3.voc.XParticipationAuthorPerformer;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.XcpdQueryRequest;

import java.util.List;
import java.util.Optional;

import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.toIi;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.toSt;

/**
 * Transformer between objects {@link XcpdQueryRequest} and {@link PRPAIN201305UV02Type} (XCPD, ITI-55).
 *
 * @author Christian Ohr
 * @since 6.1
 */
public class XcpdQueryRequestTransformer extends AbstractPatientDemographicsQueryRequestTransformer<XcpdQueryRequest> {

    @Override
    protected XcpdQueryRequest newSimpleQuery() {
        return new XcpdQueryRequest();
    }

    @Override
    protected void toPrpaSpecific(final XcpdQueryRequest simpleQuery, final PRPAIN201305UV02Type query) {
        if (simpleQuery.getRespondTo() != null) {
            final var respondTo = new MCCIMT000100UV01RespondTo();
            respondTo.setTypeCode(CommunicationFunctionType.RSP);
            final var telecom = new TEL();
            telecom.setValue(simpleQuery.getRespondTo());
            respondTo.setTelecom(telecom);
            // entityRsp is required, but its content is not relevant for XCPD
            final var entityRspId = new II();
            entityRspId.setNullFlavor(NullFlavor.NA);
            final var entityRsp = new MCCIMT000100UV01EntityRsp();
            entityRsp.setClassCode(EntityClassRoot.ENT);
            entityRsp.setDeterminerCode(EntityDeterminer.INSTANCE);
            entityRsp.setId(entityRspId);
            respondTo.getEntityRsp().add(entityRsp);
            query.getRespondTo().add(respondTo);
        }
        if (simpleQuery.getCommunityPatientIdAssigningAuthority() != null) {
            final var assignedDevice = new COCTMT090300UV01AssignedDevice();
            assignedDevice.setClassCode(RoleClassAssignedEntity.ASSIGNED);
            assignedDevice.getId().add(toIi(simpleQuery.getCommunityPatientIdAssigningAuthority()));
            final var authorOrPerformer = new QUQIMT021001UV01AuthorOrPerformer();
            authorOrPerformer.setTypeCode(XParticipationAuthorPerformer.AUT);
            authorOrPerformer.setAssignedDevice(assignedDevice);
            query.getControlActProcess().getAuthorOrPerformer().add(authorOrPerformer);
        }
    }

    @Override
    protected void fromPrpaSpecific(final PRPAIN201305UV02Type query, final XcpdQueryRequest simpleQuery) {
        query.getRespondTo().stream()
                .map(MCCIMT000100UV01RespondTo::getTelecom)
                .filter(telecom -> telecom != null && telecom.getValue() != null)
                .findFirst()
                .ifPresent(telecom -> simpleQuery.setRespondTo(telecom.getValue()));
        Optional.ofNullable(query.getControlActProcess())
                .flatMap(controlActProcess -> controlActProcess.getAuthorOrPerformer().stream()
                        .map(QUQIMT021001UV01AuthorOrPerformer::getAssignedDevice)
                        .filter(assignedDevice -> assignedDevice != null && !assignedDevice.getId().isEmpty())
                        .findFirst())
                .ifPresent(assignedDevice ->
                        simpleQuery.setCommunityPatientIdAssigningAuthority(assignedDevice.getId().get(0).getRoot()));
    }

    @Override
    protected void toQueryByParameterSpecific(final XcpdQueryRequest simpleQuery,
                                              final PRPAMT201306UV02QueryByParameter queryByParameter) {
        if (simpleQuery.getResponsePriorityCode() != null) {
            queryByParameter.setResponsePriorityCode(new CS(simpleQuery.getResponsePriorityCode(), null, null));
        }
        final var parameterList = queryByParameter.getParameterList();
        if (simpleQuery.getPatientTelecom() != null) {
            final var patientTelecom = new PRPAMT201306UV02PatientTelecom();
            patientTelecom.setValue(List.of(simpleQuery.getPatientTelecom()));
            patientTelecom.setSemanticsText(toSt("Patient.telecom"));
            parameterList.getPatientTelecom().add(patientTelecom);
        }
        if (simpleQuery.getMothersMaidenName() != null) {
            final var mothersMaidenName = new PRPAMT201306UV02MothersMaidenName();
            mothersMaidenName.setValue(List.of(simpleQuery.getMothersMaidenName()));
            mothersMaidenName.setSemanticsText(toSt("Person.MothersMaidenName"));
            parameterList.getMothersMaidenName().add(mothersMaidenName);
        }
        if (simpleQuery.getLivingSubjectBirthPlaceAddress() != null) {
            final var birthPlaceAddress = new PRPAMT201306UV02LivingSubjectBirthPlaceAddress();
            birthPlaceAddress.setValue(List.of(simpleQuery.getLivingSubjectBirthPlaceAddress()));
            birthPlaceAddress.setSemanticsText(toSt("LivingSubject.BirthPlace.Addr"));
            parameterList.getLivingSubjectBirthPlaceAddress().add(birthPlaceAddress);
        }
        if (simpleQuery.getLivingSubjectBirthPlaceName() != null) {
            final var birthPlaceName = new PRPAMT201306UV02LivingSubjectBirthPlaceName();
            birthPlaceName.setValue(List.of(simpleQuery.getLivingSubjectBirthPlaceName()));
            birthPlaceName.setSemanticsText(toSt("LivingSubject.BirthPlace.Place.Name"));
            parameterList.getLivingSubjectBirthPlaceName().add(birthPlaceName);
        }
        for (final var id : simpleQuery.getPrincipalCareProviderIds()) {
            final var principalCareProviderId = new PRPAMT201306UV02PrincipalCareProviderId();
            principalCareProviderId.setValue(List.of(id));
            principalCareProviderId.setSemanticsText(toSt("AssignedProvider.id"));
            parameterList.getPrincipalCareProviderId().add(principalCareProviderId);
        }
    }

    @Override
    protected void fromQueryByParameterSpecific(final PRPAMT201306UV02QueryByParameter queryByParameter,
                                                final XcpdQueryRequest simpleQuery) {
        if (queryByParameter.getResponsePriorityCode() != null) {
            simpleQuery.setResponsePriorityCode(queryByParameter.getResponsePriorityCode().getCode());
        }
        final var parameterList = queryByParameter.getParameterList();
        if (parameterList == null) {
            return;
        }
        parameterList.getPatientTelecom().stream()
                .flatMap(parameter -> parameter.getValue().stream())
                .findFirst()
                .ifPresent(simpleQuery::setPatientTelecom);
        parameterList.getMothersMaidenName().stream()
                .flatMap(parameter -> parameter.getValue().stream())
                .findFirst()
                .ifPresent(simpleQuery::setMothersMaidenName);
        parameterList.getLivingSubjectBirthPlaceAddress().stream()
                .flatMap(parameter -> parameter.getValue().stream())
                .findFirst()
                .ifPresent(simpleQuery::setLivingSubjectBirthPlaceAddress);
        parameterList.getLivingSubjectBirthPlaceName().stream()
                .flatMap(parameter -> parameter.getValue().stream())
                .findFirst()
                .ifPresent(simpleQuery::setLivingSubjectBirthPlaceName);
        parameterList.getPrincipalCareProviderId().forEach(parameter ->
                simpleQuery.getPrincipalCareProviderIds().addAll(parameter.getValue()));
    }
}
