package org.openehealth.ipf.modules.hl7.parser;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Version;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * CustomModelClassFactory implementation that fixes a HAPI 2.2 bug and adds caching.
 * It is strongly recommended to use this class instead of {@link ca.uhn.hl7v2.parser.CustomModelClassFactory}
 */
public class CustomModelClassFactory extends ca.uhn.hl7v2.parser.CustomModelClassFactory {

    private ModelClassFactory defaultFactory;
    private final ConcurrentMap<String, Class<?>> classMap = new ConcurrentHashMap<>();

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

    @Override
    public Class<? extends Message> getMessageClass(String name, String version, boolean isExplicit) throws HL7Exception {
        String key = "message" + name + version;
        Class<? extends Message> clazz = (Class<? extends Message>)classMap.get(key);
        if (clazz == null) {
            clazz = super.getMessageClass(name, version, isExplicit);
            if (clazz != null) classMap.putIfAbsent(key, clazz);
        }
        return clazz;
    }

    @Override
    public Class<? extends Group> getGroupClass(String name, String version) throws HL7Exception {
        String key = "group" + name + version;
        Class<? extends Group> clazz = (Class<? extends Group>)classMap.get(key);
        if (clazz == null) {
            clazz = super.getGroupClass(name, version);
            if (clazz != null) classMap.putIfAbsent(key, clazz);
        }
        return clazz;
    }

    @Override
    public Class<? extends Segment> getSegmentClass(String name, String version) throws HL7Exception {
        String key = "segment" + name + version;
        Class<? extends Segment> clazz = (Class<? extends Segment>)classMap.get(key);
        if (clazz == null) {
            clazz = super.getSegmentClass(name, version);
            if (clazz != null) classMap.putIfAbsent(key, clazz);
        }
        return clazz;
    }

    @Override
    public Class<? extends Type> getTypeClass(String name, String version) throws HL7Exception {
        String key = "type" + name + version;
        Class<? extends Type> clazz = (Class<? extends Type>)classMap.get(key);
        if (clazz == null) {
            clazz = super.getTypeClass(name, version);
            if (clazz != null) classMap.putIfAbsent(key, clazz);
        }
        return clazz;
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
