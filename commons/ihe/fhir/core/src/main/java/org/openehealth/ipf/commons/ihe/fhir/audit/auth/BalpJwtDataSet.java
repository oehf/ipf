package org.openehealth.ipf.commons.ihe.fhir.audit.auth;

import lombok.Data;

import java.util.Set;

@Data
public class BalpJwtDataSet {

    String opaqueJwt;
    String issuer;
    String subject;
    String audience;
    String jwtId;
    String clientId;
    String iheIuaSubjectName;
    String iheIuaSubjectOrganization;
    String iheIuaSubjectOrganizationId;
    Set<String> iheIuaSubjectRole;
    Set<String> iheIuaPurposeOfUse;
    String iheIuaHomeCommunityId;
    String iheIuaNationalProviderIdentifier;
    String iheIuaPersonId;
    String iheBppcPatientId;
    String iheBppcDocId;
    String iheBppcAcp;
}
