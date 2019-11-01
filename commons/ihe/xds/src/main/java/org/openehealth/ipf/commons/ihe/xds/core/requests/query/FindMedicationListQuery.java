package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import javax.xml.bind.annotation.*;

/**
 * Represents a stored query for FindMedicationListQuery (PHARM-1).
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindMedicationListQuery", propOrder = {})
@XmlRootElement(name = "findMedicationListQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindMedicationListQuery extends StoredQuery implements PatientIdBasedStoredQuery {
    private static final long serialVersionUID = 7810851265303915098L;

    @Getter @Setter private Identifiable patientId;

    /**
     * Constructs the query.
     */
    public FindMedicationListQuery() {
        super(QueryType.FIND_MEDICATION_LIST);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
