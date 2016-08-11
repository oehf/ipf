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
package org.openehealth.ipf.commons.ihe.xds.iti51;

/**
 * Base audit strategy for ITI-51.
 *
 * @author Dmytro Rud
 * @author Michael Ottati
 */
public class Iti51ServerAuditStrategy extends Iti51AuditStrategy {

    private static class LazyHolder {
        private static final Iti51ServerAuditStrategy INSTANCE = new Iti51ServerAuditStrategy();
    }

    public static Iti51ServerAuditStrategy getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Iti51ServerAuditStrategy() {
        super(true);
    }

}
