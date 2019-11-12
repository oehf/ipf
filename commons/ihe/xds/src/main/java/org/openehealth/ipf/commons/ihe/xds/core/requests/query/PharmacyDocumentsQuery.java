package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.TimeRange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Base class for Pharmacy Documents Queries (PHARM-1).
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PharmacyDocumentsQuery", propOrder = {
        "patientId", "serviceStartTime", "serviceStopTime", "status", "metadataLevel" })
@XmlRootElement(name = "pharmacyDocumentsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public abstract class PharmacyDocumentsQuery extends StoredQuery implements PatientIdBasedStoredQuery {
    private static final long serialVersionUID = -4878731956719028791L;

    @Getter @Setter private Identifiable patientId;
    @Getter private final TimeRange serviceStartTime = new TimeRange();
    @Getter private final TimeRange serviceStopTime = new TimeRange();
    @Getter @Setter private List<AvailabilityStatus> status;
    @Getter @Setter private Integer metadataLevel;

    /**
     * For JAXB serialization only.
     */
    public PharmacyDocumentsQuery() {
    }

    /**
     * Constructs the query.
     * @param queryType
     *          the type of the query.
     */
    protected PharmacyDocumentsQuery(final QueryType queryType) {
        super(queryType);
    }
}
