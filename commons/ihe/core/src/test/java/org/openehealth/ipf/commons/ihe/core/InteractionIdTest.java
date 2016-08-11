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
import org.junit.Test;
import org.openehealth.ipf.commons.core.config.SimpleRegistry;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.openehealth.ipf.commons.ihe.core.InteractionIdTest.MyInteractionProfile.Interactions.Interaction1;
import static org.openehealth.ipf.commons.ihe.core.InteractionIdTest.MyInteractionProfile.Interactions.Interaction2;

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

        MyInteractionProfile.Interactions original = Interaction1;
        SerializableEnumInteractionId<?> serializableEnumInteractionId = SerializableEnumInteractionId.create(original);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(serializableEnumInteractionId);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        SerializableEnumInteractionId<?> deserialized = (SerializableEnumInteractionId<?>) ois.readObject();

        assertSame(original, deserialized.getInteractionId());
    }

    private static final MyInteractionProfile MY_INSTANCE = new MyInteractionProfile();
    private static final SimpleRegistry SIMPLE_REGISTRY = new SimpleRegistry();


    static class MyInteractionProfile implements InteractionProfile {

        @AllArgsConstructor
        enum Interactions implements InteractionId {

            Interaction1("int1", "description1"),
            Interaction2("int2", "description2");

            @Getter
            private String name;
            @Getter
            private String description;

            @Override
            public boolean isQuery() {
                return false;
            }

            @Override
            public <T extends AuditDataset> AuditStrategy<T> getClientAuditStrategy() {
                return null;
            }

            @Override
            public <T extends AuditDataset> AuditStrategy<T> getServerAuditStrategy() {
                return null;
            }
        }

        @Override
        public List<InteractionId> getInteractionIds() {
            return Arrays.asList(Interactions.values());
        }

    }

    private static final OtherInteractionProfile OTHER_INSTANCE = new OtherInteractionProfile();

    static class OtherInteractionProfile implements InteractionProfile {

        @AllArgsConstructor
        enum Interactions implements InteractionId {

            Interaction3("int3", "description3"),
            Interaction4("int4", "description4");

            @Getter
            private String name;
            @Getter
            private String description;

            @Override
            public boolean isQuery() {
                return false;
            }

            @Override
            public <T extends AuditDataset> AuditStrategy<T> getClientAuditStrategy() {
                return null;
            }

            @Override
            public <T extends AuditDataset> AuditStrategy<T> getServerAuditStrategy() {
                return null;
            }
        }

        @Override
        public List<InteractionId> getInteractionIds() {
            return Arrays.asList(Interactions.values());
        }

    }
}
