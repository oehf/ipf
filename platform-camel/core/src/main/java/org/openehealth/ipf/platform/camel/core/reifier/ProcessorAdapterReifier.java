package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.model.ProcessorAdapterDefinition;

/**
 * @author Christian Ohr
 */
public abstract class ProcessorAdapterReifier<T extends ProcessorAdapterDefinition> extends DelegateReifier<T> {

    public ProcessorAdapterReifier(Route route, T definition) {
        super(route, definition);
    }

    @Override
    protected Processor doCreateDelegate() {
        var adapter = doCreateProcessor();
        if (definition.getInputExpression() != null) {
            adapter.input(definition.getInputExpression());
        }
        if (definition.getParamsExpression() != null) {
            adapter.params(definition.getParamsExpression());
        }
        return adapter;
    }

    protected abstract ProcessorAdapter doCreateProcessor();
}
