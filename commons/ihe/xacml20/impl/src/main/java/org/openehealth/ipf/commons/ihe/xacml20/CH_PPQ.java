package org.openehealth.ipf.commons.ihe.xacml20;

import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqPortType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqAuditDataset;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmytro Rud
 */
public class CH_PPQ implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements WsInteractionId<WsTransactionConfiguration<ChPpqAuditDataset>> {
        CH_PPQ(CH_PPQ_WS_CONFIG);

        @Getter private WsTransactionConfiguration wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration<ChPpqAuditDataset> CH_PPQ_WS_CONFIG = new WsTransactionConfiguration<>(
            "ch-ppq",
            "Privacy Policy Query",
            false,
            null, // new ChPpqAuditStrategy(false),
            null, // new ChPpqAuditStrategy(true),
            new QName("urn:ihe:iti:ppq:2016", "PolicyRepository_Service"),
            ChPpqPortType.class,
            new QName("urn:ihe:iti:ppq:2016", "PolicyRepository_Binding_Soap12"),
            false,
            "wsdl/ch-ppq.wsdl",
            true,
            false,
            true,
            false);

}
