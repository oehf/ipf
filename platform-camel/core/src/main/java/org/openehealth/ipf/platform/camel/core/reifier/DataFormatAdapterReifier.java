package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.CamelContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.reifier.dataformat.DataFormatReifier;
import org.apache.camel.spi.DataFormat;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.platform.camel.core.adapter.DataFormatAdapter;
import org.openehealth.ipf.platform.camel.core.model.DataFormatAdapterDefinition;

/**
 * @author Christian Ohr
 */
public class DataFormatAdapterReifier extends DataFormatReifier<DataFormatAdapterDefinition> {

    public DataFormatAdapterReifier(DataFormatDefinition definition) {
        super((DataFormatAdapterDefinition) definition);
    }

    @Override
    protected DataFormat doCreateDataFormat(CamelContext camelContext) {
            if (definition.getParserBeanName() != null) {
                return new DataFormatAdapter(camelContext.getRegistry()
                        .lookupByNameAndType(definition.getParserBeanName(), Parser.class));
            } else {
                return new DataFormatAdapter(camelContext.getRegistry()
                        .lookupByNameAndType(definition.getRendererBeanName(), Renderer.class));
            }
    }
}
