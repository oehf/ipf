/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.controls.pagination;

import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchResultEntry;
import org.openehealth.ipf.commons.xml.XmlUtils;

import javax.cache.Cache;
import javax.naming.ldap.PagedResultsResponseControl;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Ehcache-based pagination storage.
 *
 * @author Dmytro Rud
 * @since 4.3
 * @deprecated
 */
@Slf4j
@Deprecated(forRemoval = true)
public class EhcachePaginationStorage extends JCachePaginationStorage {

    public EhcachePaginationStorage(Cache<String, Object> cache, boolean needSerialization) {
        super(cache, needSerialization);
    }

}