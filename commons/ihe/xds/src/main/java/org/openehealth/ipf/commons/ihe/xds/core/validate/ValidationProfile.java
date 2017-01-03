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

import org.openehealth.ipf.commons.ihe.xds.XdsInteractionId;
import org.openehealth.ipf.commons.ihe.xds.XdsIntegrationProfile;

/**
 * Validation profile for XDS-like transactions.
 * This has become an interface in IPF 3.2 and is implemented by subclasses of
 * {@link XdsInteractionId XDS interaction IDs}
 *
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 * @author Michael Ottati
 *
 */
public interface ValidationProfile {

    /**
     * @return <code>true</code> if checks are done for query transactions.
     */
    boolean isQuery();


    /**
     * @return ID of the eHealth transaction.
     */
    XdsInteractionId getInteractionId();


    /**
     * @return ID of interaction profile the transaction belongs to.
     */
    XdsIntegrationProfile getInteractionProfile();


    /**
     * @return <code>true</code> when the transaction uses ebXML 3.0.
     */
    default boolean isEbXml30Based() {
        return getInteractionProfile().isEbXml30Based();
    }

    default boolean isPartOf(Class<? extends XdsIntegrationProfile> clazz) {
        return clazz.isAssignableFrom(getInteractionProfile().getClass());
    }

}
