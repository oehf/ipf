package org.openehealth.ipf.platform.camel.hl7.reifier;

import org.apache.camel.Route;
import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.reifier.ProcessorAdapterReifier;
import org.openehealth.ipf.platform.camel.hl7.model.HapiAdapterDefinition;

/**
 * @author Christian Ohr
 */
public class HapiAdapterReifier extends ProcessorAdapterReifier<HapiAdapterDefinition> {

    public HapiAdapterReifier(Route route, ProcessorDefinition<?> definition) {
        super(route, (HapiAdapterDefinition) definition);
    }

    @Override
    protected ProcessorAdapter doCreateProcessor() {
        return definition.getAdapter();
    }
}
