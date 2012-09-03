/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v25.datatype.*;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.Escape;
import org.apache.commons.lang3.StringUtils;
import java.util.*;

/**
 * A renderer of HL7 v2 elements which considers XDS-specific
 * requirements regarding required and prohibited fields
 * as prescribed in the ITI TF Volume 3 Chapter 4.
 *
 * @author Dmytro Rud
 */
abstract public class XdsHl7v2Renderer {

    /**
     * Encoding characters for HL7 v2 messages.
     */
    public static final EncodingCharacters ENCODING_CHARACTERS =
            new EncodingCharacters('|', '^', '~', '\\', '&');

    /**
     * Numbers of fields of HL7 v2 composite types which are allowed to appear in XDS-specific
     * string representation.  Note that the numbers are ZERO-based!  When some type is not
     * mentioned here, its all sub-fields will be allowed.
     */
    private static final Map<Class<? extends Type>, Collection<Integer>> INCLUSIONS;

    private static void addInclusion(Class<? extends Type> typeClass, Integer... fieldNumbers) {
        INCLUSIONS.put(typeClass, Arrays.asList(fieldNumbers));
    }

    static {
        INCLUSIONS = new HashMap<Class<? extends Type>, Collection<Integer>>();
        addInclusion(CE.class,  0, 2);
        addInclusion(CX.class,  0, 3);
        addInclusion(HD.class,  1, 2);
        addInclusion(XON.class, 0, 5, 9);
        addInclusion(XTN.class, 2, 3);
    }


    private XdsHl7v2Renderer() {
        throw new IllegalStateException("cannot instantiate helper class");
    }


    /**
     * Encodes the given HL7 v2 composite field using XDS-specific
     * rules regarding required and prohibited sub-fields.
     * @param composite
     *      HL7 v2 composite field to be rendered.
     * @return
     *      String representation of the given HL7 v2 composite field.
     */
    public static String encode(Composite composite) {
        return encodeComposite(composite, ENCODING_CHARACTERS.getComponentSeparator());
    }


    protected static String encodeComposite(Composite composite, char delimiter) {
        StringBuilder sb = new StringBuilder();
        Type[] fields = composite.getComponents();
        Collection<Integer> inclusions = INCLUSIONS.get(composite.getClass());

        for (int i = 0; i < fields.length; ++i) {
            if ((inclusions == null) || inclusions.contains(i)) {
                if (fields[i] instanceof Composite) {
                    sb.append(encodeComposite((Composite) fields[i], ENCODING_CHARACTERS.getSubcomponentSeparator()));
                } else if (fields[i] instanceof Primitive) {
                    sb.append(encodePrimitive((Primitive) fields[i]));
                } else {
                    // actually, this line should be unreachable
                    throw new IllegalStateException("Don't know how to handle " + fields[i]);
                }
            }
            sb.append(delimiter);
        }

        return StringUtils.stripEnd(sb.toString(), String.valueOf(delimiter));
    }


    public static String encodePrimitive(Primitive p) {
        String value = p.getValue();
        return (value == null) ? "" : Escape.escape(value, ENCODING_CHARACTERS);
    }

}
