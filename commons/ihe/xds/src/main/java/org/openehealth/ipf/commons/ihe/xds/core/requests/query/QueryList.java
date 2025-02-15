/*
 * Copyright 2009 the original author or authors.
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.Validate.noNullElements;
import static java.util.Objects.requireNonNull;

/**
 * Represents a list of query parameters.
 * <p>
 * The list allows AND and OR semantics via two levels of lists.
 * The inner lists of parameters have OR semantics. The outer list
 * contains the inner lists and uses AND semantics. E.g. the query
 * list <code>(a, b), (c, d)</code> contains two inner lists and the
 * parameters are evaluated (a OR b) AND (c OR d).
 * @param <T>
 *          The type contained in the list.
 *
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QueryList")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class QueryList<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -2729640243221349924L;
    
    @XmlJavaTypeAdapter(ListOfListAdapter.class)
    @JsonIgnore
    @Getter private List<List<T>> outerList = new ArrayList<>();

    /**
     * This method is private because it is used only for Jackson serialization.
     */
    @JsonProperty
    private List<List<T>> getList() {
        return outerList;
    }

    /**
     * This method is private because it is used only for Jackson serialization.
     */
    private void setList(List<List<T>> list) {
        outerList = list;
    }

    /**
     * Constructs a query list.
     */
    public QueryList() {}

    /**
     * Constructs a query list using another list.
     * <p>
     * This constructor does not clone the objects in the list.
     * @param other
     *          the other list.
     */
    public QueryList(QueryList<T> other) {
        requireNonNull(other, "other cannot be null");
        noNullElements(other.getOuterList(), "other.getOuterList() cannot contain null elements");
        for (var innerList : other.getOuterList()) {
            noNullElements(innerList, "innerList cannot contain null elements");
            outerList.add(new ArrayList<>(innerList));
        }
    }

    /**
     * Constructs a query list.
     * @param singleElement
     *          the only initial element in the list.
     */
    public QueryList(T singleElement) {
        requireNonNull(singleElement, "singleElement cannot be null");
        outerList.add(Collections.singletonList(singleElement));
    }

}
