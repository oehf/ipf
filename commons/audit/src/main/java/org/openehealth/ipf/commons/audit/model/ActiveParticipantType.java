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
package org.openehealth.ipf.commons.audit.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.codes.NetworkAccessPointTypeCode;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.MediaType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
public class ActiveParticipantType implements Serializable, Validateable {

    /**
     * <p>Indicator that the user is or is not the requestor, or initiator, for the event being audited.</p>
     * <p>Used to identify which of the participants initiated the transaction being audited.
     * If the audit source cannot determine which of the participants is the requestor,
     * then the field shall be present with the value FALSE in all participants. The system shall not identify
     * multiple participants as UserIsRequestor. If there are several known requestors, the reporting system shall pick
     * only one as UserIsRequestor.</p>
     */
    @Getter
    private final boolean userIsRequestor;

    /**
     * <p>Unique identifier for the user actively participating in the event.</p>
     * <p>
     * If the participant is a person, then the User ID shall be the identifier used for that person on this particular system,
     * in the form of loginName@domain-name. If the participant is an identifiable process, the UserID selected shall be one of the
     * identifiers used in the internal system logs. For example, the User ID may be the process ID as used within the local
     * operating system in the local system logs. If the participant is a node, then User ID may be the node name assigned by the
     * system administrator. Other participants such as threads, relocatable processes, web service end-points,
     * web server dispatchable threads, etc. will have an appropriate identifier.
     * </p>
     * <p>
     * The implementation shall document in the conformance statement the identifiers used, see Section A.6.
     * The purpose of this requirement is to allow matching of the audit log identifiers with internal system logs on the reporting systems.
     * </p>
     * <p>
     * When importing or exporting data, e.g., by means of media, the UserID field is used both to identify people and to
     * identify the media itself. When the Role ID Code is EV(110154, DCM, "Destination Media") or EV(110155, DCM, "Source Media"),
     * the UserID may be:
     * <ul>
     * <li>a URI (the preferred form) identifying the source or destination</li>
     * <li>an email address of the form "mailto:user@address"</li>
     * <li>a description of the media type (e.g., DVD) together with a description of its identifying label, as a free text field</li>
     * <li>a description of the media type (e.g., paper, film) together with a description of the location of the media creator (i.e., the printer)</li>
     * </ul>
     * <p>
     * The UserID field for Media needs to be highly flexible given the large variety of media and transports that might be used.
     * </p>
     */
    @Getter
    private final String userID;

    /**
     * <p>
     * Alternative unique identifier for the user.
     * </p>
     * <p>
     * If the participant is a person, then Alternative User ID shall be the identifier used for that person within an enterprise
     * for authentication purposes, for example, a Kerberos Username (user@realm). If the participant is a DICOM application,
     * then Alternative User ID shall be one or more of the AE Titles that participated in the event.
     * </p>
     * <p>
     * Multiple AE titles shall be encoded as:
     * </p>
     * <pre>
     * AETITLES= aetitle1;aetitle2;…
     * </pre>
     * <p>
     * When importing or exporting data, e.g., by means of media, the Alternative UserID field is used either to identify people
     * or to identify the media itself. When the Role ID Code is (110154, DCM, "Destination Media") or (110155, DCM, "Source Media"),
     * the Alternative UserID may be any machine readable identifications on the media, such as media serial number, volume label,
     * or DICOMDIR SOP Instance UID.
     * </p>
     */
    @Getter
    @Setter
    private String alternativeUserID;

    /**
     * A human readable identification of the participant. If the participant is a person, the person's name shall be used.
     * If the participant is a process, then the process name shall be used.
     */
    @Getter
    @Setter
    private String userName;

    /**
     * <p>
     * An identifier for the network access point of the user device This could be a device id, IP address,
     * or some other identifier associated with a device.
     * </p>
     * <p>
     * The NetworkAccessPointTypeCode and NetworkAccessPointID can be ambiguous for systems that have multiple physical network
     * connections. For these multi-homed nodes a single DNS name or IP address shall be selected and used when reporting audit
     * events. DICOM does not require the use of a specific method for selecting the network connection to be used for identification,
     * but it must be the same for all of the audit messages generated for events on that node.
     * </p>
     */
    @Getter
    @Setter
    private String networkAccessPointID;

    /**
     * An identifier for the type of network access point.
     */
    @Getter
    @Setter
    private NetworkAccessPointTypeCode networkAccessPointTypeCode;

    private List<ActiveParticipantRoleId> roleIDCodes;

    /**
     * Volume ID, URI, or other identifier for media. Often required if digital media. May be present otherwise.
     */
    @Getter
    @Setter
    private String mediaIdentifier;

    @Getter
    @Setter
    private MediaType mediaType;

    public ActiveParticipantType(String userId, boolean userIsRequestor) {
        this.userID = requireNonNull(userId, "userId must be not null");
        this.userIsRequestor = userIsRequestor;
    }

    /**
     * Specification of the role(s) the user plays when performing the event,
     * as assigned in role-based access control security.
     *
     * @return the role(s) the user plays when performing the event
     */
    public List<ActiveParticipantRoleId> getRoleIDCodes() {
        if (roleIDCodes == null) {
            roleIDCodes = new ArrayList<>();
        }
        return this.roleIDCodes;
    }

    @Override
    public void validate() {
        // no special rules
    }
}
