/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit.types;

/**
 * Specification of the role(s) the user plays when performing the event,
 * as assigned in role-based access control security.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public interface ActiveParticipantRoleId extends CodedValueType {

    static ActiveParticipantRoleId of(String code, String codeSystemName, String originalText) {
        return new ActiveParticipantRoleIdImpl(code, codeSystemName, originalText);
    }

    class ActiveParticipantRoleIdImpl extends CodedValueTypeImpl implements ActiveParticipantRoleId {
        public ActiveParticipantRoleIdImpl(String code, String codeSystemName, String originalText) {
            super(code, codeSystemName, originalText);
        }
    }
}
