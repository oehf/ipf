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
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.jasypt.encryption.StringEncryptor;
import org.openehealth.ipf.commons.flow.domain.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Encrypts flow (part) message text on insert or update.
 * 
 * @see PreInsertEventListener
 * @see PreUpdateEventListener
 * @see StringEncryptor
 * 
 * @author Mitko Kolev
 */
public class RenderedMessageEncryptEventListener implements
        PreInsertEventListener, 
        PreUpdateEventListener, 
        Initializable {

    private static final long serialVersionUID = -7516699694816986560L;

    @Autowired
    private StringEncryptor stringEncryptor;

    public StringEncryptor getStringEncryptor() {
        return stringEncryptor;
    }

    public void setStringEncryptor(StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }
    
    @Override
    public void initialize(Configuration cfg) {
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        Object entity = event.getEntity();
        Serializable id = event.getId();
        encrypt(entity, id, event.getState());
        return false;
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        Object entity = event.getEntity();
        Serializable id = event.getId();
        encrypt(entity, id, event.getState());
        return false;
    }

    protected void encrypt(Object entityObject, Serializable id, Object[] state) {
        if (entityObject instanceof TextMessage) {
            TextMessage entity = (TextMessage) entityObject;
            if (state[0] instanceof String) {
                    String decrypted = ((String) state[0]);
                    String encryptedString = stringEncryptor.encrypt(decrypted);
                    state[0] = encryptedString;
                    entity.setText(encryptedString);
            }
        }
    }

}
