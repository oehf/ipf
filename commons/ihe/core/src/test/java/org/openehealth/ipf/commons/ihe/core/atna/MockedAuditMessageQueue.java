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
package org.openehealth.ipf.commons.ihe.core.atna;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.queue.AbstractMockedAuditMessageQueue;
import org.openehealth.ipf.commons.xml.XsdValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.openehealth.ipf.commons.xml.XmlUtils.source;

/**
 * Mocked sender implementation for ATNA audit records.
 * Records the messages to allow verification in tests and, optionally,
 * validates them against the schema from DICOM Part 15 Appendix A.5.1.
 *
 * @author Jens Riemschneider
 */
@Slf4j
public class MockedAuditMessageQueue implements AbstractMockedAuditMessageQueue {

    private static final String NEW_VALIDATION_SCHEMA = "/atna2.xsd";
    private XsdValidator validator = new XsdValidator();
    private final boolean needValidation;

    @Getter
    List<AuditMessage> messages = Collections.synchronizedList(new ArrayList<>());

    public MockedAuditMessageQueue() {
        this(true);
    }

    public MockedAuditMessageQueue(boolean needValidation) {
        super();
        this.needValidation = needValidation;
    }

    @Override
    public void audit(AuditContext auditContext, AuditMessage... auditMessages) {
        for (AuditMessage message : auditMessages) {
            String s = Current.toString(message, true);
            log.debug(s);
            if (needValidation) {
                validator.validate(source(s), NEW_VALIDATION_SCHEMA);
            }
            messages.add(message);
        }
    }

    @Override
    public void clear() {
        messages.clear();
    }
}
