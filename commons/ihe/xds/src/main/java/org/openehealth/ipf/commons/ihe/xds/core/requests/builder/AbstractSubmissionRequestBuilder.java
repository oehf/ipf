/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.requests.builder;

import lombok.NonNull;
import org.openehealth.ipf.commons.core.URN;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationLabel;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Abstract builder to support build for certain types of
 *
 * @param <T> The Type of builder.
 * @param <R> The Type of class to build.
 * @param <D> The Type of documents currently processed.
 */
@SuppressWarnings("unchecked")
abstract class AbstractSubmissionRequestBuilder<T extends AbstractSubmissionRequestBuilder<T, R, D>, R, D> {
    private final SubmissionSet submissionSet;
    private final boolean autoGenerate;
    private final Function<D, DocumentEntry> documentMapper;
    private final List<Folder> folders = new ArrayList<>();
    private final List<D> documents = new ArrayList<>();
    private final List<Association> associations = new ArrayList<>();

    public AbstractSubmissionRequestBuilder(boolean autoGenerate, @NonNull SubmissionSet submissionSet,
            Function<D, DocumentEntry> documentMapper) {
        this.autoGenerate = autoGenerate;
        this.submissionSet = submissionSet;
        this.documentMapper = documentMapper;
    }

    public T withDocument(D doc) {
        this.documents.add(doc);
        return (T) this;
    }

    public T withDocuments(List<D> docs) {
        this.documents.addAll(docs);
        return (T) this;
    }

    public T withFolder(Folder folder) {
        this.folders.add(folder);
        return (T) this;
    }

    public T withFolders(List<Folder> folders) {
        this.folders.addAll(folders);
        return (T) this;
    }

    public T withAssociation(Association association) {
        this.associations.add(association);
        return (T) this;
    }

    public T withAssociations(List<Association> associations) {
        this.associations.addAll(associations);
        return (T) this;
    }

    abstract R doBuild(SubmissionSet submissionSet, List<Folder> folders, List<D> documents,
            List<Association> associations);

    public R build() {
        if (autoGenerate) {
            supportivePostProcess();
        }
        return doBuild(submissionSet, folders, documents, associations);
    }

    /**
     * Supportive operation for constructing a submission request (ITI-41 + ITI-42).
     * The following information will be set if not defined:
     * SubmissionSet.submissionTime, UniqueId's, EntryUuid's and HasMember
     * associations
     */
    private void supportivePostProcess() {
        var docEntries = documents.stream().map(documentMapper).toList();
        assignDefault(folders, XDSMetaClass::getUniqueId, XDSMetaClass::assignUniqueId);
        assignDefault(folders, XDSMetaClass::getEntryUuid, XDSMetaClass::assignEntryUuid);
        assignDefault(docEntries, XDSMetaClass::getUniqueId, XDSMetaClass::assignUniqueId);
        assignDefault(docEntries, XDSMetaClass::getEntryUuid, XDSMetaClass::assignEntryUuid);
        assignDefault(associations, Association::getEntryUuid, Association::assignEntryUuid);
        assignDefault(submissionSet, XDSMetaClass::getUniqueId, XDSMetaClass::assignUniqueId);
        assignDefault(submissionSet, XDSMetaClass::getEntryUuid, XDSMetaClass::assignEntryUuid);
        assignDefault(submissionSet, SubmissionSet::getSubmissionTime, (set) -> set.setSubmissionTime(Timestamp.now()));

        associations.addAll(docEntries.stream()
                .filter(metadata -> !hasAssociationFromSubmissionSetWithHasMemberTo(metadata.getEntryUuid()))
                .map(metadata -> createHasMemberAssocationWithOriginalLabel(metadata.getEntryUuid())).toList());
        associations.addAll(folders.stream()
                .filter(metadata -> !hasAssociationFromSubmissionSetWithHasMemberTo(metadata.getEntryUuid()))
                .map(metadata -> createHasMemberAssocation(metadata.getEntryUuid())).toList());
        associations.addAll(associations.stream()
                .filter(assoc -> AssociationType.HAS_MEMBER.equals(assoc.getAssociationType()))
                .filter(assoc -> !Objects.equals(assoc.getSourceUuid(), submissionSet.getEntryUuid()))
                .filter(assoc -> !hasAssociationFromSubmissionSetWithHasMemberTo(assoc.getEntryUuid()))
                .map(assoc -> createHasMemberAssocation(assoc.getEntryUuid())).toList());
    }

    private Association createHasMemberAssocationWithOriginalLabel(String entryUuid) {
        var assoc = createHasMemberAssocation(entryUuid);
        assoc.setLabel(AssociationLabel.ORIGINAL);
        return assoc;
    }

    private Association createHasMemberAssocation(String entryUuid) {
        return new Association(AssociationType.HAS_MEMBER, new URN(UUID.randomUUID()).toString(),
                submissionSet.getEntryUuid(), entryUuid);

    }

    private boolean hasAssociationFromSubmissionSetWithHasMemberTo(String entryUuid) {
        return associations.stream()
                .anyMatch(assoc -> AssociationType.HAS_MEMBER.equals(assoc.getAssociationType())
                        && Objects.equals(assoc.getSourceUuid(), submissionSet.getEntryUuid())
                        && Objects.equals(assoc.getTargetUuid(), entryUuid));
    }

    protected static <T> void assignDefault(T objectToCheck, Function<T, Object> nullCheck,
            Consumer<T> defaultAssignment) {
        if (nullCheck.apply(objectToCheck) == null) {
            defaultAssignment.accept(objectToCheck);
        }
    }

    private static <T> void assignDefault(List<T> listToCheck, Function<T, Object> nullCheck,
            Consumer<T> defaultAssignment) {
        listToCheck.stream().filter(l -> nullCheck.apply(l) == null).forEach(defaultAssignment);
    }

}
