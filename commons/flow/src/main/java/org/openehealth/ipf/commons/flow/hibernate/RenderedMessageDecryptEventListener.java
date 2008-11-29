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

import java.io.Serializable;

import org.hibernate.cfg.Configuration;
import org.hibernate.event.Initializable;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.jasypt.encryption.StringEncryptor;
import org.openehealth.ipf.commons.flow.domain.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Decrypts a FlowMessage on update,load and insert.
 * 
 * @see PostUpdateEventListener
 * @see PostLoadEventListener
 * @see PostInsertEventListener
 * @see StringEncryptor
 * 
 * @author Mitko Kolev
 */
public class RenderedMessageDecryptEventListener implements
        PostLoadEventListener, PostInsertEventListener,
        PostUpdateEventListener, Initializable {

    @Autowired
    private StringEncryptor stringEncryptor;

    public RenderedMessageDecryptEventListener() {
    }

    /**
     * Generated
     */
    private static final long serialVersionUID = -7516699694816986560L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.hibernate.event.PostLoadEventListener#onPostLoad(org.hibernate.event
     * .PostLoadEvent)
     */
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

    public StringEncryptor getStringEncryptor() {
        return stringEncryptor;
    }

    public void setStringEncryptor(StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }

    /**
     * Decrypts with the given cryptoService
     * 
     * @param cryptoService
     * @param entityObject
     * @param id
     * @param state
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    @Override
    public void initialize(Configuration cfg) {
        if (stringEncryptor == null) {
            throw new IllegalArgumentException(
                    "Cannot initialize the RenderedMessageDecryptEventListener, "
                            + " no org.jasypt.encryption.StringEncryptor is provided in the configuration!"
                            + "If you do not wish to use encryption, "
                            + "remove the listener from the configuration!");
        }
    }
}
