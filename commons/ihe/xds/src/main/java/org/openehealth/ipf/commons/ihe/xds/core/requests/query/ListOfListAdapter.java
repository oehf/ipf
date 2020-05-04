/*
 * Copyright 2011 the original author or authors.
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A JAXB {@link XmlAdapter} that helps serialize generic lists of lists. This sort of thing is used by the
 * IPF {@link QueryList} class but is not handled naturally by JAXB. It takes a little effort here to get a
 * reasonable serialization.
 *
 * @param <T> The type of object contained in the inner list
 */
@XmlTransient
public class ListOfListAdapter<T> extends XmlAdapter<ListOfListAdapter.ListOfListWrapper<T>, List<List<T>>> {
    @Override
    public List<List<T>> unmarshal(ListOfListWrapper<T> v) throws Exception {
        return v.getInnerList().stream()
                .map(ListWrapper::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public ListOfListWrapper<T> marshal(List<List<T>> v) throws Exception {
        var outerList = v.stream()
                .map(ListWrapper::new)
                .collect(Collectors.toList());
        return new ListOfListWrapper<>(outerList);
    }

    @XmlType(name = "ListOfListWrapper", namespace = "http://www.openehealth.org/ipf/xds")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ListOfListWrapper<T> {
        private List<ListWrapper<T>> innerList;

        // Required for JAXB
        @SuppressWarnings({"UnusedDeclaration"})
        protected ListOfListWrapper() {
        }

        public ListOfListWrapper(List<ListWrapper<T>> list) {
            this.innerList = list;
        }

        public List<ListWrapper<T>> getInnerList() {
            return innerList;
        }
    }

    @XmlType(name = "ListWrapper", namespace = "http://www.openehealth.org/ipf/xds")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ListWrapper<T> {
        private List<T> value;

        // Required for JAXB
        @SuppressWarnings({"UnusedDeclaration"})
        protected ListWrapper() {
        }

        public ListWrapper(List<T> list) {
            this.value = list;
        }

        public List<T> getValue() {
            return value;
        }
    }
}

