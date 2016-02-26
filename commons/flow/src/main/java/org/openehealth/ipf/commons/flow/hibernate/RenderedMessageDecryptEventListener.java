/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.flow.hibernate;

import java.io.IOException;
import java.io.Serializable;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.openehealth.ipf.commons.flow.domain.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Decrypts flow (part) message text on insert, update and load.
 * 
 * @see PostUpdateEventListener
 * @see PostLoadEventListener
 * @see PostInsertEventListener
 * @see StringEncryptor
 * 
 * @author Mitko Kolev
 */
public class RenderedMessageDecryptEventListener implements
        PostLoadEventListener, 
        PostInsertEventListener,
        PostUpdateEventListener{

    private static final long serialVersionUID = -7516699694816986560L;

    @Autowired
    private transient StringEncryptor stringEncryptor;

    public StringEncryptor getStringEncryptor() {
        return stringEncryptor;
    }

    public void setStringEncryptor(StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        Object entity = event.getEntity();
        Serializable id = event.getId();
        decrypt(entity, id, null);
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        decrypt(event.getEntity(), event.getId(), event.getState());
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        decrypt(event.getEntity(), event.getId(), event.getState());
    }

    protected void decrypt(Object entityObject, Serializable id, Object[] state) {
        if (entityObject instanceof TextMessage) {
            TextMessage entity = (TextMessage) entityObject;
            if (state != null) {
                // change the state, and the entity. (pre- listeners)
                if (state[0] instanceof String) {
                    String encrypted = (String) state[0];
                    String decryptedString = stringEncryptor.decrypt(encrypted);
                    state[0] = decryptedString;
                    entity.setText(decryptedString);
                }
            } else {
                // change only the object entity (post-load listener)
                String text = entity.getText();
                String decryptedString = stringEncryptor.decrypt(text);
                entity.setText(decryptedString);
            }
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

}
