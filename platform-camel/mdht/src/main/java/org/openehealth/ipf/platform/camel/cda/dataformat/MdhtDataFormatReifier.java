package org.openehealth.ipf.platform.camel.cda.dataformat;

import org.apache.camel.CamelContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.reifier.dataformat.DataFormatReifier;

import java.util.Map;

/**
 * @author Christian Ohr
 */
public class MdhtDataFormatReifier extends DataFormatReifier<MdhtDataFormatDefinition> {

    public MdhtDataFormatReifier(CamelContext camelContext, DataFormatDefinition definition) {
        super(camelContext, (MdhtDataFormatDefinition) definition);
    }

    @Override
    protected void prepareDataFormatConfig(Map<String, Object> properties) {
    }
}
