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
package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Swiss EPR access level.
 *
 * @author Dmytro Rud
 */
@AllArgsConstructor
public enum AccessLevel {
    NORMAL    ("urn:e-health-suisse:2015:policies:access-level:normal"),
    RESTRICTED("urn:e-health-suisse:2015:policies:access-level:restricted");

    @Getter private final String urn;
}
