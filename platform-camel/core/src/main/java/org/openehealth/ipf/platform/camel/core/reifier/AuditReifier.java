package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.platform.camel.core.model.AuditDefinition;

/**
 * @author Christian Ohr
 */
public class AuditReifier extends DelegateReifier<AuditDefinition> {

    public AuditReifier(Route route, ProcessorDefinition<?> definition) {
        super(route, (AuditDefinition) definition);
    }

    @Override
    protected Processor doCreateDelegate() {
        if (definition.getAuditProcessorBeanName() != null) {
            definition.setAuditProcessor(
                    camelContext.getRegistry().lookupByNameAndType(definition.getAuditProcessorBeanName(), Processor.class));
        }
        return definition.getAuditProcessor();
    }
}
