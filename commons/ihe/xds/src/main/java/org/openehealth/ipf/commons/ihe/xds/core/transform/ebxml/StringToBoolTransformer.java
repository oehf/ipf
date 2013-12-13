/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Boris Stanojevic
 */
public class StringToBoolTransformer {

    private static final String YES = "yes";
    private static final String NO = "no";

    /**
     *
     * @param booleanValue given {@link Boolean}
     * @return "yes"|"no" otherwise <code>null</code>
     */
    public String toEbXML(Boolean booleanValue) {
        if (booleanValue == null) {
            return null;
        }
        return booleanValue? YES : NO;
    }

    /**
     *
     * @param slotValue given slotValue which can only have "yes"|"no" values
     * @return <code>true</code>|<code>false</code> otherwise <code>null</code>
     */
    public Boolean fromEbXML(String slotValue){
        if (StringUtils.isBlank(slotValue)){
            return null;
        }
        if (YES.equals(slotValue.trim().toLowerCase())){
            return new Boolean(true);
        } else if (NO.equals(slotValue.trim().toLowerCase())) {
            return new Boolean(false);
        }
        return null;
    }
}
