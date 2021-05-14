package org.openehealth.ipf.commons.ihe.svs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsAuditDataset;
import org.openehealth.ipf.commons.ihe.svs.iti48.Iti48AuditStrategy;
import org.openehealth.ipf.commons.ihe.svs.iti48.Iti48PortType;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Definitions for the Community Medication Prescription and Dispense (SVS) integration profile of the PHARM TF.
 *
 * @author Quentin Ligier
 */
public class SVS implements IntegrationProfile {

    private static final SVS Instance = new SVS();

    @AllArgsConstructor
    public enum Interactions implements WsInteractionId<WsTransactionConfiguration<SvsAuditDataset>> {
        ITI_48(ITI_48_WS_CONFIG);

        @Getter
        private final WsTransactionConfiguration<SvsAuditDataset> wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final WsTransactionConfiguration<SvsAuditDataset> ITI_48_WS_CONFIG = new WsTransactionConfiguration<>(
            "svs-iti48",
            "Retrieve Value Set",
            true,
            new Iti48AuditStrategy(false),
            new Iti48AuditStrategy(true),
            new QName("urn:ihe:iti:svs:2008", "ValueSetRepository_Service", "ihe"),
            Iti48PortType.class,
            new QName("urn:ihe:iti:svs:2008", "ValueSetRepository_Binding_Soap12", "ihe"),
            false, // TODO MTOM?
            "wsdl/iti48/iti48.wsdl",
            true,
            false,
            false, // TODO ?
            false
    );
}
