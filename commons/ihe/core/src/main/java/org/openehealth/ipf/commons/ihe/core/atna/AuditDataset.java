/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.atna;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A generic data structure used to store information pieces 
 * needed for ATNA auditing.
 * 
 * @author Dmytro Rud
 */
public class AuditDataset implements Serializable {
    private static final long serialVersionUID = -2919172035448943710L;

    private static final transient Logger LOG = LoggerFactory.getLogger(AuditDataset.class);

    // whether we audit on server (true) or on client (false)
    private final boolean serverSide;


    /**
     * Constructor.
     * 
     * @param serverSide
     *            specifies whether this audit dataset will be used on the
     *            server side (<code>true</code>) or on the client side 
     *            (<code>false</code>)
     */
    public AuditDataset(boolean serverSide) {
        this.serverSide = serverSide;
    }

    public boolean isServerSide() {
        return serverSide;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
