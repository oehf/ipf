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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import com.ctc.wstx.sr.CompactNsContext;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.*;

/**
 * Validation profile interface for XDS-like transactions.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public interface ValidationProfile {

    public static enum InteractionProfile {
        XDS_A, XDS_B, XCA, Continua_HRN;
    }

    /**
     * @return <code>true</code> if checks are done for query transactions.
     */
    boolean isQuery();

    /**
     * @return ID of the eHealth transaction.
     */
    InteractionId getInteractionId();

    /**
     * @return ID of interaction profile the transaction belongs to.
     */
    InteractionProfile getProfile();

    /**
     * @return <code>true</code> when the transaction uses ebXML 3.0.
     */
    boolean isEbXml30Based();
}
