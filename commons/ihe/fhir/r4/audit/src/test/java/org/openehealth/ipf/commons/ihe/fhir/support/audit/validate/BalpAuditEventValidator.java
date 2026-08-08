/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.support.audit.validate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.marshal.BalpJsonSerializationStrategy;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Validates the AuditEvent a transaction puts on the wire against the StructureDefinition it claims.
 * <p>
 * The IHE transaction profiles constrain AuditEvent well beyond what an assertion on a handful of
 * elements can cover -- mandatory agent and entity slices, fixed codes, invariants tying the audit source
 * to one of the agents. This runs the real thing: an ATNA {@link AuditMessage} is marshalled by the same
 * serialization strategy an audit context would use, parsed back as a plain resource, and validated
 * against whatever {@code meta.profile} then says. Anything the profile requires and IPF does not produce
 * shows up as an error.
 * <p>
 * The profiles come from the IHE implementation guide packages IPF already ships for request and response
 * validation, so no separate source of truth is involved. Pass the classpath location of the package that
 * defines the transaction's audit profiles, e.g.
 * {@code new BalpAuditEventValidator("classpath:META-INF/profiles/v423/ihe.iti.mhd.tgz")}.
 *
 * @author Christian Ohr
 * @since 5.3
 */
public class BalpAuditEventValidator {

    /**
     * The BALP package itself, always loaded. The transaction packages depend on it but do not bundle it,
     * and without it the value sets their slices are bound to cannot be expanded -- which shows up as
     * mandatory slices being reported missing when they are in fact there.
     */
    public static final String BALP_PACKAGE_PATH = "classpath:META-INF/profiles/balp/v114/ihe.iti.balp.tgz";

    private static final Set<ResultSeverityEnum> CRITICAL = EnumSet.of(
        ResultSeverityEnum.FATAL, ResultSeverityEnum.ERROR);

    /**
     * Context used to read the serialized AuditEvent back. Deliberately vanilla: the profiled AuditEvent
     * classes must not be registered here, so that what is validated is the resource as a receiver sees
     * it rather than the Java object IPF built.
     */
    private final FhirContext wireContext = FhirContext.forR4();

    private final BalpJsonSerializationStrategy serializationStrategy = new BalpJsonSerializationStrategy();

    private final FhirValidator validator;

    /**
     * @param npmPackagePaths classpath locations of the IHE implementation guide packages defining the
     *                        audit profiles, e.g. {@code classpath:META-INF/profiles/v423/ihe.iti.mhd.tgz}
     */
    public BalpAuditEventValidator(String... npmPackagePaths) {
        this.validator = wireContext.newValidator()
            .setValidateAgainstStandardSchema(false)
            .setValidateAgainstStandardSchematron(false)
            .registerValidatorModule(instanceValidator(npmPackagePaths));
    }

    private FhirInstanceValidator instanceValidator(String... npmPackagePaths) {
        var supportChain = new ValidationSupportChain();
        var npmSupport = new NpmPackageValidationSupport(wireContext);
        for (var path : Stream.concat(Stream.of(BALP_PACKAGE_PATH), Stream.of(npmPackagePaths)).toList()) {
            try {
                npmSupport.loadPackageFromClasspath(path);
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot load validation package " + path, e);
            }
        }
        supportChain.addValidationSupport(npmSupport);
        supportChain.addValidationSupport(new DefaultProfileValidationSupport(wireContext));
        supportChain.addValidationSupport(new SnapshotGeneratingValidationSupport(wireContext));
        supportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(wireContext));
        supportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(wireContext));

        var instanceValidator = new FhirInstanceValidator(supportChain);
        // an audit record claiming a profile nobody knows is a defect, not something to pass silently
        instanceValidator.setErrorForUnknownProfiles(true);
        instanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Hint);
        instanceValidator.setAnyExtensionsAllowed(true);
        return instanceValidator;
    }

    /**
     * Marshals the audit message the way a FHIR-serializing audit context would, and reads it back.
     *
     * @param auditMessage ATNA audit message of the transaction
     * @return the AuditEvent a receiver would get
     */
    public AuditEvent toAuditEvent(AuditMessage auditMessage) {
        var writer = new StringWriter();
        try {
            serializationStrategy.marshal(auditMessage, writer, true);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return (AuditEvent) wireContext.newJsonParser().parseResource(writer.toString());
    }

    /**
     * @param auditMessage ATNA audit message of the transaction
     * @return every validation message about the AuditEvent it turns into
     */
    public List<SingleValidationMessage> validate(AuditMessage auditMessage) {
        return validate(toAuditEvent(auditMessage));
    }

    /**
     * @param auditEvent AuditEvent to validate against the profiles it claims
     * @return every validation message, in the order the validator reported them
     */
    public List<SingleValidationMessage> validate(AuditEvent auditEvent) {
        return validator.validateWithResult(auditEvent).getMessages();
    }

    /**
     * @param auditEvent an AuditEvent
     * @return the canonical URLs it claims to conform to
     */
    public static List<String> claimedProfiles(AuditEvent auditEvent) {
        return auditEvent.getMeta().getProfile().stream()
            .map(PrimitiveType::getValueAsString)
            .toList();
    }

    /**
     * @param messages validation messages
     * @return those of them that make the resource non-conformant
     */
    public static List<SingleValidationMessage> errors(List<SingleValidationMessage> messages) {
        return messages.stream()
            .filter(message -> CRITICAL.contains(message.getSeverity()))
            .toList();
    }

    /**
     * Fails unless the AuditEvent the audit message turns into conforms to the profile it claims. An
     * AuditEvent claiming no profile at all fails as well: without one there is nothing to validate
     * against, and the transaction was supposed to declare one.
     *
     * @param auditMessage ATNA audit message of the transaction
     * @throws AssertionError listing every error, with the serialized AuditEvent
     */
    public void assertConformant(AuditMessage auditMessage) {
        var auditEvent = toAuditEvent(auditMessage);
        if (claimedProfiles(auditEvent).isEmpty()) {
            throw new AssertionError("AuditEvent claims no profile:\n" + asJson(auditEvent));
        }
        var errors = errors(validate(auditEvent));
        if (!errors.isEmpty()) {
            throw new AssertionError(describe(auditEvent, errors));
        }
    }

    /**
     * Like {@link #assertConformant(AuditMessage)}, but tolerating errors that IPF is known not to
     * satisfy yet. Every known gap has to occur: one that has been closed makes this fail, so that it
     * gets removed here rather than quietly staying on the list.
     * <p>
     * A gap is matched as a substring of the error's location or of its message. Prefer the location,
     * or an element value quoted in the message: the validator localizes its prose, so matching German
     * or English wording would make the test depend on the machine it runs on.
     *
     * @param auditMessage ATNA audit message of the transaction
     * @param knownGaps    substrings identifying the errors that are expected for now
     * @throws AssertionError on any other error, or when a known gap no longer occurs
     */
    public void assertConformantApartFrom(AuditMessage auditMessage, String... knownGaps) {
        var auditEvent = toAuditEvent(auditMessage);
        if (claimedProfiles(auditEvent).isEmpty()) {
            throw new AssertionError("AuditEvent claims no profile:\n" + asJson(auditEvent));
        }
        var errors = errors(validate(auditEvent));

        var closed = Stream.of(knownGaps)
            .filter(gap -> errors.stream().noneMatch(error -> matches(error, gap)))
            .toList();
        if (!closed.isEmpty()) {
            throw new AssertionError(
                "No longer reported, remove from the known gaps of this test: " + closed);
        }

        var unexpected = errors.stream()
            .filter(error -> Stream.of(knownGaps).noneMatch(gap -> matches(error, gap)))
            .toList();
        if (!unexpected.isEmpty()) {
            throw new AssertionError(describe(auditEvent, unexpected));
        }
    }

    private static boolean matches(SingleValidationMessage error, String knownGap) {
        return (error.getLocationString() != null && error.getLocationString().contains(knownGap))
            || (error.getMessage() != null && error.getMessage().contains(knownGap));
    }

    /**
     * @param auditEvent the AuditEvent that was validated
     * @param messages   messages to describe
     * @return a report naming the claimed profiles, every message and the resource itself
     */
    public String describe(AuditEvent auditEvent, List<SingleValidationMessage> messages) {
        return messages.stream()
            .map(message -> "  [%s] %s: %s".formatted(
                message.getSeverity(), message.getLocationString(), message.getMessage()))
            .collect(Collectors.joining("\n",
                "AuditEvent does not conform to %s:\n".formatted(claimedProfiles(auditEvent)),
                "\n" + asJson(auditEvent)));
    }

    /**
     * @param auditEvent an AuditEvent
     * @return it, pretty-printed as JSON
     */
    public String asJson(AuditEvent auditEvent) {
        return wireContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent);
    }

}
