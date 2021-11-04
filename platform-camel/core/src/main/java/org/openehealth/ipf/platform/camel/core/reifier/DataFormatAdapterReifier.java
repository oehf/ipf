package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.CamelContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.reifier.dataformat.DataFormatReifier;
import org.apache.camel.spi.DataFormat;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.platform.camel.core.adapter.DataFormatAdapter;
import org.openehealth.ipf.platform.camel.core.model.DataFormatAdapterDefinition;

import java.util.Map;

/**
 * @author Christian Ohr
 */
public class DataFormatAdapterReifier extends DataFormatReifier<DataFormatAdapterDefinition> {

    public DataFormatAdapterReifier(CamelContext camelContext, DataFormatDefinition definition) {
        super(camelContext, (DataFormatAdapterDefinition) definition);
    }

    @Override
    protected DataFormat doCreateDataFormat() {
            if (definition.getParserBeanName() != null) {
                return new DataFormatAdapter(camelContext.getRegistry()
                        .lookupByNameAndType(definition.getParserBeanName(), Parser.class));
            } else {
                return new DataFormatAdapter(camelContext.getRegistry()
                        .lookupByNameAndType(definition.getRendererBeanName(), Renderer.class));
            }
    }

    @Override
    protected void prepareDataFormatConfig(Map<String, Object> properties) {
    }
}
