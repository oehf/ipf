package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.Route;
import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openehealth.ipf.platform.camel.core.adapter.ParserAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.model.ParserAdapterDefinition;

/**
 * @author Christian Ohr
 */
public class ParserAdapterReifier extends ProcessorAdapterReifier<ParserAdapterDefinition> {

    public ParserAdapterReifier(Route route, ProcessorDefinition<?> definition) {
        super(route, (ParserAdapterDefinition) definition);
    }

    @Override
    protected ProcessorAdapter doCreateProcessor() {
        Parser<?> parser = definition.getParser();
        String parserBean = definition.getParserBean();
        if (parserBean != null) {
            parser = camelContext.getRegistry().lookupByNameAndType(parserBean, Parser.class);
        }
        return new ParserAdapter(parser);
    }
}
