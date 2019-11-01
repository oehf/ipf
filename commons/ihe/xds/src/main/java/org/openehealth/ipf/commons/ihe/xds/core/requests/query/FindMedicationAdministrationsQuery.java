package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import javax.xml.bind.annotation.*;

/**
 * Represents a stored query for FindMedicationAdministrationsQuery (PHARM-1).
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindMedicationAdministrationsQuery", propOrder = {})
@XmlRootElement(name = "findMedicationAdministrationsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindMedicationAdministrationsQuery extends StoredQuery implements PatientIdBasedStoredQuery {
    private static final long serialVersionUID = -5233621576759064938L;

    @Getter @Setter private Identifiable patientId;

    /**
     * Constructs the query.
     */
    public FindMedicationAdministrationsQuery() {
        super(QueryType.FIND_MEDICATION_ADMINISTRATIONS);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
