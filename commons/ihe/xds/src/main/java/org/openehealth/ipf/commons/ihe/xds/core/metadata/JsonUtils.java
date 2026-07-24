/*
 * Copyright 2018 the original author or authors.
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


import lombok.experimental.UtilityClass;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

/**
 * @author Dmytro Rud
 */
@UtilityClass
public class JsonUtils {

    /**
     * Creates and returns an ObjectMapper instance suitable for the simplified XDS data model.
     */
    public ObjectMapper createObjectMapper() {
        // Java 8 date/time support is auto-registered by jackson-databind 3.x, so no JavaTimeModule is needed.
        // ALLOW_FINAL_FIELDS_AS_MUTATORS restores the Jackson 2.x default so that pre-initialized final fields
        // (e.g. the TimeRange fields of the stored queries) are populated on deserialization.
        return JsonMapper.builder()
                .addModule(new JakartaXmlBindAnnotationModule())
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
                .build();
    }

}
