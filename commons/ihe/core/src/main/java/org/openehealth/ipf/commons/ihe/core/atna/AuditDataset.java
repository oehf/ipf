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
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic data structure used to store information pieces 
 * needed for ATNA auditing.
 * 
 * @author Dmytro Rud
 */
public class AuditDataset implements Serializable {
    private static final long serialVersionUID = -2919172035448943710L;

    private static final transient Log LOG = LogFactory.getLog(AuditDataset.class);

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

    /**
     * <i>"What you see is what I get"</i>&nbsp;&mdash; returns a string that
     * consists from all fields available through getter methods.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Checks whether this audit dataset contains non-null values in the fields
     * from the given list.
     * 
     * @param fieldNames
     *            a list of field names with first letter capitalized, e.g.
     *            "Address"
     * @param positiveCheck
     *            {@code true} when the given fields must be present;
     *            {@code false} when they must be absent.
     * @return a set of names of the fields which do not match the given
     *         condition (i.e. are absent when they must be present, and vice
     *         versa).
     * @throws Exception
     *             on reflection errors
     */
    public Set<String> checkFields(String[] fieldNames, boolean positiveCheck) throws Exception {
        Set<String> result = new HashSet<String>();

        for (String fieldName : fieldNames) {
            Method m = getClass().getMethod("get" + fieldName);
            Object o = m.invoke(this);
            if ((o == null) == positiveCheck) {
                result.add(fieldName);
            }
        }

        return result;
    }


    /**
     * Checks whether this audit dataset misses some fields, when 
     * yes&nbsp;&mdash; writes a corresponding message into the log.
     * 
     * @param fieldNames
     *            a list of field names with first letter capitalized, e.g.
     *            "Address"
     * @param positiveCheck
     *            {@code true} when the given fields must be present;
     *            {@code false} when they must be absent.
     * @param allowIncompleteAudit
     *            {@code true} when the incomplete audit records are allowed;
     *            {@code false} otherwise.
     * @return
     *            {@code true} when the auditing can be performed;
     *            {@code false} otherwise.
     * @throws Exception
     *            on reflection errors.
     */
    public boolean isAuditingPossible(
            String[] fieldNames, 
            boolean positiveCheck,
            boolean allowIncompleteAudit) throws Exception
    {
        Set<String> missing = checkFields(fieldNames, positiveCheck);
        if(! missing.isEmpty()) {
            StringBuilder sb = new StringBuilder("Missing audit fields: ");
            for(String fieldName : missing) {
                sb.append(fieldName).append(", ");
            }
            sb.append(allowIncompleteAudit ? 
                "but incomplete audit is allowed, so we'll perform it." :
                "auditing not possible.");
            LOG.error(sb.toString());
        }
        
        return missing.isEmpty() || allowIncompleteAudit;
    }
    
}
