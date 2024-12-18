/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.core.config.SimpleRegistry;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.openehealth.ipf.commons.ihe.core.InteractionIdTest.MyIntegrationProfile.Interactions.Interaction1;
import static org.openehealth.ipf.commons.ihe.core.InteractionIdTest.MyIntegrationProfile.Interactions.Interaction2;

/**
 *
 */
public class InteractionIdTest {

    @Test
    public void testIsProfileFor() {
        assertTrue(MY_INSTANCE.isProfileFor(Interaction1));
        assertFalse(OTHER_INSTANCE.isProfileFor(Interaction1));
    }

    @Test
    public void testGetInteractionIds() {
        assertTrue(MY_INSTANCE.getInteractionIds().contains(Interaction1));
        assertTrue(MY_INSTANCE.getInteractionIds().contains(Interaction2));
        assertFalse(OTHER_INSTANCE.getInteractionIds().contains(Interaction1));
    }

    @Test
    public void testReconstruct() throws IOException, ClassNotFoundException {

        var original = Interaction1;
        SerializableEnumInteractionId<?> serializableEnumInteractionId = SerializableEnumInteractionId.create(original);
        var baos = new ByteArrayOutputStream();
        var oos = new ObjectOutputStream(baos);
        oos.writeObject(serializableEnumInteractionId);

        var bais = new ByteArrayInputStream(baos.toByteArray());
        var ois = new ObjectInputStream(bais);
        SerializableEnumInteractionId<?> deserialized = (SerializableEnumInteractionId<?>) ois.readObject();

        assertSame(original, deserialized.getInteractionId());
    }

    private static final MyIntegrationProfile MY_INSTANCE = new MyIntegrationProfile();
    private static final SimpleRegistry SIMPLE_REGISTRY = new SimpleRegistry();


    static class MyIntegrationProfile implements IntegrationProfile {

        @AllArgsConstructor
        enum Interactions implements InteractionId {

            Interaction1("int1", "description1"),
            Interaction2("int2", "description2");

            @Getter
            private final String name;
            @Getter
            private final String description;

        }

        @Override
        public List<InteractionId> getInteractionIds() {
            return Arrays.asList(Interactions.values());
        }

    }

    private static final OtherIntegrationProfile OTHER_INSTANCE = new OtherIntegrationProfile();

    static class OtherIntegrationProfile implements IntegrationProfile {

        @AllArgsConstructor
        enum Interactions implements InteractionId {

            Interaction3("int3", "description3"),
            Interaction4("int4", "description4");

            @Getter
            private final String name;
            @Getter
            private final String description;

        }

        @Override
        public List<InteractionId> getInteractionIds() {
            return Arrays.asList(Interactions.values());
        }

    }
}
