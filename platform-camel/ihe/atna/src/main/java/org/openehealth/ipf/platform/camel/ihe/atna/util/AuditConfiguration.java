/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.atna.util;

import org.apache.camel.impl.DefaultComponent;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.core.config.ContextFacade;

import java.util.Collection;
import java.util.Map;

/**
 * @author Christian Ohr
 */
public abstract class AuditConfiguration {

    public static AuditContext obtainAuditContext(DefaultComponent component, Map<String, Object> parameters) {
        Boolean audit = component.getAndRemoveParameter(parameters, "audit", Boolean.class, true);
        AuditContext auditContext = component.resolveAndRemoveReferenceParameter(parameters, "auditContext", AuditContext.class);
        if (auditContext == null) {
            if (audit != null && !audit) {
                auditContext = AuditContext.noAudit();
            } else {
                Collection<AuditContext> beans = ContextFacade.getBeans(AuditContext.class);
                if (beans.size() == 1) {
                    auditContext = beans.iterator().next();
                } else {
                    throw new AuditException("Must have exactly one bean of type " + AuditContext.class.getName() + ", but was : " + beans.size());
                }
            }
        }
        return auditContext;
    }

}