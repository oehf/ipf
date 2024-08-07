/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.audit

import groovy.xml.slurpersupport.GPathResult
import org.apache.cxf.binding.soap.SoapMessage
import org.apache.cxf.headers.Header
import org.apache.cxf.message.Message
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId
import org.openehealth.ipf.commons.audit.types.PurposeOfUse
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset.HumanUser

/**
 * WS audit dataset enricher which fulfills both IHE and Swiss EPR requirements.
 *
 * @author Dmytro Rud
 */
class SwissEprWsAuditDatasetEnricher extends XuaWsAuditDatasetEnricher {

    static final String SWISS_USER_POU_OID  = '2.16.756.5.30.1.127.3.10.5'
    static final String SWISS_USER_ROLE_OID = '2.16.756.5.30.1.127.3.10.6'

    @Override
    void enrichAuditDatasetFromRequest(SoapMessage message, Header.Direction headerDirection, WsAuditDataset auditDataset) {
        GPathResult xuaToken = extractXuaToken(message, headerDirection)
        if (xuaToken != null) {
            extractXuaTokenElements(xuaToken, auditDataset)
            if (!auditDataset.humanUsers.empty) {
                def iheUser = auditDataset.humanUsers[0]
                conditionallyAddHumanUser(createMainEprUser(xuaToken, iheUser), auditDataset)
                conditionallyAddHumanUser(createAdditionalEprUser(xuaToken, iheUser, auditDataset.purposesOfUse), auditDataset)
            }
        }

        extractW3cTraceContextId(message, auditDataset)
    }

    @Override
    void enrichAuditDatasetFromResponse(SoapMessage message, Header.Direction headerDirection, WsAuditDataset auditDataset) {
        extractW3cTraceContextId(message, auditDataset)
    }

    private static HumanUser createMainEprUser(GPathResult xuaToken, HumanUser iheUser) {
        def user = new HumanUser()
        user.id = xuaToken.Subject.NameID[0].text()
        user.name = xuaToken.AttributeStatement.Attribute.find { it.@Name == SUBJECT_NAME_ATTRIBUTE_NAME }.AttributeValue[0].text()
        user.roles.addAll(iheUser.roles)
        return user
    }

    private static HumanUser createAdditionalEprUser(GPathResult xuaToken, HumanUser iheUser, PurposeOfUse... purposesOfUse) {
        def user = new HumanUser()
        user.id = xuaToken.Subject.SubjectConfirmation.NameID[0].text()
        user.name = xuaToken.Subject.SubjectConfirmation.SubjectConfirmationData.AttributeStatement.Attribute.find { it.@Name == SUBJECT_NAME_ATTRIBUTE_NAME }.AttributeValue[0].text()
        switch (iheUser.roles.find { it.codeSystemName == SWISS_USER_ROLE_OID }?.code) {
            case 'HCP':
                if (purposesOfUse.find { (it.codeSystemName == SWISS_USER_POU_OID) && it.code.contains('AUTO') }) {
                    user.roles << ActiveParticipantRoleId.of('TCU', SWISS_USER_ROLE_OID, 'Technical User')
                } else {
                    user.roles << ActiveParticipantRoleId.of('ASS', SWISS_USER_ROLE_OID, 'Assistant')
                }
                break
            case 'PAT':
                user.roles << ActiveParticipantRoleId.of('REP', SWISS_USER_ROLE_OID, 'Representative')
                break
        }
        return user
    }

    private static void extractW3cTraceContextId(SoapMessage message, WsAuditDataset auditDataset) {
        if (auditDataset.w3cTraceContextId == null) {
            def httpHeaders = message.get(Message.PROTOCOL_HEADERS) as Map<String, List<String>>
            if (httpHeaders != null) {
                for (String headerName : httpHeaders.keySet()) {
                    if ('traceparent'.equalsIgnoreCase(headerName)) {
                        auditDataset.w3cTraceContextId = httpHeaders[headerName][0]
                        break
                    }
                }
            }
        }
    }

}
