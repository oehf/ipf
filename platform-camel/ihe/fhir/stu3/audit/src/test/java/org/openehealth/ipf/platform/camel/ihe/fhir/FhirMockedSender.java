/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.audit.queue.AbstractMockedAuditMessageQueue;
import org.openehealth.ipf.commons.ihe.fhir.translation.AuditRecordTranslator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmytro Rud
 *
 * FIXME
 */
@Slf4j
public class FhirMockedSender implements AbstractMockedAuditMessageQueue {

    protected List<AuditEvent> messages = Collections.synchronizedList(new ArrayList<>());

    private final FhirContext fhirContext;
    private final boolean needValidation;
    private final AuditRecordTranslator translator = new AuditRecordTranslator();

    public FhirMockedSender(FhirContext fhirContext, boolean needValidation) {
        super();
        this.fhirContext = fhirContext;
        this.needValidation = needValidation;
    }

    @Override
    public List<AuditMessage> getMessages() {
        return null;
    }

    @Override
    public void audit(AuditContext auditContext, AuditMessage... auditMessages) {
        for (AuditMessage message : auditMessages) {
            AuditEvent auditEventResource = translator.translate(message);
            log.debug(fhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(auditEventResource));
            if (needValidation) {
                FhirValidator validator = fhirContext.newValidator();
                // FhirInstanceValidator instanceValidator = new FhirInstanceValidator();
                // validator.registerValidatorModule(instanceValidator);
                ValidationResult result = validator.validateWithResult(auditEventResource);
                if (!result.isSuccessful()) {
                    StringBuilder sb = new StringBuilder("Validation of FHIR AuditEvent failed:");
                    for (SingleValidationMessage error : result.getMessages()) {
                        sb.append('\n').append(error.toString());
                    }
                    throw new ValidationException(sb.toString());
                }
            }
            messages.add(auditEventResource);
        }
    }

    @Override
    public void clear() {
        messages.clear();
    }
}
