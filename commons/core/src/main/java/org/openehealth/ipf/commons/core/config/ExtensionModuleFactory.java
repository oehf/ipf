package org.openehealth.ipf.commons.core.config;

import java.util.Properties;

import groovy.lang.MetaMethod;
import org.codehaus.groovy.runtime.m12n.ExtensionModule;
import org.codehaus.groovy.runtime.m12n.MetaInfExtensionModule;
import org.codehaus.groovy.runtime.m12n.PropertiesModuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension module that wraps the registration inside logging
 * statements so that the extension process can be observed if necessary.
 *
 * In order to load the extensions with this class,
 * the extension module descriptor must contain a corresponding line:
 * <p>
 * moduleFactory=org.openehealth.ipf.commons.core.config.ExtensionModuleFactory
 * </p>
 */
public class ExtensionModuleFactory extends PropertiesModuleFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionModuleFactory.class);

    public ExtensionModuleFactory() {
    }

    @Override
    public ExtensionModule newModule(Properties properties, ClassLoader classLoader) {
        LOG.info("Registering new extension module {} defined in class {}",
                properties.getProperty(MODULE_NAME_KEY),
                properties.getProperty(MetaInfExtensionModule.MODULE_INSTANCE_CLASSES_KEY));
        ExtensionModule module = MetaInfExtensionModule.newModule(properties, classLoader);
        if (LOG.isDebugEnabled()) {
            for(MetaMethod method : module.getMetaMethods()) {
                LOG.debug("registered method: {}", method);
            }
        }
        return module;
    }
}
