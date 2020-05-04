package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.Route;
import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.RendererAdapter;
import org.openehealth.ipf.platform.camel.core.model.RendererAdapterDefinition;

/**
 * @author Christian Ohr
 */
public class RendererAdapterReifier extends ProcessorAdapterReifier<RendererAdapterDefinition> {

    public RendererAdapterReifier(Route route, ProcessorDefinition<?> definition) {
        super(route, (RendererAdapterDefinition) definition);
    }


    @Override
    protected ProcessorAdapter doCreateProcessor() {
        var renderer = definition.getRenderer();
        var rendererBean = definition.getRendererBean();
        if (rendererBean != null) {
            renderer = camelContext.getRegistry().lookupByNameAndType(rendererBean, Renderer.class);
        }
        return new RendererAdapter(renderer);
    }
}
