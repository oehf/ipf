/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.boot.ech;

import org.openehealth.ipf.boot.atna.IpfAtnaAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(IpfAtnaAutoConfiguration.class)
@EnableConfigurationProperties(IpfEchConfigurationProperties.class)
public class IpfEchAutoConfiguration {

    private final IpfEchConfigurationProperties properties;

    public IpfEchAutoConfiguration(IpfEchConfigurationProperties properties) {
        this.properties = properties;
    }
}
