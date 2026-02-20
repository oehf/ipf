/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.ListResource;
import org.openehealth.ipf.commons.ihe.fhir.mhd.Mhd423;

import java.util.Date;
import java.util.UUID;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.MHD_LIST;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.MHD_LIST_PROFILE;

@SuppressWarnings("unchecked")
@ResourceDef(name = "List", id = "mhdList", profile = MHD_LIST_PROFILE)
public class MhdList<T extends MhdList<T>> extends ListResource implements Mhd423 {

    public MhdList() {
        super();
        setDate(new Date());
        setStatus(ListStatus.CURRENT);
        setMode(ListMode.WORKING);
        MHD_LIST.setProfile(this);
    }

    /**
     * Adds an identifier to be a EntryUuid as required by the profile
     * @param uuid uuid
     * @return this object
     */
    public T setEntryUuidIdentifier(UUID uuid) {
        getIdentifier().add(new EntryUuidIdentifier(uuid));
        return (T)this;
    }

    @Override
    public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
            return false;
        return other_ instanceof MhdList;
    }

}
