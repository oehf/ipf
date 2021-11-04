package org.openehealth.ipf.tutorials.xds;

import org.apache.camel.reifier.ProcessorReifier;

/**
 * @author Christian Ohr
 */
public class RegRepModelExtensionInitializer {

    public RegRepModelExtensionInitializer() {
        ProcessorReifier.registerReifier(SearchDefinition.class, SearchReifier::new);
    }
}
