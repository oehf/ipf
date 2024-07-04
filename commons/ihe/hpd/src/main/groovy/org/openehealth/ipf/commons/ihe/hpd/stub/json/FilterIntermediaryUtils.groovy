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

import jakarta.xml.bind.JAXBElement
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.Filter
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.FilterSet
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ObjectFactory

/**
 * @author Dmytro Rud
 */
class FilterIntermediaryUtils {

    static final ObjectFactory OBJECT_FACTORY = new ObjectFactory()

    static final List<String> FILTER_TYPES = ['and', 'or', 'not', 'equalityMatch', 'substring', 'greaterOrEqual', 'lessOrEqual', 'present', 'approxMatch', 'extensibleMatch']

    static FilterIntermediary fromDsml(String filterType, Object dsml) {
        return Class.forName(FilterIntermediaryUtils.packageName + '.' + filterType.capitalize()).declaredMethods.find { it.name == 'fromDsml' }.invoke(null, dsml) as FilterIntermediary
    }

    static FilterIntermediary fromFilter(Filter f) {
        for (fieldName in FILTER_TYPES) {
            def fieldValue = f."${fieldName}"
            if (fieldValue) {
                return fromDsml(fieldName, fieldValue)
            }
        }
        return null
    }

    static Filter toFilter(FilterIntermediary fi) {
        return new Filter("${fi.class.simpleName.uncapitalize()}": fi.toDsml())
    }

    static List<FilterIntermediary> fromFilterSet(FilterSet fs) {
        return fs.filterGroup.collect { jaxbElement -> fromDsml(jaxbElement.name.localPart, jaxbElement.value) }
    }

    static FilterSet toFilterSet(List<FilterIntermediary> fis) {
        return new FilterSet(filterGroup: fis.collect { fi ->
            OBJECT_FACTORY."${'createFilterSet' + fi.class.simpleName}"(fi.toDsml()) as JAXBElement
        })
    }

}
