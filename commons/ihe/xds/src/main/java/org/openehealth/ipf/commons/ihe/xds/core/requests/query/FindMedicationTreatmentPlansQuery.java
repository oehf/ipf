package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.*;

/**
 * Represents a stored query for FindMedicationTreatmentPlansQuery (PHARM-1).
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindMedicationTreatmentPlansQuery", propOrder = {})
@XmlRootElement(name = "findMedicationTreatmentPlansQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindMedicationTreatmentPlansQuery extends Pharm1StableDocumentsQuery {
    private static final long serialVersionUID = 5983946116420097344L;

    /**
     * Constructs the query.
     */
    public FindMedicationTreatmentPlansQuery() {
        super(QueryType.FIND_MEDICATION_TREATMENT_PLANS);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
