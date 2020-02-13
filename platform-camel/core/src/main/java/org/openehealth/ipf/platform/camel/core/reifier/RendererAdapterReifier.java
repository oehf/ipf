package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.RendererAdapter;
import org.openehealth.ipf.platform.camel.core.model.RendererAdapterDefinition;

/**
 * @author Christian Ohr
 */
public class RendererAdapterReifier extends ProcessorAdapterReifier<RendererAdapterDefinition> {

    public RendererAdapterReifier(ProcessorDefinition<?> definition) {
        super((RendererAdapterDefinition) definition);
    }


    @Override
    protected ProcessorAdapter doCreateProcessor(RouteContext routeContext) {
        Renderer<?> renderer = definition.getRenderer();
        String rendererBean = definition.getRendererBean();
        if (rendererBean != null) {
            renderer = routeContext.lookup(rendererBean, Renderer.class);
        }
        return new RendererAdapter(renderer);
    }
}
