/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.function.Function;

/**
 * Resolves canonical references that carry a version suffix, e.g.
 * {@code http://hl7.org/fhir/StructureDefinition/Encounter|4.0.1}, by retrying the lookup with the
 * version stripped off.
 * <p>
 * Implementation guides published with more recent versions of the IHE/HL7 IG publisher write such
 * version-pinned references into their snapshots. For the
 * definitions contained in an implementation guide package this is harmless, because
 * {@link org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport} indexes them both
 * with and without their version. The FHIR core definitions, however, are served by
 * {@link ca.uhn.fhir.context.support.DefaultProfileValidationSupport}, which for FHIR R4 indexes
 * them by their plain canonical URL only, so a reference to {@code …/Encounter|4.0.1} would go
 * unresolved and the validator would report the target type of the referencing element as unknown.
 * <p>
 * Belongs at the end of a {@link org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain},
 * behind the supports that hold the actual definitions, which it delegates to. Delegating back to the
 * very chain this support is part of terminates, because the retried URL no longer carries a version.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class VersionedCanonicalUrlValidationSupport implements IValidationSupport {

    private final FhirContext fhirContext;
    private final IValidationSupport delegate;

    /**
     * @param fhirContext FhirContext
     * @param delegate    validation support to retry the version-less lookup against
     */
    public VersionedCanonicalUrlValidationSupport(FhirContext fhirContext, IValidationSupport delegate) {
        this.fhirContext = fhirContext;
        this.delegate = delegate;
    }

    @Override
    public FhirContext getFhirContext() {
        return fhirContext;
    }

    @Override
    public String getName() {
        return fhirContext.getVersion().getVersion() + " Versioned Canonical URL Validation Support";
    }

    @Override
    public IBaseResource fetchStructureDefinition(String url) {
        return withoutVersion(url, delegate::fetchStructureDefinition);
    }

    @Override
    public IBaseResource fetchValueSet(String url) {
        return withoutVersion(url, delegate::fetchValueSet);
    }

    @Override
    public IBaseResource fetchCodeSystem(String system) {
        return withoutVersion(system, delegate::fetchCodeSystem);
    }

    @Override
    public <T extends IBaseResource> T fetchResource(Class<T> clazz, String uri) {
        return withoutVersion(uri, u -> delegate.fetchResource(clazz, u));
    }

    private <T> T withoutVersion(String url, Function<String, T> lookup) {
        if (url == null) {
            return null;
        }
        var pipeIndex = url.indexOf('|');
        return (pipeIndex > 0) ? lookup.apply(url.substring(0, pipeIndex)) : null;
    }
}
