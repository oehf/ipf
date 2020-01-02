package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.reifier.ProcessorReifier;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.TransmogrifierAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;
import org.openehealth.ipf.platform.camel.core.model.TransmogrifierAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;

/**
 * @author Christian Ohr
 */
public class ValidatorAdapterReifier extends ProcessorAdapterReifier<ValidatorAdapterDefinition> {

    static {
        ProcessorReifier.registerReifier(ValidatorAdapterReifier.class, ValidatorAdapterReifier::new);
    }

    public ValidatorAdapterReifier(ProcessorDefinition<?> definition) {
        super((ValidatorAdapterDefinition) definition);
    }

    @Override
    protected ProcessorAdapter doCreateProcessor(RouteContext routeContext) {
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
