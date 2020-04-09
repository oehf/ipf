package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.Route;
import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.TransmogrifierAdapter;
import org.openehealth.ipf.platform.camel.core.model.TransmogrifierAdapterDefinition;

/**
 * @author Christian Ohr
 */
public class TransmogrifierAdapterReifier extends ProcessorAdapterReifier<TransmogrifierAdapterDefinition> {

    public TransmogrifierAdapterReifier(Route route, ProcessorDefinition<?> definition) {
        super(route, (TransmogrifierAdapterDefinition) definition);
    }

    @Override
    protected ProcessorAdapter doCreateProcessor() {
        Transmogrifier<?, ?> transmogrifier = definition.getTransmogrifier();
        String transmogrifierBean = definition.getTransmogrifierBean();
        if (transmogrifierBean != null) {
            transmogrifier = camelContext.getRegistry().lookupByNameAndType(transmogrifierBean, Transmogrifier.class);
        }
        return new TransmogrifierAdapter(transmogrifier);
    }
}
