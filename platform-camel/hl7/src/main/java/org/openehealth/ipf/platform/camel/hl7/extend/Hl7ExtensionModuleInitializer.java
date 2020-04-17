package org.openehealth.ipf.platform.camel.hl7.extend;

import org.apache.camel.reifier.ProcessorReifier;
import org.openehealth.ipf.platform.camel.hl7.model.HapiAdapterDefinition;
import org.openehealth.ipf.platform.camel.hl7.reifier.HapiAdapterReifier;

/**
 * This is called upon initialization of the Groovy API extensions for Camel.
 * As of 3.0, the Camel engine needs explicit registration of how the route
 * definitions are translated into processors.
 *
 * @author Christian Ohr
 */
@SuppressWarnings("unused")
public class Hl7ExtensionModuleInitializer {

    public Hl7ExtensionModuleInitializer() {
        ProcessorReifier.registerReifier(HapiAdapterDefinition.class, HapiAdapterReifier::new);
    }
}
