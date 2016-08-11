/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.mllp.iti30;

import org.openehealth.ipf.commons.ihe.hl7v2.TransactionOptions;

/**
 *
 */
public enum Iti30Options implements TransactionOptions {

    MERGE("A28", "A31", "A40", "A47"),
    LINK_UNLINK("A24", "A28", "A31", "A37", "A47");

    private String[]  supportedEvents;

    Iti30Options(String... supportedEvents) {
        this.supportedEvents = supportedEvents;
    }

    @Override
    public String[] getSupportedEvents() {
        return supportedEvents;
    }

}
