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

import com.fasterxml.jackson.annotation.JsonTypeInfo
import groovy.transform.CompileStatic
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*

/**
 * @author Dmytro Rud
 */
@CompileStatic
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
interface FilterIntermediary<T> {
    T toDsml()
    //... and there must be a static method with the signature "static FilterIntermediary fromDsml(T dsml)"
}

@CompileStatic
class And implements FilterIntermediary<FilterSet> {
    List<FilterIntermediary> nested
    FilterSet toDsml() { return FilterIntermediaryUtils.toFilterSet(nested) }
    static FilterIntermediary fromDsml(FilterSet fs) { return new And(nested: FilterIntermediaryUtils.fromFilterSet(fs)) }
}

@CompileStatic
class Or implements FilterIntermediary<FilterSet> {
    List<FilterIntermediary> nested
    FilterSet toDsml() { return FilterIntermediaryUtils.toFilterSet(nested) }
    static FilterIntermediary fromDsml(FilterSet fs) { return new Or(nested: FilterIntermediaryUtils.fromFilterSet(fs)) }
}

@CompileStatic
class Not implements FilterIntermediary<Filter> {
    FilterIntermediary nested
    Filter toDsml() { return FilterIntermediaryUtils.toFilter(nested) }
    static FilterIntermediary fromDsml(Filter f) { return new Not(nested: FilterIntermediaryUtils.fromFilter(f)) }
}

@CompileStatic
class EqualityMatch extends AttributeValueAssertion implements FilterIntermediary<AttributeValueAssertion> {
    AttributeValueAssertion toDsml() { return this }
    static FilterIntermediary fromDsml(AttributeValueAssertion ava) { return new EqualityMatch(name: ava.name, value: ava.value) }
}

@CompileStatic
class Substrings extends SubstringFilter implements FilterIntermediary<SubstringFilter> {
    SubstringFilter toDsml() { return this }
    static FilterIntermediary fromDsml(SubstringFilter sf) { return new Substrings(name: sf.name, initial: sf.initial, any: sf.any, final: sf.final) }
}

@CompileStatic
class GreaterOrEqual extends AttributeValueAssertion implements FilterIntermediary<AttributeValueAssertion> {
    AttributeValueAssertion toDsml() { return this }
    static FilterIntermediary fromDsml(AttributeValueAssertion ava) { return new GreaterOrEqual(name: ava.name, value: ava.value) }
}

@CompileStatic
class LessOrEqual extends AttributeValueAssertion implements FilterIntermediary<AttributeValueAssertion> {
    AttributeValueAssertion toDsml() { return this }
    static FilterIntermediary fromDsml(AttributeValueAssertion ava) { return new LessOrEqual(name: ava.name, value: ava.value) }
}

@CompileStatic
class Present extends AttributeDescription implements FilterIntermediary<AttributeDescription> {
    AttributeDescription toDsml() { return this }
    static FilterIntermediary fromDsml(AttributeDescription ad) { return new Present(name: ad.name) }
}

@CompileStatic
class ApproxMatch extends AttributeValueAssertion implements FilterIntermediary<AttributeValueAssertion> {
    AttributeValueAssertion toDsml() { return this }
    static FilterIntermediary fromDsml(AttributeValueAssertion ava) { return new ApproxMatch(name: ava.name, value: ava.value) }
}

@CompileStatic
class ExtensibleMatch extends MatchingRuleAssertion implements FilterIntermediary<MatchingRuleAssertion> {
    MatchingRuleAssertion toDsml() { return this }
    static FilterIntermediary fromDsml(MatchingRuleAssertion mra) { return new ExtensibleMatch(name: mra.name, value: mra.value, dnAttributes: mra.dnAttributes, matchingRule: mra.matchingRule) }
}
