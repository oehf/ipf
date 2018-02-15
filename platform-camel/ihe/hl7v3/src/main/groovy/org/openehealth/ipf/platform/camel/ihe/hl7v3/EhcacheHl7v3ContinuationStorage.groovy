/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3

import net.sf.ehcache.Ehcache

/**
 * @author Dmytro Rud
 *
 * @deprecated use {@link org.openehealth.ipf.commons.ihe.hl7v3.storage.EhcacheHl7v3ContinuationStorage}
 */
class EhcacheHl7v3ContinuationStorage extends org.openehealth.ipf.commons.ihe.hl7v3.storage.EhcacheHl7v3ContinuationStorage {

    EhcacheHl7v3ContinuationStorage(Ehcache ehcache) {
        super(ehcache)
    }
}