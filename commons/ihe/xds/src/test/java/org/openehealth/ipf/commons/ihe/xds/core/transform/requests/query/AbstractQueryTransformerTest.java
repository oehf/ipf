package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.StoredQuery;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractQueryTransformerTest<Q extends StoredQuery, T extends AbstractStoredQueryTransformer<Q>> {

    protected Q query;
    protected T transformer;
    protected EbXMLAdhocQueryRequest<AdhocQueryRequest> ebXML;

    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(emptyQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        var result = emptyQuery();
        transformer.fromEbXML(result, ebXML);
        assertEquals(query, result);
    }

    @Test
    public void testFromEbXMLNull() {
        var result = emptyQuery();
        transformer.fromEbXML(result, (EbXMLAdhocQueryRequest<AdhocQueryRequest>)null);
        assertEquals(emptyQuery(), result);
    }

    @Test
    public void testFromEbXMLEmpty() {
        var result = emptyQuery();
        transformer.fromEbXML(result, ebXML);
        assertEquals(emptyQuery(), result);
    }

    protected abstract Q emptyQuery();
}
