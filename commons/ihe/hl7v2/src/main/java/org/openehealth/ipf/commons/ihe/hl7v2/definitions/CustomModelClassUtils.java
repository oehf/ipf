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

import ca.uhn.hl7v2.Version;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;

/**
 * Utilities to work with custom HAPI class definitions.
 *
 * @author Dmytro Rud
 */
public final class CustomModelClassUtils {

    private static final String CUSTOM_EVENT_MAP_DIRECTORY = "org/openehealth/ipf/commons/ihe/hl7v2/";

    private CustomModelClassUtils() {
        throw new IllegalStateException("Utility class, cannot instantiate");
    }

    /**
     * Creates a model class factory for the given transaction and HL7 version.
     */
    public static CustomModelClassFactory createFactory(String transaction, String version) {
        var packageName = String.format("%s.%s.%s",
                CustomModelClassUtils.class.getPackage().getName(),
                transaction,
                Version.versionOf(version).getPackageVersion());
        var map = Collections.singletonMap(version, new String[]{packageName});
        var cmcf = new CustomModelClassFactory(map);
        cmcf.setEventMapDirectory(CUSTOM_EVENT_MAP_DIRECTORY);
        return cmcf;
    }


}
