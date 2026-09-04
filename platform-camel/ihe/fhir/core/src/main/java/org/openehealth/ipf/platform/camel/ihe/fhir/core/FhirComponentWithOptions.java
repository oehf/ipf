/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.core.TransactionOptionsUtils;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionOptions;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionOptionsProvider;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Base class for FHIR components offering options and optionsProvider endpoint parameters
 *
 * @author Christian Ohr
 * @since 4.1
 */
public abstract class FhirComponentWithOptions<AuditDatasetType extends FhirAuditDataset, O extends Enum<O> & FhirTransactionOptions>
        extends FhirComponent<AuditDatasetType> {

    private final Supplier<FhirTransactionOptionsProvider<O>> optionsProviderSupplier;

    public FhirComponentWithOptions(FhirInteractionId fhirInteractionId, Supplier<FhirTransactionOptionsProvider<O>> optionsProviderSupplier) {
        super(fhirInteractionId);
        this.optionsProviderSupplier = optionsProviderSupplier;
    }

    public FhirComponentWithOptions(CamelContext context, FhirInteractionId fhirInteractionId, Supplier<FhirTransactionOptionsProvider<O>> optionsProviderSupplier) {
        super(context, fhirInteractionId);
        this.optionsProviderSupplier = optionsProviderSupplier;
    }

    @Override
    protected FhirEndpointConfiguration createConfig(String remaining, Map<String, Object> parameters) throws Exception {
        FhirTransactionOptionsProvider<O> optionsProvider =
                getAndRemoveOrResolveReferenceParameter(parameters, "iheOptionsProvider", FhirTransactionOptionsProvider.class, optionsProviderSupplier.get());
        var options = getAndRemoveParameter(parameters, "iheOptions", String.class, optionsProvider.getDefaultOption().name());
        var itiOptions = TransactionOptionsUtils.split(options, optionsProvider.getTransactionOptionsType());
        if (itiOptions.isEmpty()) {
            throw new IllegalArgumentException("Options parameter for " + getInteractionId() + " is invalid");
        }
        getInteractionId().init(optionsProvider, itiOptions);
        return super.createConfig(remaining, parameters);
    }

}
