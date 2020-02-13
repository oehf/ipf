package org.openehealth.ipf.tutorials.xds;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.reifier.ProcessorReifier;
import org.apache.camel.spi.RouteContext;

/**
 * @author Christian Ohr
 */
public class SearchReifier extends ProcessorReifier<SearchDefinition> {

    SearchReifier(ProcessorDefinition<?> definition) {
        super((SearchDefinition) definition);
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        SearchProcessor searchProcessor = new SearchProcessor();
        searchProcessor.setIndexEvals(definition.getIndexEvals());
        searchProcessor.setFilters(definition.getFilters());
        searchProcessor.setResultTypes(definition.getResultTypes());
        searchProcessor.setResultField(definition.getResultField());
        searchProcessor.setStore(routeContext.lookup("dataStore", DataStore.class));
        searchProcessor.setProcessor(createChildProcessor(routeContext, false));
        return searchProcessor;
    }
}
