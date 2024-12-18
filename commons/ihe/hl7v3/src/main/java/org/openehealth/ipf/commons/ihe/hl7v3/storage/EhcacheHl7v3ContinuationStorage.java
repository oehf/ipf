/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.hl7v3.storage;


import javax.cache.Cache;

import java.io.Serializable;

/**
 * @author Dmytro Rud
 * @author Christian Ohr
 * @deprecated
 */
@Deprecated(forRemoval = true)
public class EhcacheHl7v3ContinuationStorage extends JCacheHl7v3ContinuationStorage {

    public EhcacheHl7v3ContinuationStorage(Cache<String, Serializable> cache) {
        super(cache);
    }
}

