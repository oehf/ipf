/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.boot.hl7v2;

import org.openehealth.ipf.modules.hl7.config.CustomModelClasses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * A minimal application depending on this starter, as a user of the starter would write it. It
 * contributes custom HL7v2 model classes the way the extension mechanism is meant to be used:
 * by declaring a {@link CustomModelClasses} bean and nothing else.
 */
@SpringBootApplication
public class TestApplication {

    public static final String CUSTOM_PACKAGE = "org.openehealth.ipf.boot.hl7v2.test.v25";

    @Bean
    CustomModelClasses customModelClasses() {
        var customModelClasses = new CustomModelClasses();
        customModelClasses.setModelClasses(Map.of("2.5", new String[]{ CUSTOM_PACKAGE }));
        return customModelClasses;
    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
