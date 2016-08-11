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

package org.openehealth.ipf.commons.ihe.xds.rad69;

/**
 *
 */
public class Rad69ClientAuditStrategy extends Rad69AuditStrategy {

    private static class LazyHolder {
        private static final Rad69ClientAuditStrategy INSTANCE = new Rad69ClientAuditStrategy();
    }

    public static Rad69ClientAuditStrategy getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Rad69ClientAuditStrategy() {
        super(false);
    }
}
