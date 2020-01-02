/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.modules.hl7.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openehealth.ipf.modules.hl7.HL7v2Exception;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * Convenience subclass of {@link ca.uhn.hl7v2.model.AbstractMessage} used for custom message structures
 * as e.g. defined by IHE.
 *
 * @author Christian Ohr
 */
@SuppressWarnings("serial")
public abstract class AbstractMessage extends ca.uhn.hl7v2.model.AbstractMessage {

	protected enum Cardinality {
        REQUIRED, OPTIONAL, REQUIRED_REPEATING, OPTIONAL_REPEATING
    }

    public AbstractMessage() {
        super(new DefaultModelClassFactory());
        init();
    }

    public AbstractMessage(ModelClassFactory factory) {
        super(factory);
        init();
    }

    private void init() {
        try {
            for (Map.Entry<Class<? extends Structure>, Cardinality> structure : structures(
                    new LinkedHashMap<>())
                    .entrySet()) {
                switch (structure.getValue()) {
                case REQUIRED:
                    add(structure.getKey(), true, false);
                    break;
                case REQUIRED_REPEATING:
                    add(structure.getKey(), true, true);
                    break;
                case OPTIONAL:
                    add(structure.getKey(), false, false);
                    break;
                case OPTIONAL_REPEATING:
                    add(structure.getKey(), false, true);
                    break;
                default:
                    // does not happen
                    break;
                }

            }
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }
    }


    /**
     * @deprecated use {@link ca.uhn.hl7v2.model.AbstractMessage#getTyped(String, Class)}
     */
    @Deprecated(forRemoval = true)
    protected <T extends Structure> T get(Class<T> structureClass) {
        return super.getTyped(structureClass.getSimpleName(), structureClass);
    }

    /**
     * @deprecated use {@link ca.uhn.hl7v2.model.AbstractMessage#getTyped(String, Class)}
     */
    @Deprecated(forRemoval = true)
    protected <T extends Structure> T get(String structure, Class<T> structureClass) {
        return super.getTyped(structure, structureClass);
    }

    /**
     * @deprecated use {@link ca.uhn.hl7v2.model.AbstractMessage#getTyped(String, int, Class)}
     */
    @Deprecated(forRemoval = true)
    protected <T extends Structure> T get(Class<T> structureClass, int rep) {
        return super.getTyped(structureClass.getSimpleName(), rep, structureClass);
    }

    /**
     * @deprecated use {@link ca.uhn.hl7v2.model.AbstractMessage#getTyped(String, int, Class)}
     */
    @Deprecated(forRemoval = true)
    protected <T extends Structure> T get(String structure, Class<T> structureClass, int rep) {
    	return super.getTyped(structure, rep, structureClass);
    }

    /**
     * @deprecated use {@link ca.uhn.hl7v2.model.AbstractGroup#getReps(String)}
     */
    @Deprecated(forRemoval = true)
    protected <T extends Structure> int getReps(Class<T> structureClass) {
        return super.getReps(structureClass.getSimpleName());
    }

    /**
     * @deprecated use {@link ca.uhn.hl7v2.model.AbstractGroup#getReps(String)}
     */
    @Deprecated(forRemoval = true)
    protected <T extends Structure> int getReps(String structure, Class<T> structureClass) {
        return super.getReps(structure);
    }

    abstract protected Map<Class<? extends Structure>, Cardinality> structures(
            Map<Class<? extends Structure>, Cardinality> structures);

}
