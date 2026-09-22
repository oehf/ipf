/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.map.yaml;

import tools.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Jackson binding for {@code *.mapping.yaml} files, mirroring
 * {@code META-INF/ipf/mapping-schema.json}.
 * <p>
 * Property names are bound strictly, so a typo is a startup error with a line and a column. The
 * two places where the format accepts either a scalar or an object - an entry and an unmatched
 * declaration - are bound as {@link JsonNode} and interpreted by {@link YamlMappingLoader},
 * which is also where their own property names are checked.
 *
 * @since 6.0
 */
final class MappingYaml {

    static final String SCHEMA_RESOURCE = "/META-INF/ipf/mapping-schema.json";
    static final String SCHEMA_LOCATION = "http://openehealth.org/schema/ipf-commons-map.json";

    private MappingYaml() {
    }

    static class Document {

        public Map<String, YamlMapping> mappings = new LinkedHashMap<>();
    }

    static class YamlMapping {

        public String keySystem;
        public String valueSystem;
        public Boolean reversible;
        public Boolean override;
        public Map<String, JsonNode> entries = new LinkedHashMap<>();
        public JsonNode unmatched;
        public YamlReverse reverse;
    }

    static class YamlReverse {

        public JsonNode unmatched;
    }
}
