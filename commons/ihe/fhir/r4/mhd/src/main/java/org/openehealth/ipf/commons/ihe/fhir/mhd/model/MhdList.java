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

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.OidType;
import org.ietf.jgss.Oid;
import org.openehealth.ipf.commons.core.URN;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.MHD_LIST;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.MHD_LIST_PROFILE;

@SuppressWarnings("unchecked")
@ResourceDef(name = "List", id = "mhdList", profile = MHD_LIST_PROFILE)
public class MhdList<T extends MhdList<T>> extends ListResource {

    public MhdList() {
        super();
        setDate(new Date());
        setStatus(ListStatus.CURRENT);
        setMode(ListMode.WORKING);
        MHD_LIST.setProfile(this);
    }

    @Child(name = "homeCommunityId", type = OidType.class, order = 10)
    @Extension(url = MhdProfile.HOME_COMMUNITY_ID_PROFILE, definedLocally = false)
    @Description(shortDefinition = "The homeCommunityId where the artifact resides")
    private OidType homeCommunityId;

    /**
     * Returns the community this List resides in, as introduced by MHD 4.2.4 with the Target
     * Communities Option (CP-ITI-1326-02).
     *
     * @return the homeCommunityId, never {@code null}
     */
    public OidType getHomeCommunityId() {
        if (homeCommunityId == null) {
            homeCommunityId = new OidType();
        }
        return homeCommunityId;
    }

    public boolean hasHomeCommunityId() {
        return homeCommunityId != null && !homeCommunityId.isEmpty();
    }

    public T setHomeCommunityId(OidType homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
        return (T)this;
    }

    /**
     * Sets the community this List resides in. The OID is rendered as a URN, as the {@code oid}
     * data type requires.
     *
     * @param oid homeCommunityId
     * @return this object
     */
    public T setHomeCommunityId(Oid oid) {
        this.homeCommunityId = new OidType(new URN(oid).toString());
        return (T)this;
    }

    /**
     * Adds an identifier to be a EntryUuid as required by the profile. Since MHD 4.2.4 this slice is
     * restricted to one occurrence, so an already present EntryUuid identifier is replaced.
     *
     * @param uuid uuid
     * @return this object
     */
    public T setEntryUuidIdentifier(UUID uuid) {
        MhdIdentifierType.ENTRY_UUID.removeFrom(getIdentifier());
        getIdentifier().add(new EntryUuidIdentifier(uuid));
        return (T)this;
    }

    /**
     * Returns the EntryUuid identifier, recognized either by its {@code type} coding (MHD 4.2.4) or
     * by its {@code use} (MHD 4.2.3 and earlier).
     *
     * @return the EntryUuid identifier, if present
     */
    public Optional<Identifier> getEntryUuidIdentifier() {
        return MhdIdentifierType.ENTRY_UUID.find(getIdentifier());
    }

    /**
     * Returns the Unique Id identifier, recognized either by its {@code type} coding (MHD 4.2.4) or
     * by its {@code use} (MHD 4.2.3 and earlier).
     *
     * @return the UniqueId identifier, if present
     */
    public Optional<Identifier> getUniqueIdIdentifier() {
        return MhdIdentifierType.UNIQUE_ID.find(getIdentifier());
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(homeCommunityId);
    }

    @Override
    public void copyValues(ListResource dst) {
        super.copyValues(dst);
        if (dst instanceof MhdList<?> mhdList) {
            mhdList.homeCommunityId = homeCommunityId == null ? null : homeCommunityId.copy();
        }
    }

    @Override
    public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
            return false;
        if (!(other_ instanceof MhdList<?> other))
            return false;
        return compareDeep(homeCommunityId, other.homeCommunityId, true);
    }

}
