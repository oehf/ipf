package org.openehealth.ipf.platform.camel.cda.extend;

import org.apache.camel.reifier.dataformat.DataFormatReifier;
import org.openehealth.ipf.platform.camel.cda.dataformat.MdhtDataFormatDefinition;
import org.openehealth.ipf.platform.camel.cda.dataformat.MdhtDataFormatReifier;

/**
 * This is called upon initialization of the Groovy API extensions for Camel.
 * As of 3.0, the Camel engine needs explicit registration of how the route
 * and dataformat definitions are translated into processors and dataformats
 *
 * @DSL
 *
 * @author Christian Ohr
 */
@SuppressWarnings("unused")
public class MdhtExtensionModuleInitializer {

    public MdhtExtensionModuleInitializer() {
        DataFormatReifier.registerReifier(MdhtDataFormatDefinition.class, MdhtDataFormatReifier::new);
    }
}
