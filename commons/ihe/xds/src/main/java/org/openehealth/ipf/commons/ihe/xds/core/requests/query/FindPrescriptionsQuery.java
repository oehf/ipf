package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import javax.xml.bind.annotation.*;

/**
 * Represents a stored query for FindPrescriptionsQuery (PHARM-1).
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindPrescriptionsQuery", propOrder = {})
@XmlRootElement(name = "findPrescriptionsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindPrescriptionsQuery extends StoredQuery implements PatientIdBasedStoredQuery {
    private static final long serialVersionUID = -3448220074327935334L;

    @Getter @Setter private Identifiable patientId;

    /**
     * Constructs the query.
     */
    public FindPrescriptionsQuery() {
        super(QueryType.FIND_PRESCRIPTIONS);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
