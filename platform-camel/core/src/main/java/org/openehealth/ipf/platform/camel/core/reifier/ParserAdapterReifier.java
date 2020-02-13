package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openehealth.ipf.platform.camel.core.adapter.ParserAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.model.ParserAdapterDefinition;

/**
 * @author Christian Ohr
 */
public class ParserAdapterReifier extends ProcessorAdapterReifier<ParserAdapterDefinition> {

    public ParserAdapterReifier(ProcessorDefinition<?> definition) {
        super((ParserAdapterDefinition) definition);
    }

    @Override
    protected ProcessorAdapter doCreateProcessor(RouteContext routeContext) {
        Parser<?> parser = definition.getParser();
        String parserBean = definition.getParserBean();
        if (parserBean != null) {
            parser = routeContext.lookup(parserBean, Parser.class);
        }
        return new ParserAdapter(parser);
    }
}
