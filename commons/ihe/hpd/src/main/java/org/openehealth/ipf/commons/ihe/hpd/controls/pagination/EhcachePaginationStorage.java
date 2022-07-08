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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Ehcache-based pagination storage.
 *
 * @author Dmytro Rud
 * @since 4.3
 */
@Slf4j
public class EhcachePaginationStorage implements PaginationStorage {

    private static final JAXBContext JAXB_CONTEXT;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(
                    org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ObjectFactory.class,
                    Container.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Cache<String, Object> cache;
    private final boolean needSerialization;

    /**
     * @param cache             cache instance
     * @param needSerialization whether search result entries shall be serialized using JAXB -- this is necessary for persistent caches
     */
    public EhcachePaginationStorage(Cache<String, Object> cache, boolean needSerialization) {
        this.cache = Objects.requireNonNull(cache);
        this.needSerialization = needSerialization;
    }

    @Override
    public void store(byte[] cookie, List<SearchResultEntry> entries) {
        log.debug("Store {} entries for cookie with hash {}", entries.size(), Arrays.hashCode(cookie));

        if (needSerialization) {
            Container container = new Container(entries);
            String value = XmlUtils.renderJaxb(JAXB_CONTEXT, container, false);
            cache.put(new String(cookie), value);
        } else {
            cache.put(new String(cookie), entries);
        }
    }

    @Override
    public TakeResult take(PagedResultsResponseControl pagination) throws Exception {
        byte[] cookie = pagination.getCookie();
        Object value = cache.get(new String(cookie));

        if (value == null) {
            log.debug("No entries for cookie with hash {}", Arrays.hashCode(cookie));
            return new TakeResult(null, false);
        }

        List<SearchResultEntry> entries;
        if (needSerialization) {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            Container container = (Container) unmarshaller.unmarshal(XmlUtils.source((String) value));
            entries = container.entries;
        } else {
            entries = (List<SearchResultEntry>) value;
        }

        int entriesCount = entries.size();
        int requestedCount = pagination.getResultSize();
        if (entriesCount > requestedCount) {
            log.debug("Return {} entries for cookie with hash {}, let {} in the storage", requestedCount, Arrays.hashCode(cookie), entries.size() - requestedCount);

            List<SearchResultEntry> entriesToStore = new ArrayList<>(entries.subList(requestedCount, entriesCount));
            store(cookie, entriesToStore);

            List<SearchResultEntry> entriesToDeliver = new ArrayList<>(entries.subList(0, requestedCount));
            return new TakeResult(entriesToDeliver, true);

        } else {
            log.debug("Return {} last entries for cookie with hash {} and delete the cache", entriesCount, Arrays.hashCode(cookie));
            cache.remove(new String(cookie));
            return new TakeResult(entries, false);
        }
    }


    @XmlRootElement
    private static class Container {

        @XmlElement(name = "entry")
        public List<SearchResultEntry> entries;

        public Container() {
            // for JAXB
        }

        public Container(List<SearchResultEntry> entries) {
            this.entries = entries;
        }
    }

}
