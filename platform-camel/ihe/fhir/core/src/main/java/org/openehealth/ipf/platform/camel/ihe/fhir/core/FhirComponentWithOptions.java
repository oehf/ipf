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
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Base class for FHIR components offering a options and optionsProvider endpoint parameters
 *
 * @author Christian Ohr
 * @since 4.1
 */
public abstract class FhirComponentWithOptions<AuditDatasetType extends FhirAuditDataset,
        O extends Enum<O> & FhirTransactionOptions,
        P extends FhirTransactionOptionsProvider<FhirQueryAuditDataset, O>>
        extends FhirComponent<AuditDatasetType> {

    private final Supplier<P> optionsProviderSupplier;

    public FhirComponentWithOptions(FhirInteractionId<AuditDatasetType> fhirInteractionId, Supplier<P> optionsProviderSupplier) {
        super(fhirInteractionId);
        this.optionsProviderSupplier = optionsProviderSupplier;
    }

    public FhirComponentWithOptions(CamelContext context, FhirInteractionId<AuditDatasetType> fhirInteractionId, Supplier<P> optionsProviderSupplier) {
        super(context, fhirInteractionId);
        this.optionsProviderSupplier = optionsProviderSupplier;
    }

    @Override
    protected FhirEndpointConfiguration<AuditDatasetType> createConfig(String remaining, Map<String, Object> parameters) throws Exception {
        FhirTransactionOptionsProvider<AuditDatasetType, O> optionsProvider =
                getAndRemoveOrResolveReferenceParameter(parameters, "optionsProvider", FhirTransactionOptionsProvider.class, optionsProviderSupplier.get());
        var options = getAndRemoveParameter(parameters, "options", String.class, optionsProvider.getDefaultOption().name());
        List<O> itiOptions = TransactionOptionsUtils.split(options, optionsProvider.getTransactionOptionsType());
        if (itiOptions.isEmpty()) {
            throw new IllegalArgumentException("Options parameter for " + getInteractionId() + " is invalid");
        }
        getInteractionId().init(optionsProvider, itiOptions);
        return super.createConfig(remaining, parameters);
    }

}
