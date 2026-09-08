/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.boot.hl7v2;

import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.modules.hl7.config.CustomModelClasses;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

/**
 * Asserts that a Spring Boot application can contribute custom HL7v2 model classes by simply
 * declaring a {@link CustomModelClasses} bean -- which was not possible before IPF 6.0.0,
 * because the starter registered no collector for them.
 *
 * @author Christian Ohr
 * @since 6.0
 */
@SpringBootTest(classes = { TestApplication.class },
        properties = "spring.application.name=hl7v2-custom-model-classes-test")
public class IpfHl7v2CustomModelClassesTest {

    @Autowired
    private CustomModelClassFactory modelClassFactory;

    @Autowired
    private HapiContext hapiContext;

    @Test
    public void testCustomModelClassesAreCollected() {
        var modelClasses = modelClassFactory.getCustomModelClasses();
        assertThat(modelClasses, hasKey("2.5"));
        assertThat(modelClasses.get("2.5"), hasItemInArray(TestApplication.CUSTOM_PACKAGE));
    }

    /**
     * The contributed packages are appended after the auto-configured ones, which is what gives
     * them precedence: HAPI's {@code findClass} does not stop at the first hit, so the last
     * matching package in the array wins.
     */
    @Test
    public void testModelClassesOfTheAutoConfiguredFactoryAreKept() {
        var modelClasses = modelClassFactory.getCustomModelClasses();
        assertThat(modelClasses.get("2.3.1"), arrayContaining(
                "org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v231"));
        assertThat(modelClasses.get("2.5"), arrayContaining(
                "org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25",
                "org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25",
                TestApplication.CUSTOM_PACKAGE));
    }

    /**
     * The auto-configured package names are only useful if they actually resolve -- they used to
     * be assembled without the separating dot, so IPF's own PIX/PDQ structures were never found
     * and this HapiContext silently fell back to the HAPI default model classes.
     * <p>
     * Note that {@code QBP_Q21} exists in both the PIX and the PDQ package, and that the PIX one
     * wins here because HAPI keeps the last match rather than the first. That only concerns this
     * general purpose HapiContext: the IHE transaction endpoints do not use it, they carry their
     * own single-profile factory built by {@code CustomModelClassUtils.createFactory()}.
     */
    @Test
    public void testAutoConfiguredModelClassesResolve() throws Exception {
        assertMessageClass("RSP_K21", "2.5",
                "org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.RSP_K21");
        assertMessageClass("RSP_K23", "2.5",
                "org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.RSP_K23");
        assertMessageClass("QBP_Q21", "2.5",
                "org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.QBP_Q21");
        assertMessageClass("ADT_A01", "2.3.1",
                "org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v231.message.ADT_A01");
    }

    private void assertMessageClass(String name, String version, String expected) throws Exception {
        Class<? extends Message> messageClass = modelClassFactory.getMessageClass(name, version, true);
        assertThat(messageClass.getCanonicalName(), is(expected));
    }

    @Test
    public void testContributionsReachTheFactoryUsedByTheHapiContext() {
        assertThat(hapiContext.getModelClassFactory(), is(sameInstance(modelClassFactory)));
    }

}
