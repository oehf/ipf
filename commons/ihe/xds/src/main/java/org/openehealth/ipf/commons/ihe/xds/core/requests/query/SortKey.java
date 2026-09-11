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
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;
import lombok.EqualsAndHashCode;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * One key of a {@link SortOrder}: an XDS metadata attribute and the direction to order a result set by
 * it. The attribute is named the way ITI-18 names slots, e.g. {@code $XDSDocumentEntryCreationTime}.
 *
 * @author Christian Ohr
 * @since 6.0
 * @see SortOrder
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SortKey")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class SortKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 4262930716253147081L;

    /**
     * Marks a descending key in the encoded form, as in {@code -$XDSDocumentEntryCreationTime}.
     */
    static final String DESCENDING_PREFIX = "-";

    @XmlEnum
    @XmlType(name = "SortDirection")
    public enum Direction {

        ASCENDING {
            @Override
            public <T extends Comparable<? super T>> Comparator<T> comparator() {
                return Comparator.nullsLast(Comparator.naturalOrder());
            }
        },

        DESCENDING {
            @Override
            public <T extends Comparable<? super T>> Comparator<T> comparator() {
                return Comparator.nullsLast(Comparator.reverseOrder());
            }
        };

        /**
         * Each constant supplies its own ordering rather than being constructed with one, because a
         * field would have to name a single concrete type. The only type that fits both
         * {@code naturalOrder()} and a heterogeneous result set is {@code Comparator<Comparable<Object>>},
         * which no caller could apply to a list of its own type without casting. As a generic method the
         * ordering is usable at whatever type the caller actually has.
         *
         * @param <T> type of the values being ordered
         * @return a comparator ordering in this direction, with missing values last <em>in both
         *      directions</em>. Reversing an ascending comparator would instead move them to the front
         *      of a descending result, so that values lacking the attribute would crowd out the ones
         *      the requester asked to see first.
         */
        public abstract <T extends Comparable<? super T>> Comparator<T> comparator();
    }

    @XmlAttribute(required = true)
    @Getter private String slotName;

    @XmlAttribute(required = true)
    @Getter private Direction direction;

    /**
     * For JAXB serialization only.
     */
    public SortKey() {
    }

    /**
     * @param slotName  name of the metadata attribute to sort by, in the {@code $}-prefixed spelling
     *                  ITI-18 uses for slots, e.g. {@code $XDSDocumentEntryCreationTime}. Deliberately a
     *                  plain string and not a {@code QueryParameter}: what is sortable is an attribute,
     *                  and the query parameters are not the same vocabulary -- creation time is filtered
     *                  by the pair {@code CreationTimeFrom}/{@code CreationTimeTo} but sorted by the
     *                  attribute itself. Extension attributes are allowed too.
     * @param direction the direction to sort in
     */
    public SortKey(String slotName, Direction direction) {
        if (isBlank(slotName)) {
            throw new IllegalArgumentException("slot name cannot be blank");
        }
        this.slotName = slotName;
        this.direction = requireNonNull(direction, "direction cannot be null");
    }

    /**
     * @param slotName name of the metadata attribute to sort by, e.g. {@code $XDSDocumentEntryCreationTime}
     * @return an ascending key on it
     */
    public static SortKey ascending(String slotName) {
        return new SortKey(slotName, Direction.ASCENDING);
    }

    /**
     * @param slotName name of the metadata attribute to sort by, e.g. {@code $XDSDocumentEntryCreationTime}
     * @return a descending key on it
     */
    public static SortKey descending(String slotName) {
        return new SortKey(slotName, Direction.DESCENDING);
    }

    /**
     * @return this key as it appears in the sort order slot, a slot name with a leading {@code -} when
     *      descending
     */
    public String encode() {
        return direction == Direction.DESCENDING ? DESCENDING_PREFIX + slotName : slotName;
    }

    /**
     * Reads one entry of the sort order slot.
     * <p>
     * Unlike the constructor, this takes its input off the wire, so a malformed value is bad metadata
     * rather than a programming error and is reported the way every other malformed slot in ITI-18 is --
     * as an {@link XDSMetaDataException}, which the transaction turns into a RegistryError instead of an
     * unhandled fault.
     *
     * @param value one entry of the sort order slot
     * @return the key it stands for
     * @throws XDSMetaDataException if the value names no attribute
     */
    public static SortKey decode(String value) {
        var trimmed = value != null ? value.trim() : "";
        var descending = trimmed.startsWith(DESCENDING_PREFIX);
        var slotName = descending ? trimmed.substring(DESCENDING_PREFIX.length()).trim() : trimmed;
        if (isBlank(slotName)) {
            throw new XDSMetaDataException(ValidationMessage.INVALID_SORT_ORDER, value);
        }
        return new SortKey(slotName, descending ? Direction.DESCENDING : Direction.ASCENDING);
    }

}
