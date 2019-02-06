/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.pcc44;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.IQuery;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.Constants;

import java.util.Map;

/**
 * Request Factory for PCC-44 requests returning a bundle of resources that were queried
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class Pcc44ClientRequestFactory implements ClientRequestFactory<IQuery<Bundle>> {

    @Override
    public IClientExecutable<IQuery<Bundle>, ?> getClientExecutable(IGenericClient client, Object requestData, Map<String, Object> parameters) {
        IQuery<IBaseBundle> query;
        String queriedResourceType = (String)parameters.get(Constants.FHIR_RESOURCE_TYPE_HEADER);
        if (requestData instanceof ICriterion) {
            query = client.search()
                    .forResource(queriedResourceType)
                    .where((ICriterion<?>) requestData);
        } else {
            query = client.search()
                    .byUrl(requestData.toString());
        }
        if (parameters.containsKey(Constants.FHIR_COUNT)) {
            query.count(Integer.parseInt(parameters.get(Constants.FHIR_COUNT).toString()));
        }
        return query.returnBundle(Bundle.class);
    }

}
