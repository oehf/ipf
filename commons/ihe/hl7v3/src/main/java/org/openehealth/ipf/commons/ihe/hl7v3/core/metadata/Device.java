/*
 * Copyright 2021 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core.metadata;

import lombok.Data;
import net.ihe.gazelle.hl7v3.datatypes.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified model of a device.
 *
 * @author Quentin Ligier
 * @since 4.1
 */
@Data
public class Device {

    /**
     * Telecom of the sender/receiver.
     */
    private TEL telecom;

    /**
     * The application IDs. At least one ID shall be provided. The list is never {@code null}.
     */
    private final List<II> ids = new ArrayList<>();

    /**
     * The application names. It may be empty but is never {@code null}.
     */
    private final List<EN> names = new ArrayList<>();

    /**
     * The application description.
     */
    private ED desc;

    /**
     * The application net address. It may be empty but is never {@code null}.
     */
    private final List<TEL> deviceTelecom = new ArrayList<>();

    /**
     * The application model name.
     */
    private SC manufacturerModelName;

    /**
     * The application software name.
     */
    private SC softwareName;
}
