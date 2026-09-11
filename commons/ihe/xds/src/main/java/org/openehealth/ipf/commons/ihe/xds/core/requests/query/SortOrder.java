/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The order a Document Consumer asks a registry to return a stored query result set in.
 * <p>
 * ITI-18 defines no ordering, so this is a bilaterally agreed extension. It travels as one extra query
 * slot, by default named {@value #DEFAULT_SLOT_NAME}, holding the keys in order of precedence with a
 * leading {@code -} marking a descending one:
 * <pre>
 * &lt;rim:Slot name="$ipfSortOrder"&gt;
 *   &lt;rim:ValueList&gt;
 *     &lt;rim:Value&gt;('-$XDSDocumentEntryCreationTime','$XDSDocumentEntryAuthorPerson')&lt;/rim:Value&gt;
 *   &lt;/rim:ValueList&gt;
 * &lt;/rim:Slot&gt;
 * </pre>
 * A registry that does not know the slot must ignore it, as ITI-18 requires of any parameter it does not
 * understand, so adding an order never breaks a query -- but it also means an ignored order is
 * indistinguishable from an honoured one unless the registry acknowledges it in the response.
 * <p>
 * Paging over an ordered result set is only sound if the order is <em>total</em>: keys that leave ties
 * let a registry place equal entries differently between two calls, so the same entry can appear on two
 * pages or on none. {@link #withStableTiebreaker()} appends the entry UUID for that reason.
 *
 * @author Christian Ohr
 * @since 6.0
 * @see SortKey
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SortOrder", propOrder = {"keys"})
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class SortOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 6522477012269587733L;

    /**
     * Name of the query slot the sort order travels in. Namespaced to IPF because it is an extension
     * rather than an IHE-defined parameter, and a future IHE parameter must not collide with it.
     */
    public static final String DEFAULT_SLOT_NAME = "$ipfSortOrder";

    private static volatile String slotName = DEFAULT_SLOT_NAME;

    /**
     * @return the slot name sort orders are written to and read from
     */
    public static String getSlotName() {
        return slotName;
    }

    /**
     * Sets the slot name for deployments that agreed on a different one with their registry.
     * <p>
     * Deliberately global rather than per query or per endpoint: which slot carries the order is part of
     * the bilateral agreement with the registry, so it is a property of the deployment, not something a
     * single query gets to choose. Set it once at startup, before the first query is transformed.
     *
     * @param slotName the agreed slot name, which by IHE convention starts with {@code $} and must not
     *                 collide with an IHE-defined query parameter
     */
    public static void setSlotName(String slotName) {
        if (slotName == null || !slotName.startsWith("$")) {
            throw new IllegalArgumentException("a query slot name has to start with '$', but was " + slotName);
        }
        SortOrder.slotName = slotName;
    }

    /**
     * The slot the entry UUID is filtered with, appended by {@link #withStableTiebreaker()}. Spelled out
     * rather than taken from {@code QueryParameter}, which belongs to the transform layer.
     */
    private static final String ENTRY_UUID_SLOT_NAME = "$XDSDocumentEntryEntryUUID";

    @XmlElement(name = "key")
    @Getter private final List<SortKey> keys = new ArrayList<>();

    /**
     * For JAXB serialization only.
     */
    public SortOrder() {
    }

    /**
     * @param keys the sort keys, in order of precedence
     */
    public SortOrder(List<SortKey> keys) {
        if (keys != null) {
            this.keys.addAll(keys);
        }
    }

    /**
     * @param keys the sort keys, in order of precedence
     */
    public SortOrder(SortKey... keys) {
        this(Arrays.asList(keys));
    }

    /**
     * Returns this order made total by appending the entry UUID as the least significant key, unless it
     * is already among the keys.
     * <p>
     * Sorting alone does not need this; paging does. An order that leaves ties is not a defined sequence,
     * and a window into an undefined sequence can repeat or skip entries even against a registry whose
     * content does not change.
     *
     * @return an order ending in a unique key
     */
    public SortOrder withStableTiebreaker() {
        if (keys.stream().anyMatch(key -> ENTRY_UUID_SLOT_NAME.equals(key.getSlotName()))) {
            return this;
        }
        var stable = new SortOrder(keys);
        stable.keys.add(SortKey.ascending(ENTRY_UUID_SLOT_NAME));
        return stable;
    }

    /**
     * @return whether this order names no key at all, in which case nothing is written to the slot
     */
    public boolean isEmpty() {
        return keys.isEmpty();
    }

    /**
     * @return the values of the sort order slot, one per key, in order of precedence
     */
    public List<String> encode() {
        return keys.stream().map(SortKey::encode).toList();
    }

    /**
     * @param values values of the sort order slot, in order of precedence
     * @return the order they stand for, or null if there are none
     * @throws IllegalArgumentException if a value names no slot
     */
    public static SortOrder decode(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return new SortOrder(values.stream().map(SortKey::decode).toList());
    }

}
