package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.core.model.AuditDefinition;

/**
 * @author Christian Ohr
 */
public class AuditReifier extends DelegateReifier<AuditDefinition> {

    public AuditReifier(RouteContext routeContext, ProcessorDefinition<?> definition) {
        super(routeContext, (AuditDefinition) definition);
    }

    @Override
    protected Processor doCreateDelegate() {
        if (definition.getAuditProcessorBeanName() != null) {
            definition.setAuditProcessor(
                    routeContext.lookup(definition.getAuditProcessorBeanName(), Processor.class));
        }
        return definition.getAuditProcessor();
    }
}
