/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.chain;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmytro Rud
 */
public class ChainUtilsTest {
    private static final List<Chainable> INITIAL = new ArrayList<Chainable>();


    @BeforeClass
    public static void setUpClass() {
        for (int i = 1; i <= 7; ++i) {
            INITIAL.add(new MyChainable("i" + i, "", ""));
        }
    }


    private static void doTest(List<Chainable> custom, String expected) {
        List<Chainable> chain = ChainUtils.createChain(INITIAL, custom);
        StringBuilder sb = new StringBuilder();
        for (Chainable c : chain) {
            sb.append(c.getId()).append(" ");
        }
        assertEquals(expected, sb.toString().trim());
    }


    @Test
    public void testHappyCase() {
        List<Chainable> custom = Arrays.<Chainable> asList(
                new MyChainable("c1", "", ""),
                new MyChainable("c1", "", ""),
                new MyChainable("c2", "i5 i6 i7", "i3"),
                new MyChainable("c4", "c2", "")
        );
        doTest(custom, "i1 i2 i3 c4 c2 i4 i5 i6 i7 c1");
    }


    @Test(expected = ChainException.class)
    public void testBeforeEqualsToAfter() {
        List<Chainable> custom = Arrays.<Chainable> asList(
                new MyChainable("c1", "", ""),
                new MyChainable("c1", "", ""),
                new MyChainable("c2", "i5 i6 i7", "i3"),
                new MyChainable("c4", "c2", ""),
                new MyChainable("c5", "i4", "i4")        // should fail, Before==After
        );
        doTest(custom, "dummy");
    }                                               


    @Test(expected = ChainException.class)
    public void testBeforeGreaterThanAfter() {
        List<Chainable> custom = Arrays.<Chainable> asList(
                new MyChainable("c1", "", ""),
                new MyChainable("c1", "", ""),
                new MyChainable("c2", "i5 i6 i7", "i3"),
                new MyChainable("c4", "c2", ""),
                new MyChainable("c5", "i3", "i4")        // should fail, Before>After
        );
        doTest(custom, "dummy");
    }


    @Test(expected = ChainException.class)
    public void testDependencyLoop() {
        List<Chainable> custom = Arrays.<Chainable> asList(
                new MyChainable("c1", "c2", "c3"),
                new MyChainable("c2", "c1", "c3"),
                new MyChainable("c3", "c1", "c2")
        );
        doTest(custom, "dummy");
    }

}
