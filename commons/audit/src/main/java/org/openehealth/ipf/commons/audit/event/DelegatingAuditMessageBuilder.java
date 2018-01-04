/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit.event;

import org.openehealth.ipf.commons.audit.model.AuditMessage;

import static java.util.Objects.requireNonNull;

/**
 * AuditMessageBuilder that wraps a DICOM {@link BaseAuditMessageBuilder} or a subclass thereof.
 * The intention is that subclasses provide their interaction-specific API for building audit
 * messages that is more closely aligned (in terms of wording) with what is specified in DICOM.
 *
 * @author Christian Ohr
 */
public abstract class DelegatingAuditMessageBuilder<T extends DelegatingAuditMessageBuilder<T, D>, D extends BaseAuditMessageBuilder<D>> implements AuditMessageBuilder<T> {

    protected final D delegate;

    public DelegatingAuditMessageBuilder(D delegate) {
        this.delegate = requireNonNull(delegate);
    }

    @Override
    public AuditMessage getMessage() {
        return delegate.getMessage();
    }

    @Override
    public void validate() {
        delegate.validate();
    }

    protected T self() {
        return (T) this;
    }
}
