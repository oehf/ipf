/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.stub.json

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import groovy.transform.CompileStatic
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*

/**
 * @author Dmytro Rud
 */
@CompileStatic
class SearchRequestIntermediary {

    String requestId
    @JsonSerialize(using = ControlListSerializer.class)
    @JsonDeserialize(using = ControlListDeserializer.class)
    List<Control> controls
    FilterIntermediary filter
    List<String> attributes
    String dn
    SearchRequest.SearchScope scope
    SearchRequest.DerefAliasesType derefAliases
    long sizeLimit
    long timeLimit
    boolean typesOnly

    static SearchRequestIntermediary fromSearchRequest(SearchRequest searchRequest) {
        return new SearchRequestIntermediary(
            requestId:      searchRequest.requestID,
            controls:       searchRequest.control,
            filter:         FilterIntermediaryUtils.fromFilter(searchRequest.filter),
            attributes:     searchRequest.attributes.attribute.collect { it.name },
            dn:             searchRequest.dn,
            scope:          searchRequest.scope,
            derefAliases:   searchRequest.derefAliases,
            sizeLimit:      searchRequest.sizeLimit,
            timeLimit:      searchRequest.timeLimit,
            typesOnly:      searchRequest.typesOnly,
        )
    }

    SearchRequest toSearchRequest() {
        return new SearchRequest(
            requestID:      requestId,
            control:        controls,
            filter:         FilterIntermediaryUtils.toFilter(filter),
            attributes:     new AttributeDescriptions(attribute: attributes.collect { new AttributeDescription(name: it) }),
            dn:             dn,
            scope:          scope,
            derefAliases:   derefAliases,
            sizeLimit:      sizeLimit,
            timeLimit:      timeLimit,
            typesOnly:      typesOnly,
        )
    }

}
