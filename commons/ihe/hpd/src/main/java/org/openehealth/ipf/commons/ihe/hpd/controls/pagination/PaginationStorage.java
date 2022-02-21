/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.controls.pagination;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchResultEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmytro Rud
 * @since 3.7.5
 */
public interface PaginationStorage {

    /**
     * Stores an initial set of result entries.
     *
     * @param cookie  cookie (key).
     * @param entries entries.
     */
    void store(byte[] cookie, List<SearchResultEntry> entries);

    /**
     * Removes at most the given count of result entries from the storage and returns them
     * together with a flag indicating whether some more entries remain in the storage.
     * If the cookie is unknown, the list of result entries will be empty.
     *
     * @param pagination an object containing the cookie (key) and the maximal count of result entries to return.
     * @return result entries and the remaining entries' availability flag.
     */
    TakeResult take(Pagination pagination);


    class TakeResult {

        @Setter
        private List<SearchResultEntry> entries;

        @Getter
        @Setter
        private boolean moreEntriesAvailable;

        /**
         * @param entries              requested entries.
         * @param moreEntriesAvailable whether some further entries remain in the storage or not.
         */
        public TakeResult(List<SearchResultEntry> entries, boolean moreEntriesAvailable) {
            this.entries = entries;
            this.moreEntriesAvailable = moreEntriesAvailable;
        }

        public List<SearchResultEntry> getEntries() {
            if (entries == null) {
                entries = new ArrayList<>();
            }
            return entries;
        }
    }

}
