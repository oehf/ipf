/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti67;

/**
 * Strategy for auditing ITI-67 transactions on the client side
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class Iti67ClientAuditStrategy extends Iti67AuditStrategy {

    private static class LazyHolder {
        private static final Iti67ClientAuditStrategy INSTANCE = new Iti67ClientAuditStrategy();
    }

    public static Iti67ClientAuditStrategy getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Iti67ClientAuditStrategy() {
        super(false);
    }

}
