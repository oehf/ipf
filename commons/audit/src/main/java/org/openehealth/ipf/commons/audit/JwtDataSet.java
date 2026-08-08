/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * The claims extracted from an access token, named after the IUA and BPPC claims they carry. Nothing
 * about them is specific to an audit format: they are kept next to the audit message so that whoever
 * renders it can build from the claims themselves, rather than reverse-engineering them from the ATNA
 * participants the claims were encoded into.
 */
@Data
public class JwtDataSet implements Serializable {

    /**
     * Whether the request carried an access token the audit source could not look into -- a reference
     * token, or one encrypted to somebody else. All that can be recorded of such a token is that it was
     * there; every other field stays empty. The token itself is deliberately not kept: it is a
     * credential, and an audit record is not the place for one.
     */
    boolean opaque;

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

    /**
     * @return a data set standing for an access token that was presented but could not be looked into
     */
    public static JwtDataSet opaque() {
        var jwtDataSet = new JwtDataSet();
        jwtDataSet.setOpaque(true);
        return jwtDataSet;
    }
}
