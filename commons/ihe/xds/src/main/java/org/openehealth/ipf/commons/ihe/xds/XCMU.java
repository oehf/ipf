/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.chxcmu.ChXcmuAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.chxcmu.ChXcmuPortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmytro Rud
 * @since 3.4
 */
public class XCMU implements XdsIntegrationProfile {

	private static final XCMU Instance = new XCMU();

	@AllArgsConstructor
	public enum Interactions implements XdsInteractionId {
		CH_XCMU(CH_XCMU_WS_CONFIG);

		@Getter
		private WsTransactionConfiguration wsTransactionConfiguration;

		@Override
		public XdsIntegrationProfile getInteractionProfile() {
			return Instance;
		}
	}

	@Override
	public boolean isEbXml30Based() {
		return true;
	}

	@Override
	public boolean requiresHomeCommunityId() {
		return true;
	}

	@Override
	public List<InteractionId> getInteractionIds() {
		return Arrays.asList(Interactions.values());
	}

	private final static WsTransactionConfiguration CH_XCMU_WS_CONFIG = new WsTransactionConfiguration(
		"ch-xcmu",
		"Cross Gateway Update Document Set",
		false,
		new ChXcmuAuditStrategy(false),
		new ChXcmuAuditStrategy(true),
		new QName("urn:ihe:iti:xcmu:2017", "RespondingGateway_Service", "ihe"),
		ChXcmuPortType.class,
		new QName("urn:ihe:iti:xcmu:2017", "RespondingGateway_Binding_Soap12", "ihe"),
		false,
		"wsdl/ch-xcmu.wsdl",
		true,
		false,
		false,
		false);
}
