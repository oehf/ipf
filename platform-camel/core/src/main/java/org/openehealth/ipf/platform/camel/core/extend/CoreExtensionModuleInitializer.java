package org.openehealth.ipf.platform.camel.core.extend;

import org.apache.camel.reifier.ProcessorReifier;
import org.apache.camel.reifier.dataformat.DataFormatReifier;
import org.openehealth.ipf.platform.camel.core.model.AuditDefinition;
import org.openehealth.ipf.platform.camel.core.model.DataFormatAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.ParserAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.RendererAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.SplitterDefinition;
import org.openehealth.ipf.platform.camel.core.model.TransmogrifierAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.reifier.AuditReifier;
import org.openehealth.ipf.platform.camel.core.reifier.DataFormatAdapterReifier;
import org.openehealth.ipf.platform.camel.core.reifier.ParserAdapterReifier;
import org.openehealth.ipf.platform.camel.core.reifier.RendererAdapterReifier;
import org.openehealth.ipf.platform.camel.core.reifier.SplitterReifier;
import org.openehealth.ipf.platform.camel.core.reifier.TransmogrifierAdapterReifier;
import org.openehealth.ipf.platform.camel.core.reifier.ValidatorAdapterReifier;

/**
 * This is called upon initialization of the Groovy API extensions for Camel.
 * As of 3.0, the Camel engine needs explicit registration of how the route
 * and dataformat definitions are translated into processors and dataformats
 *
 * @author Christian Ohr
 */
@SuppressWarnings("unused")
public class CoreExtensionModuleInitializer {

    public CoreExtensionModuleInitializer() {
        DataFormatReifier.registerReifier(DataFormatAdapterDefinition.class, DataFormatAdapterReifier::new);

        ProcessorReifier.registerReifier(ParserAdapterDefinition.class, ParserAdapterReifier::new);
        ProcessorReifier.registerReifier(RendererAdapterDefinition.class, RendererAdapterReifier::new);
        ProcessorReifier.registerReifier(SplitterDefinition.class, SplitterReifier::new);
        ProcessorReifier.registerReifier(TransmogrifierAdapterDefinition.class, TransmogrifierAdapterReifier::new);
        ProcessorReifier.registerReifier(ValidatorAdapterDefinition.class, ValidatorAdapterReifier::new);
        ProcessorReifier.registerReifier(AuditDefinition.class, AuditReifier::new);
    }
}
