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
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchResultEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Simple in-memory implementation of a pagination storage.
 *
 * @author Dmytro Rud
 * @since 3.7.5
 */
@Slf4j
public class EhcachePaginationStorage implements PaginationStorage {

    private final Ehcache ehcache;

    public EhcachePaginationStorage(Ehcache ehcache) {
        this.ehcache = Objects.requireNonNull(ehcache);
    }

    @Override
    public void store(byte[] cookie, List<SearchResultEntry> entries) {
        log.debug("Store {} entries for cookie with hash {}", entries.size(), Arrays.hashCode(cookie));
        ehcache.put(new Element(new String(cookie), entries));
    }

    @Override
    public TakeResult take(Pagination pagination) {
        byte[] cookie = pagination.getCookie();
        Element element = ehcache.get(new String(cookie));

        if (element == null) {
            log.debug("No entries for cookie with hash {}", Arrays.hashCode(cookie));
            return new TakeResult(null, false);
        }

        List<SearchResultEntry> entries = (List<SearchResultEntry>) element.getObjectValue();
        int entriesCount = entries.size();
        if (entriesCount > pagination.getSize()) {
            log.debug("Return {} entries for cookie with hash {}, let {} in the storage", pagination.getSize(), Arrays.hashCode(cookie), entries.size() - pagination.getSize());

            List<SearchResultEntry> entriesToStore = new ArrayList<>(entries.subList(pagination.getSize(), entriesCount));
            store(cookie, entriesToStore);

            List<SearchResultEntry> entriesToDeliver = new ArrayList<>(entries.subList(0, pagination.getSize()));
            return new TakeResult(entriesToDeliver, true);

        } else {
            log.debug("Return {} last entries for cookie with hash {} and delete the cache", entriesCount, Arrays.hashCode(cookie));
            ehcache.remove(cookie);
            return new TakeResult(entries, false);
        }
    }

}
