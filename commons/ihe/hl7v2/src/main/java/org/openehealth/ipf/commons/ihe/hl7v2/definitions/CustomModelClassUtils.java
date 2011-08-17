/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions;

import java.util.Collections;
import java.util.Map;

import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.openehealth.ipf.modules.hl7.parser.PipeParser;

import ca.uhn.hl7v2.parser.Parser;

/**
 * Utilities to work with custom HAPI class definitions.
 * @author Dmytro Rud
 */
public class CustomModelClassUtils {

    private CustomModelClassUtils() {
        throw new IllegalStateException("Utility class, cannot instantiate");
    }

    /**
     * Creates a class factory for the given transaction and HL7 version.
     */
    public static CustomModelClassFactory createFactory(String transaction, String version) {
        String packageName = new StringBuilder()
            .append(CustomModelClassUtils.class.getPackage().getName())
            .append(".v")
            .append(version.replace(".", ""))
            .append('.')
            .append(transaction)
            .toString();
        Map<String, String[]> map = Collections.singletonMap(version, new String[] {packageName});
        return new CustomModelClassFactory(map);
    }
    
    /**
     * Creates a parser for the given transaction and HL7 version.
     */
    public static Parser createParser(String transaction, String version) {
        CustomModelClassFactory factory = createFactory(transaction, version);
        return new PipeParser(factory);
    }
}
