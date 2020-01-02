package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.CamelContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.reifier.ProcessorReifier;
import org.apache.camel.reifier.dataformat.DataFormatReifier;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.platform.camel.core.adapter.DataFormatAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.RendererAdapter;
import org.openehealth.ipf.platform.camel.core.model.DataFormatAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.RendererAdapterDefinition;

/**
 * @author Christian Ohr
 *
 * FIXME
 */
public class DataFormatAdapterReifier extends DataFormatReifier<DataFormatAdapterDefinition> {

    static {
        DataFormatReifier.registerReifier(DataFormatAdapterDefinition.class, DataFormatAdapterReifier::new);
    }

    public DataFormatAdapterReifier(DataFormatDefinition definition) {
        super((DataFormatAdapterDefinition) definition);
    }

    @Override
    protected void configureDataFormat(DataFormat dataFormat, CamelContext camelContext) {
    }


}
