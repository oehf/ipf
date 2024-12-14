/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.controls.sorting;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.DsmlAttr;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchResponse;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchResultEntry;

import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.util.*;

/**
 * @author Dmytro Rud
 * @since 4.3
 */
@Slf4j
public class SearchResponseSorter {

    private static final String DEFAULT_MATCHING_RULE = "std";

    /**
     * Map from algorithm names ("matching rule IDs" in LDAP terminology) to string comparators.
     * Applications can extend this map.
     */
    @Getter
    private static final Map<String, Comparator<String>> COMPARATORS = new HashMap<>();

    static {
        // case-sensitive, no whitespace normalization
        COMPARATORS.put("std",
                (s1, s2) -> StringUtils.compare(s1, s2, true));

        // case-insensitive, no whitespace normalization
        COMPARATORS.put("std_i",
                (s1, s2) -> StringUtils.compareIgnoreCase(s1, s2, true));

        // case-sensitive, with whitespace normalization
        COMPARATORS.put("std_w",
                (s1, s2) -> StringUtils.compare(StringUtils.normalizeSpace(s1), StringUtils.normalizeSpace(s2), true));

        // case-insensitive, with whitespace normalization
        COMPARATORS.put("std_iw",
                (s1, s2) -> StringUtils.compareIgnoreCase(StringUtils.normalizeSpace(s1), StringUtils.normalizeSpace(s2), true));
    }

    /**
     * First, sort values of the specified attribute in each search result entry.
     * Then, sort search result entries based on the first value of the specified attribute.
     *
     * @param searchResponse search response whose contents will be sorted <b>in-place</b>
     * @param control        sorting control
     */
    public static void sort(SearchResponse searchResponse, SortControl2 control) throws IOException {
        String requestId = searchResponse.getRequestID();

        if (control.getKeys().length == 0) {
            log.debug("No sorting keys provided for response with ID {}", requestId);
            ControlUtils.setControl(searchResponse, new SortResponseControl2(ResultCodes.UNWILLING_TO_PERFORM, null, control.isCritical()));
            return;
        }
        if (control.getKeys().length > 1) {
            log.debug("More than one sorting key provided in response with ID {}", requestId);
            ControlUtils.setControl(searchResponse, new SortResponseControl2(ResultCodes.UNWILLING_TO_PERFORM, null, control.isCritical()));
            return;
        }

        SortKey key = control.getKeys()[0];

        String matchingRuleId = StringUtils.isBlank(key.getMatchingRuleID()) ? DEFAULT_MATCHING_RULE : key.getMatchingRuleID().trim();
        Comparator<String> comparator0 = COMPARATORS.get(matchingRuleId);
        if (comparator0 == null) {
            log.debug("Unknown matching rule ID {} in response with ID {}", matchingRuleId, requestId);
            ControlUtils.setControl(searchResponse, new SortResponseControl2(ResultCodes.INAPPROPRIATE_MATCHING, key.getAttributeID(), control.isCritical()));
            return;
        }

        Comparator<String> comparator = key.isAscending() ? comparator0 : (s1, s2) -> -comparator0.compare(s1, s2);

        // sort values of the specified attribute in each search result entry (it does not make sense for DN)
        if (!key.getAttributeID().equalsIgnoreCase("dn")) {
            for (SearchResultEntry entry : searchResponse.getSearchResultEntry()) {
                List<String> values = new ArrayList<>();
                Iterator<DsmlAttr> iterator = entry.getAttr().iterator();

                while (iterator.hasNext()) {
                    DsmlAttr attr = iterator.next();
                    if (key.getAttributeID().equalsIgnoreCase(attr.getName())) {
                        values.addAll(attr.getValue().stream().map(Object::toString).toList());
                        iterator.remove();
                    }
                }

                values.sort(comparator);

                DsmlAttr attr = new DsmlAttr();
                attr.setName(key.getAttributeID());
                attr.getValue().addAll(values);
                entry.getAttr().add(attr);
            }
        }

        // sort search result entries based on the first value of the specified attribute
        searchResponse.getSearchResultEntry().sort((entry1, entry2) -> comparator.compare(
                extractAttributeValue(entry1, key.getAttributeID()),
                extractAttributeValue(entry2, key.getAttributeID())));

        // set response control
        ControlUtils.setControl(searchResponse, new SortResponseControl2(ResultCodes.SUCCESS, null, control.isCritical()));
    }

    private static String extractAttributeValue(SearchResultEntry entry, String attributeName) {
        if (attributeName.equalsIgnoreCase("dn")) {
            return entry.getDn();
        }
        for (DsmlAttr attr : entry.getAttr()) {
            if (attributeName.equalsIgnoreCase(attr.getName())) {
                return attr.getValue().isEmpty() ? null : (String) attr.getValue().get(0);
            }
        }
        return null;
    }

}
