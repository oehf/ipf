package org.openehealth.ipf.modules.hl7.parser;

import java.util.List;
import java.util.Map;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Version;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * CustomModelClassFactory implementation that fixes a HAPI 2.2 bug
 */
public class CustomModelClassFactory extends ca.uhn.hl7v2.parser.CustomModelClassFactory {

    private ModelClassFactory defaultFactory;

    public CustomModelClassFactory() {
        this((Map<String, String[]>)null);
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

    /**
     * Looks up its own event map. If no structure was found, the call is delegated to
     * the default ModelClassFactory. If nothing can be found, the eventName is returned
     * as structure. Fixes bug #213 in HAPI 2.2
     *
     * @see ca.uhn.hl7v2.parser.AbstractModelClassFactory#getMessageStructureForEvent(java.lang.String, ca.uhn.hl7v2.Version)
     */
    @Override
    public String getMessageStructureForEvent(String eventName, Version version) throws HL7Exception {
        Map<String, String> p = getEventMapForVersion(version);
        if (p == null)
            throw new HL7Exception("No map found for version " + version);
        String structure = p.get(eventName);
        if (structure == null) {
            structure = defaultFactory.getMessageStructureForEvent(eventName, version);
        }
        if (structure == null) {
            structure = eventName;
        }
        return structure;
    }

    public ModelClassFactory getDelegate() {
        return defaultFactory;
    }
}
