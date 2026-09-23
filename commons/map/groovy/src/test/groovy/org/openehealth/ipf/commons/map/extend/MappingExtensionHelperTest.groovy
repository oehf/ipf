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
package org.openehealth.ipf.commons.map.extend

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.map.Mappings

import static org.openehealth.ipf.commons.map.extend.MappingExtensionHelper.simpleMethodMissingLogic

/**
 * The dynamic mapXxx() / mapReverseXxx() form of the DSL, where the mapping name is part of the
 * method name, and the composite-value convention that goes with it.
 */
class MappingExtensionHelperTest {

    static Mappings mappings

    @BeforeAll
    static void setupClass() {
        mappings = Mappings.builder()
                .load(MappingExtensionHelperTest.class.getResource('/example2.map'))
                .build()
    }

    @Test
    void mapsByNameDerivedFromTheMethodName() {
        assert simpleMethodMissingLogic(mappings, 'I', 'mapEncounterType', []) == 'IMP'
        assert simpleMethodMissingLogic(mappings, 'X', 'mapEncounterType', []) == null
        assert simpleMethodMissingLogic(mappings, 'X', 'mapEncounterType', ['WRONG']) == 'WRONG'
    }

    @Test
    void mapsInReverse() {
        assert simpleMethodMissingLogic(mappings, 'IMP', 'mapReverseEncounterType', []) == 'I'
        assert simpleMethodMissingLogic(mappings, 'X', 'mapReverseEncounterType', []) == null
        assert simpleMethodMissingLogic(mappings, 'X', 'mapReverseEncounterType', ['WRONG']) == 'WRONG'
    }

    @Test
    void appliesTheFallbackOfTheMapping() {
        assert simpleMethodMissingLogic(mappings, 'anything', 'mapVip', []) == 'anything'
    }

    @Test
    void aMethodNameThatIsNeitherIsAnError() {
        try {
            simpleMethodMissingLogic(mappings, 'I', 'somethingElse', [])
            assert false, 'expected a failure'
        } catch (RuntimeException expected) {
            assert expected.cause instanceof MissingMethodException
        }
    }

    /**
     * There is no composite convention: a value containing a tilde comes back as the string it is,
     * and a key containing one is looked up as it is.
     */
    @Test
    void compositeKeysAndValuesAreSingleStrings() {
        assert simpleMethodMissingLogic(mappings, 'a~b', 'mapListTest', []) == 'c~d'
        assert simpleMethodMissingLogic(mappings, 'c~d', 'mapReverseListTest', []) == 'a~b'
        assert simpleMethodMissingLogic(mappings, 'anything', 'mapListTest2', []) == 'c~d'
    }

    /**
     * An entry mapping to an empty string yields the empty string, not the fallback or a default.
     */
    @Test
    void emptyValuesAreReturnedAsTheyAre() {
        assert simpleMethodMissingLogic(mappings, 'N', 'mapEmptyEntry', []) == ''
        assert simpleMethodMissingLogic(mappings, 'N', 'mapEmptyEntry', ['DEFAULT']) == ''
        assert simpleMethodMissingLogic(mappings, 'X', 'mapEmptyEntry', []) == 'IMP'
    }
}
