package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import javax.xml.bind.annotation.*;

/**
 * Represents a stored query for FindDispensesQuery (PHARM-1).
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindDispensesQuery", propOrder = {})
@XmlRootElement(name = "findDispensesQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindDispensesQuery extends StoredQuery implements PatientIdBasedStoredQuery {
    private static final long serialVersionUID = -8924928000145710050L;

    @Getter @Setter private Identifiable patientId;

    /**
     * Constructs the query.
     */
    public FindDispensesQuery() {
        super(QueryType.FIND_DISPENSES);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
