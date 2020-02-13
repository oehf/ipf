package org.openehealth.ipf.platform.camel.cda.dataformat;

import org.apache.camel.CamelContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.reifier.dataformat.DataFormatReifier;
import org.apache.camel.spi.DataFormat;

/**
 * @author Christian Ohr
 */
public class MdhtDataFormatReifier extends DataFormatReifier<MdhtDataFormatDefinition> {

    public MdhtDataFormatReifier(DataFormatDefinition definition) {
        super((MdhtDataFormatDefinition) definition);
    }

    @Override
    protected void configureDataFormat(DataFormat dataFormat, CamelContext camelContext) {
    }

}
