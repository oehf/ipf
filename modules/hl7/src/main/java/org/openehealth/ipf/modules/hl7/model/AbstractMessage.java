package org.openehealth.ipf.modules.hl7.model;
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
import java.util.LinkedHashMap;
import java.util.Map;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.log.HapiLogFactory;

/**
 * 
 * @author Christian Ohr
 */
@SuppressWarnings("serial")
public abstract class AbstractMessage extends
		ca.uhn.hl7v2.model.AbstractMessage {

	protected enum Cardinality {
        REQUIRED, OPTIONAL, REQUIRED_REPEATING, OPTIONAL_REPEATING;
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
                    new LinkedHashMap<Class<? extends Structure>, Cardinality>())
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
            HapiLogFactory.getHapiLog(getClass()).error(
                    "Unexpected error creating structures for "
                            + getClass().getName(), e);
            throw new RuntimeException(e);
        }
    }

    protected <T extends Structure> T get(Class<T> structureClass) {
        return get(structureClass.getSimpleName(), structureClass);
    }

    @SuppressWarnings("unchecked")
    protected <T extends Structure> T get(String structure, Class<T> structureClass) {
        try {
            return (T) get(structure);
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error(
                    "Error accessing structure " + structure, e);
            throw new RuntimeException(e);
        }
    }

    protected <T extends Structure> T get(Class<T> structureClass, int rep) {
        return get(structureClass.getSimpleName(), structureClass, rep);
    }

    @SuppressWarnings("unchecked")
    protected <T extends Structure> T get(String structure, Class<T> structureClass, int rep) {
        try {
            return (T) get(structure, rep);
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error(
                    "Error accessing structure " + structure, e);
            throw new RuntimeException(e);
        }
    }

    protected <T extends Structure> int getReps(Class<T> structureClass) {
        return getReps(structureClass.getSimpleName(), structureClass);
    }

    protected <T extends Structure> int getReps(String structure, Class<T> structureClass) {
        try {
            return getAll(structure).length;
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error(
                    "Error accessing structure " + structure, e);
            throw new RuntimeException(e);
        }
    }

    abstract protected Map<Class<? extends Structure>, Cardinality> structures(
            Map<Class<? extends Structure>, Cardinality> structures);

}
