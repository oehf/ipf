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
package org.openehealth.ipf.commons.flow.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

/**
 * The readable String representation of the FlowMessage
 * 
 * @author Mitko Kolev
 */
@Entity
@Table(name = "T_FLOW_PART_MESSAGE")
public class FlowPartMessage implements TextMessage {

    @Id
    @Column(name = "C_ID", length = 128)
    private final String identifier; // internal

    @Field(index = Index.YES, store = Store.NO)
    @Column(name = "C_TEXT", length = Integer.MAX_VALUE)
    @Lob
    private String text;

    public FlowPartMessage() {
        this(null);
    }

    public FlowPartMessage(String text) {
        identifier = UUID.randomUUID().toString();
        this.text = text;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
