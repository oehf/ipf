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
import org.openehealth.ipf.commons.ihe.xds.rmux1.RmuX1ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rmux1.RmuX1PortType;
import org.openehealth.ipf.commons.ihe.xds.rmux1.RmuX1ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * RMU = Restricted Metadata Update.
 *
 * @author Dmytro Rud
 * @since 3.4
 */
public class RMU implements XdsIntegrationProfile {

	private static final RMU Instance = new RMU();

	@AllArgsConstructor
	public enum Interactions implements XdsInteractionId {
		RMU_X1(RMU_X1_WS_CONFIG);

		@Getter
		private WsTransactionConfiguration<? extends XdsAuditDataset> wsTransactionConfiguration;

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

	private final static WsTransactionConfiguration<XdsSubmitAuditDataset> RMU_X1_WS_CONFIG = new WsTransactionConfiguration<>(
		"rmu-itiX1",
		"Restricted Update Document Set",
		false,
		new RmuX1ClientAuditStrategy(),
		new RmuX1ServerAuditStrategy(),
		new QName("urn:ihe:iti:rmu:2018", "UpdateResponder_Service", "ihe"),
		RmuX1PortType.class,
		new QName("urn:ihe:iti:rmu:2018", "UpdateResponder_Binding_Soap12", "ihe"),
		false,
		"wsdl/rmu-itiX1.wsdl",
		true,
		false,
		false,
		false);
}
