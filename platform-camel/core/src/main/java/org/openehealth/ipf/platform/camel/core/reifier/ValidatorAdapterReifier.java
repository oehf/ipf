package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;

/**
 * @author Christian Ohr
 */
public class ValidatorAdapterReifier extends ProcessorAdapterReifier<ValidatorAdapterDefinition> {

    public ValidatorAdapterReifier(RouteContext routeContext, ProcessorDefinition<?> definition) {
        super(routeContext, (ValidatorAdapterDefinition) definition);
    }

    @Override
    protected ProcessorAdapter doCreateProcessor() {
        Validator<?, ?> validator = definition.getValidator();
        if (definition.getValidatorBean() != null) {
            validator = routeContext.lookup(definition.getValidatorBean(), Validator.class);
        }
        ValidatorAdapter adapter = new ValidatorAdapter(validator);
        if (definition.getProfile() != null) {
            return adapter.staticProfile(definition.getProfile());
        }
        if (definition.getProfileExpression() != null) {
            return adapter.profile(definition.getProfileExpression());
        }
        return adapter;
    }
}
