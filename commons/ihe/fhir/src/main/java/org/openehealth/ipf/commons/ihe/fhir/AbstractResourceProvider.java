/*
 * Copyright 2015 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.rest.server.IResourceProvider;

/**
 * Abstract resource provider that allows subclasses to forward the received payload into the
 * Camel route served by the consumer.
 * <p>
 * Note that this can be subclassed for writing
 * resource-specific providers, while plain providers should extend from {@link AbstractPlainProvider}.
 * </p>
 * <p>
 * Also note that there can be only one AbstractResourceProvider per resource type served by the
 * FHIR servlet. As this does not necessarily fit the modular approach of IHE transaction
 * endpoints, it is recommended to derive resource providers from AbstractPlainProvider instead.
 * </p>
 *
 * @since 3.1
 */
public abstract class AbstractResourceProvider<AuditDatasetType extends FhirAuditDataset>
        extends AbstractPlainProvider<AuditDatasetType> implements IResourceProvider {
}
