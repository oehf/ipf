package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import javax.xml.bind.annotation.*;

/**
 * Represents a stored query for FindPrescriptionsForDispenseQuery (PHARM-1).
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindPrescriptionsForDispenseQuery", propOrder = {})
@XmlRootElement(name = "findPrescriptionsForDispenseQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindPrescriptionsForDispenseQuery extends StoredQuery implements PatientIdBasedStoredQuery {
    private static final long serialVersionUID = -2056362879334066497L;

    @Getter @Setter private Identifiable patientId;

    /**
     * Constructs the query.
     */
    public FindPrescriptionsForDispenseQuery() {
        super(QueryType.FIND_PRESCRIPTIONS_FOR_DISPENSE);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
