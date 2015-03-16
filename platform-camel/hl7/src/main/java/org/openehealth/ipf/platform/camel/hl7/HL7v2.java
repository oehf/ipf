/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.hl7;

import ca.uhn.hl7v2.*;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.impl.SimpleValidationExceptionHandler;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.ValueBuilder;
import org.apache.camel.component.hl7.HL7;
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;

/**
 * Factory class for commonly used HL7-related expressions, predicates and
 * processors.
 */
public final class HL7v2 {

    private static final Parser FALLBACK = new GenericParser();

    private HL7v2() {
    }

    /**
     * @return Expression that extracts data from HL7 messages based on a HAPI Terser expression
     */
    public static ValueBuilder get(String terserSpec) {
        return HL7.terser(terserSpec);
    }

    /**
     * @return Expression that sets data in a HL7 messages based on a HAPI Terser expression
     */
    public static Expression set(String terserSpec, Expression value) {
        return new TerserSetExpression(terserSpec, value);
    }

    /**
     * @return Expression that creates an acknowledgement to a message
     */
    public static ValueBuilder ack() {
        return new ValueBuilder(HL7.ack());
    }

    /**
     * @return Expression that creates an acknowledgement to a message using a given code
     */
    public static ValueBuilder ack(AcknowledgmentCode acknowledgmentCode) {
        return ack(acknowledgmentCode, null, ErrorCode.APPLICATION_INTERNAL_ERROR);
    }

    /**
     * @return Expression that creates an acknowledgement to a message using a given code, message and error details
     */
    public static ValueBuilder ack(AcknowledgmentCode acknowledgmentCode, String message, ErrorCode errorCode) {
        return new ValueBuilder(HL7.ack(acknowledgmentCode, message, errorCode));
    }

    /**
     * Returns a response to the message in the body, using the provided event type and trigger event
     *
     * @param eventType    event type, e.g. ADT
     * @param triggerEvent trigger event, e.g. A01
     * @return expression that generates the response for the body message
     */
    public static ValueBuilder response(String eventType, String triggerEvent) {
        return new ValueBuilder(new ResponseExpression(eventType, triggerEvent));
    }

    /**
     * Returns a constant response message. Useful for mocks and tests only
     *
     * @param message constant message
     * @return expression that generates a static message using the same HapiContext as the message
     * in the body
     */
    public static ValueBuilder staticResponse(String message) {
        return new ValueBuilder(new StaticResponseExpression(message));
    }

    /**
     * Returns a validating Camel processor for a message. The actual validation rules
     * to be used is defined in the message's HapiContext. Unlike {@link #messageConforms()}, this processor
     * throws an exception with details about the validation result instead of just returning true or false.
     *
     * @return a validating Camel processor for a message
     */
    public static Processor validatingProcessor() {
        return validatingProcessor(null);
    }

    /**
     * Returns a validating Camel processor for a message. The actual validation rules
     * to be used is defined in the message's HapiContext. Unlike {@link #messageConforms()}, this processor
     * throws an exception with details about the validation result instead of just returning true or false.
     *
     * @param context HAPI context
     *
     * @return a validating Camel processor for a message
     */
    public static Processor validatingProcessor(final HapiContext context) {
        return new Processor() {

            @Override
            public void process(Exchange exchange) throws Exception {
                if (! ValidatorAdapter.validationEnabled(exchange)) {
                    return;
                }

                Message msg = bodyMessage(exchange);
                HapiContext ctx = context == null ? msg.getParser().getHapiContext() : context;

                // We could also write an exception handler on top of SimpleValidationExceptionHandler that
                // encapsulates the behavior below, but that may restrict custom validation...
                SimpleValidationExceptionHandler handler = new SimpleValidationExceptionHandler(ctx);
                handler.setMinimumSeverityToCollect(Severity.ERROR);
                if (ctx.<Boolean>getMessageValidator().validate(msg, handler)) {
                    throw new ValidationException("Message validation failed", handler.getExceptions());
                }
            }
        };
    }

    /**
     * @return Predicate that returns true if the message validates successfully
     */
    public static Predicate messageConforms() {
        return HL7.messageConforms();
    }

    /**
     * @return Predicate that returns true if the message validates successfully using a dedicated HapiContext
     */
    public static Predicate messageConformsTo(HapiContext hapiContext) {
        return HL7.messageConformsTo(hapiContext);
    }

    /**
     * @return Predicate that returns true if the message validates successfully using an expression that returns
     * a {@link ca.uhn.hl7v2.validation.ValidationContext}.
     */
    public static Predicate messageConformsTo(Expression expression) {
        return HL7.messageConformsTo(expression);
    }

    /**
     * Returns the HAPI Message from the message body. If the body is a string, it is parsed on the fly
     *
     * @param exchange exchange
     * @return HAPI message
     * @throws ca.uhn.hl7v2.HL7Exception
     */
    static Message bodyMessage(Exchange exchange) throws HL7Exception {
        Object body = exchange.getIn().getBody();
        Message message;

        if (body instanceof MessageAdapter) {
            message = ((MessageAdapter) body).getHapiMessage();
        } else if (body instanceof Message) {
            message = (Message) body;
        } else if (body instanceof String) {
            HapiContext context = exchange.getIn().getHeader("CamelHL7Context", HapiContext.class);
            Parser parser = context != null ? context.getGenericParser() : FALLBACK;
            message = parser.parse((String) body);
        } else {
            // try type conversion
            message = exchange.getIn().getBody(Message.class);
        }
        Validate.notNull(message, "Exchange does not contain or can be converted to the required 'ca.uhn.hl7v2.model.Message' type");
        return message;
    }


}
