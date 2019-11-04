package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.*;

/**
 * Represents a stored query for FindPrescriptionsForValidationQuery (PHARM-1).
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindPrescriptionsForValidationQuery", propOrder = {})
@XmlRootElement(name = "findPrescriptionsForValidationQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindPrescriptionsForValidationQuery extends Pharm1StableDocumentsQuery {
    private static final long serialVersionUID = 1029367082044223870L;

    /**
     * Constructs the query.
     */
    public FindPrescriptionsForValidationQuery() {
        super(QueryType.FIND_PRESCRIPTIONS_FOR_VALIDATION);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
