package org.openehealth.ipf.commons.ihe.svs;

import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.svs.iti48.Iti48PortType;
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


    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration<> ITI_48_WS_CONFIG = new WsTransactionConfiguration<>(
            "svs-iti48",
            "Retrieve Value Set",
            true,
            null, // TODO
            null, // TODO
            new QName("urn:ihe:iti:svs:2008", "ValueSetRepository_Service", "ihe"),
            Iti48PortType.class,
            new QName("urn:ihe:iti:svs:2008", "ValueSetRepository_Binding_Soap12", "ihe"),
            false, // TODO MTOM?
            "wsdl/iti48.wsdl",
            true,
            false,
            false, // TODO ?
            false
    );
}
