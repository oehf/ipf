package org.openehealth.ipf.modules.hl7.parser;

import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

import java.util.Map;

/**
 * CustomModelClassFactory implementation that exposes the delegate ModelClassFactory
 */
public class CustomModelClassFactory extends ca.uhn.hl7v2.parser.CustomModelClassFactory {

    private final ModelClassFactory defaultFactory;

    public CustomModelClassFactory() {
        this(null);
    }

    public CustomModelClassFactory(Map<String, String[]> map) {
        this(new DefaultModelClassFactory(), map);
    }

    public CustomModelClassFactory(String packageName, ModelClassFactory defaultFactory) {
        super(packageName);
        this.defaultFactory = defaultFactory;
    }

    public CustomModelClassFactory(ModelClassFactory defaultFactory, Map<String, String[]> map) {
        super(defaultFactory, map);
        this.defaultFactory = defaultFactory;
    }

    public ModelClassFactory getDelegate() {
        return defaultFactory;
    }

}
