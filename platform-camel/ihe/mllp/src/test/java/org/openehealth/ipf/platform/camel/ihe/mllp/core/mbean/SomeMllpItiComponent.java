package org.openehealth.ipf.platform.camel.ihe.mllp.core.mbean;

import org.openehealth.ipf.modules.hl7.parser.PipeParser;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpComponent;

public class SomeMllpItiComponent extends MllpComponent {
    
    public static final Hl7v2TransactionConfiguration CONFIGURATION =
        new Hl7v2TransactionConfiguration(
                "2.x",
                "Some MLLP adapter",
                "IPF-Test",
                207,
                207,
                new String[] { "ADT" },
                new String[] { "A01 A04" },
                new String[] { "ACK" },
                new String[] { "*" },
                new boolean[] { true },
                new boolean[] { false },
                new PipeParser());
    
    private static final NakFactory NAK_FACTORY = new NakFactory(CONFIGURATION);

    @Override
    public NakFactory getNakFactory() {
        return NAK_FACTORY;
    }
    
    @Override
    public MllpAuditStrategy getClientAuditStrategy() {
        return null;
    }

    @Override
    public MllpAuditStrategy getServerAuditStrategy() {
        return null;
    }
    
    @Override
    public Hl7v2TransactionConfiguration getTransactionConfiguration() {
        return CONFIGURATION;
    }
    
}
