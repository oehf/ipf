/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditDataset;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.stripToNull;

/**
 * Configuration of a dispatching MLLP endpoint.
 * @author Dmytro Rud
 */
public class MllpDispatchEndpointConfiguration extends MllpEndpointConfiguration {

    @Getter private final String[] routes;


    protected MllpDispatchEndpointConfiguration(MllpComponent<MllpDispatchEndpointConfiguration, MllpAuditDataset> component, String uri, Map<String, Object> parameters) {
        super(component, uri, parameters);
        var routesString = stripToNull(component.getAndRemoveParameter(parameters, "routes", String.class));
        routes = routesString != null ? routesString.split("\\s*,\\s*") : new String[0];
    }

}
