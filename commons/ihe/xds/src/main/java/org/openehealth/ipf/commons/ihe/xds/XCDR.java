package org.openehealth.ipf.commons.ihe.xds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.iti80.Iti80ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti80.Iti80PortType;
import org.openehealth.ipf.commons.ihe.xds.iti80.Iti80ServerAuditStrategy;

import javax.xml.namespace.QName;

import java.util.List;

/**
 * Created by remcoo on 05/04/2017.
 */
public class XCDR implements XdsIntegrationProfile {

	private static final XCDR Instance = new XCDR();

	@AllArgsConstructor
	public enum Interactions implements XdsInteractionId {
		ITI_80(ITI_80_WS_CONFIG);

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
		return null;
	}

	private final static WsTransactionConfiguration ITI_80_WS_CONFIG = new WsTransactionConfiguration(
		"xca-iti80",
		"Cross Gateway Provide Document",
		true,
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
		true);
}
