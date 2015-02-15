/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.platform.camel.hl7.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.Severity;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.validation.MessageRule;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.gazelle.validation.core.CachingGazelleProfileRule;
import org.openehealth.ipf.gazelle.validation.profile.ConformanceProfile;
import org.openehealth.ipf.gazelle.validation.profile.HL7v2Transactions;
import org.openehealth.ipf.gazelle.validation.profile.store.GazelleProfileStore;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;

/**
 * Factory for manually triggering a validation of a message depending on a profile or a defined
 * IHETransaction. In general this should not be necessary when the HapiContext of the parsed
 * message contains a ValidationContext that includes the corresponding
 * {@link org.openehealth.ipf.gazelle.validation.core.GazelleProfileRule GazelleProfileRule}
 * instance as one of its message rules. In that case it would be sufficient to use
 * {@link org.openehealth.ipf.platform.camel.hl7.HL7v2#validatingProcessor()} or
 * {@link org.apache.camel.component.hl7.HL7#messageConforms()}.
 *
 * @author Boris Stanojevic
 * @author Christian Ohr
 */
public final class ConformanceProfileValidators {

    private static final HapiContext FALLBACK_HAPI_CONTEXT;

    static {
        FALLBACK_HAPI_CONTEXT = new DefaultHapiContext();
        FALLBACK_HAPI_CONTEXT.setProfileStore(new GazelleProfileStore());
        FALLBACK_HAPI_CONTEXT.setValidationContext(ValidationContextFactory.noValidation());
    }

    private ConformanceProfileValidators() {
    }

    /**
     * Returns a validating Camel processor for a dedicated profile
     *
     * @param conformanceProfile HL7 conformance profile
     * @return a validating Camel processor for a dedicated profile
     */
    public static Processor validatingProcessor(final ConformanceProfile conformanceProfile) {
        return new Processor() {

            private CachingGazelleProfileRule rule = new CachingGazelleProfileRule(conformanceProfile);

            @Override
            public void process(Exchange exchange) throws Exception {
                doValidate(bodyMessage(exchange), rule);
            }
        };
    }

    /**
     * Returns a validating Camel processor for a message in a IHE transaction. The actual profile
     * to be used is guessed from the message's event type and version
     *
     * @param iheTransaction IHE transaktion
     * @return a validating Camel processor for a message in a IHE transaction
     */
    public static Processor validatingProcessor(final HL7v2Transactions iheTransaction) {
        return new Processor() {

            private CachingGazelleProfileRule rule = new CachingGazelleProfileRule(iheTransaction);

            @Override
            public void process(Exchange exchange) throws Exception {
                doValidate(bodyMessage(exchange), rule);
            }
        };
    }

    private static void doValidate(Message message, final MessageRule validator)
            throws IOException, JAXBException {
        throwIPFValidationException(validator.apply(message));
    }

    private static void throwIPFValidationException(ca.uhn.hl7v2.validation.ValidationException[] exceptions) {
        List<ca.uhn.hl7v2.validation.ValidationException> fatalExceptions = new ArrayList<>();
        for (ca.uhn.hl7v2.validation.ValidationException exception : exceptions) {
            if (exception.getSeverity().equals(Severity.ERROR)) {
                fatalExceptions.add(exception);
            }
        }
        if (!fatalExceptions.isEmpty()) {
            throw new ValidationException("Message validation failed", fatalExceptions);
        }
    }

    /**
     * Returns the HAPI Message from the message body. If the body is a string, it is parsed on the fly
     * using
     * @param exchange
     * @return HAPI message
     * @throws HL7Exception
     */
    private static Message bodyMessage(Exchange exchange) throws HL7Exception {
        Object body = exchange.getIn().getBody();
        Message message;

        if (body instanceof MessageAdapter) {
            message = ((MessageAdapter)body).getHapiMessage();
        } else if (body instanceof Message) {
            message = (Message)body;
        } else if (body instanceof String) {
            HapiContext context = exchange.getIn().getHeader("CamelHL7Context", HapiContext.class);
            context = context != null ? context : FALLBACK_HAPI_CONTEXT;
            message = new GenericParser(context).parse((String)body);
        } else {
            // try type conversion
            message = exchange.getIn().getBody(Message.class);
        }
        Validate.notNull(message, "Exchange does not contain or can be converted to the required 'ca.uhn.hl7v2.model.Message' type");
        return message;
    }

}
