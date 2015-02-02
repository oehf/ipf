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

import java.util.Map;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.Version;
import org.apache.camel.component.mina2.Mina2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;

/**
 * MLLP dispatching Camel component.
 * @author Dmytro Rud
 */
public class MllpDispatchComponent extends MllpComponent<MllpDispatchEndpointConfiguration> {
    public static final Hl7v2TransactionConfiguration CONFIGURATION =
            new Hl7v2TransactionConfiguration(
                    new Version[] {Version.V25}, // not relevant for acceptance checking
                    "MLLP Dispatcher",
                    "IPF",
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    new String[] {"\0"},        // acceptance checking will be delegated (4x)
                    new String[] {"\0"},
                    new String[] {"\0"},
                    new String[] {"\0"},
                    new boolean[] {false},      // audit trail (if any) will be delegated
                    new boolean[] {false},      // interactive continuation (if any) will be delegated
                    new DefaultHapiContext());

    private static final NakFactory NAK_FACTORY = new NakFactory(CONFIGURATION);

    @Override
    protected MllpDispatchEndpointConfiguration createConfig(Map<String, Object> parameters) throws Exception {
        return new MllpDispatchEndpointConfiguration(this, parameters);
    }

    @Override
    protected MllpEndpoint createEndpoint(Mina2Endpoint wrappedEndpoint, MllpDispatchEndpointConfiguration config) {
        return new MllpDispatchEndpoint(this, wrappedEndpoint, config);
    }

    @Override
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return CONFIGURATION;
    }

    @Override
    public NakFactory getNakFactory() {
        return NAK_FACTORY;
    }
}
