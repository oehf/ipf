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

import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;

import java.io.Serializable;

/**
 *
 */
public class SerializableEnumInteractionId<E extends Enum<E> & InteractionId> implements Serializable, InteractionId {
    private static final long serialVersionUID = -2740691943593482233L;

    private final String interaction;
    private volatile transient E interactionId;

    private SerializableEnumInteractionId(E interactionId) {
        this.interactionId = interactionId;
        this.interaction = interactionId.getClass().getName() + "$" + interactionId.name();
    }

    /**
     * Create a serializable version of the given {@link InteractionId}
     *
     * @param interactionId interactionID
     * @param <E>           interaction ID type, which must be an enum
     * @return serializable version of the given {@link InteractionId}
     */
    public static <E extends Enum<E> & InteractionId> SerializableEnumInteractionId create(E interactionId) {
        return new SerializableEnumInteractionId(interactionId);
    }

    private static <E extends Enum<E> & InteractionId> E getInteractionId(String interaction) {
        int lastIndex = interaction.lastIndexOf('$');
        Class<E> enumType = null;
        try {
            enumType = (Class<E>) Class.forName(interaction.substring(0, lastIndex));
            String enumValue = interaction.substring(lastIndex + 1);
            return Enum.valueOf(enumType, enumValue);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @return the (non-serializable) interactionId
     */
    public E getInteractionId() {
        if (interactionId == null) {
            interactionId = getInteractionId(interaction);
        }
        return interactionId;
    }

    /*
    @Override
    public String getName() {
        return getInteractionId().getName();
    }

    @Override
    public String getDescription() {
        return getInteractionId().getDescription();
    }

    @Override
    public boolean isQuery() {
        return getInteractionId().isQuery();
    }

    @Override
    public <T extends AuditDataset> AuditStrategy<T> getClientAuditStrategy() {
        return getInteractionId().getClientAuditStrategy();
    }

    @Override
    public <T extends AuditDataset> AuditStrategy<T> getServerAuditStrategy() {
        return getInteractionId().getServerAuditStrategy();
    }
    */
}
