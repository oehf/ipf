/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.iti80.Iti80ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti80.Iti80PortType;
import org.openehealth.ipf.commons.ihe.xds.iti80.Iti80ServerAuditStrategy;

import javax.xml.namespace.QName;

import java.util.Arrays;
import java.util.List;

/**
 * @author Remco Overdevest
 * @since 3.3
 */
public class XCDR implements XdsIntegrationProfile {

	private static final XCDR Instance = new XCDR();

	@AllArgsConstructor
	public enum Interactions implements XdsInteractionId {
		ITI_80(ITI_80_WS_CONFIG);

		@Getter
		private final WsTransactionConfiguration<? extends XdsAuditDataset> wsTransactionConfiguration;

		@Override
		public XdsIntegrationProfile getInteractionProfile() {
			return Instance;
		}
	}

	@Override
	public HomeCommunityIdOptionality getHomeCommunityIdOptionality() {
		return HomeCommunityIdOptionality.ALWAYS;
	}

	@Override
	public List<InteractionId> getInteractionIds() {
		return Arrays.asList(Interactions.values());
	}

	private final static WsTransactionConfiguration<XdsSubmitAuditDataset> ITI_80_WS_CONFIG = new WsTransactionConfiguration<>(
		"xcdr-iti80",
		"Cross Gateway Provide Document",
		false,
		new Iti80ClientAuditStrategy(),
		new Iti80ServerAuditStrategy(),
		new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Service", "ihe"),
		Iti80PortType.class,
		new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Binding_Soap12", "ihe"),
		true,
		"wsdl/iti80.wsdl",
		true,
		false,
		false,
		false);
}
