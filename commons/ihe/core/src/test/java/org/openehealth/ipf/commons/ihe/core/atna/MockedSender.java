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

import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.xml.XsdValidator;
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;

import static org.openehealth.ipf.commons.xml.XmlUtils.source;

/**
 * Mocked sender implementation for ATNA audit records.
 * Records the messages to allow verification in tests and, optionally,
 * validates them against the schema from DICOM Part 15 Appendix A.5.1.
 *
 * @author Jens Riemschneider
 */
@Slf4j
public class MockedSender extends AbstractMockedAuditSender<AuditEventMessage> {

    private static final String VALIDATION_SCHEMA = "/dicomModified.xsd";
    XsdValidator validator = new XsdValidator();

    public MockedSender() {
        super(true);
    }

    public MockedSender(boolean needValidation) {
        super(needValidation);
    }

    @Override
    public void sendAuditEvent(AuditEventMessage[] msg) throws Exception {
        for (AuditEventMessage message : msg) {
            String s = message.toString();
            log.debug(s);
            if (needValidation) {
                validator.validate(source(s), VALIDATION_SCHEMA);
            }
            messages.add(message);
        }
    }

}
