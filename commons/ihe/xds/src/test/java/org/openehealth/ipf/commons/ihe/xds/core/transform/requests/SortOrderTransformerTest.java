/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortKey;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrder;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.StoredQuery;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.AdhocQueryRequestValidator;

import java.util.List;

import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_18;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The sort order is an extension IHE does not define, so what matters is that it travels as an ordinary
 * extra slot -- one a registry that does not implement the extension simply ignores -- and comes back as
 * the same order on the far side.
 *
 * @author Christian Ohr
 */
public class SortOrderTransformerTest {

    /**
     * Sort keys name metadata attributes, which is not the same vocabulary as the query parameters:
     * creation time is filtered by CreationTimeFrom/To but sorted by the attribute itself, so there is
     * no QueryParameter to take this name from.
     */
    private static final String CREATION_TIME = "$XDSDocumentEntryCreationTime";
    private static final String AUTHOR_PERSON = QueryParameter.DOC_ENTRY_AUTHOR_PERSON.getSlotName();
    private static final String ENTRY_UUID = QueryParameter.DOC_ENTRY_UUID.getSlotName();

    private QueryRegistryTransformer transformer;

    @BeforeEach
    public void setUp() {
        transformer = new QueryRegistryTransformer();
    }

    @AfterEach
    public void tearDown() {
        SortOrder.setSlotName(SortOrder.DEFAULT_SLOT_NAME);
    }

    @Test
    public void testTheOrderSurvivesTheRoundTrip() {
        var sortOrder = new SortOrder(
            SortKey.descending(CREATION_TIME),
            SortKey.ascending(AUTHOR_PERSON));

        var result = roundTrip(sortOrder);

        assertEquals(sortOrder, result.getSortOrder());
        assertEquals(List.of("-" + CREATION_TIME, AUTHOR_PERSON), result.getSortOrder().encode());
    }

    /**
     * The order goes into the agreed slot, encoded the way every other coded list slot is, so a registry
     * reading it with ordinary slot tooling gets a list of names rather than something IPF-specific.
     */
    @Test
    public void testTheOrderIsWrittenToTheAgreedSlot() {
        var ebXML = transformer.toEbXML(queryWith(new SortOrder(SortKey.descending(CREATION_TIME))));

        assertEquals(List.of("('-" + CREATION_TIME + "')"),
            ebXML.getSlotValues(SortOrder.DEFAULT_SLOT_NAME));
    }

    /**
     * A query without an order must look exactly as it did before this extension existed.
     */
    @Test
    public void testNoOrderMeansNoSlot() {
        var ebXML = transformer.toEbXML(queryWith(null));

        assertTrue(ebXML.getSlotValues(SortOrder.DEFAULT_SLOT_NAME).isEmpty());
        assertNull(roundTrip(null).getSortOrder());
    }

    @Test
    public void testAnEmptyOrderIsNotWrittenEither() {
        var ebXML = transformer.toEbXML(queryWith(new SortOrder()));

        assertTrue(ebXML.getSlotValues(SortOrder.DEFAULT_SLOT_NAME).isEmpty());
    }

    /**
     * The sort order is an extra slot, but a known one. Without excluding it from the extra parameter
     * sweep it would arrive both typed and untyped, and a re-serialization would then write it twice.
     */
    @Test
    public void testTheOrderDoesNotAlsoLandInExtraParameters() {
        var result = roundTrip(new SortOrder(SortKey.descending(CREATION_TIME)));

        assertTrue(result.getExtraParameters().isEmpty(),
            "the sort order slot was swept into the extra parameters: " + result.getExtraParameters());
    }

    /**
     * Other extension slots must keep working next to it.
     */
    @Test
    public void testOtherExtraParametersAreUntouched() {
        var query = new FindDocumentsQuery();
        query.setSortOrder(new SortOrder(SortKey.ascending(CREATION_TIME)));
        query.getExtraParameters().put("$somethingElse", new QueryList<>("42"));

        var result = (FindDocumentsQuery) transformer.fromEbXML(
            transformer.toEbXML(new QueryRegistry(query))).getQuery();

        assertEquals(query.getSortOrder(), result.getSortOrder());
        assertEquals(List.of(List.of("42")), result.getExtraParameters().get("$somethingElse").getOuterList());
    }

    /**
     * Paging needs a total order, so the tiebreaker is appended -- but only once, and never twice if the
     * caller already asked to sort by the entry UUID.
     */
    @Test
    public void testTheStableTiebreakerIsAppendedOnlyWhenMissing() {
        var withTiebreaker = new SortOrder(SortKey.descending(CREATION_TIME)).withStableTiebreaker();
        assertEquals(List.of("-" + CREATION_TIME, ENTRY_UUID), withTiebreaker.encode());

        var alreadyUnique = new SortOrder(SortKey.ascending(ENTRY_UUID));
        assertEquals(alreadyUnique, alreadyUnique.withStableTiebreaker());
    }

    /**
     * Which slot carries the order is bilaterally agreed, so a deployment can rename it -- and then both
     * directions have to use the new name.
     */
    @Test
    public void testTheSlotNameIsConfigurable() {
        SortOrder.setSlotName("$icwSortOrder");

        var sortOrder = new SortOrder(SortKey.descending(CREATION_TIME));
        var ebXML = transformer.toEbXML(queryWith(sortOrder));

        assertTrue(ebXML.getSlotValues(SortOrder.DEFAULT_SLOT_NAME).isEmpty());
        assertEquals(List.of("('-" + CREATION_TIME + "')"), ebXML.getSlotValues("$icwSortOrder"));
        assertEquals(sortOrder, ((StoredQuery) transformer.fromEbXML(ebXML).getQuery()).getSortOrder());
    }

    @Test
    public void testASlotNameHasToLookLikeOne() {
        assertThrows(IllegalArgumentException.class, () -> SortOrder.setSlotName("icwSortOrder"));
        assertThrows(IllegalArgumentException.class, () -> SortOrder.setSlotName(null));
    }

    @Test
    public void testAKeyNeedsASlotName() {
        assertThrows(IllegalArgumentException.class, () -> SortKey.ascending("  "));
    }

    /**
     * ITI-18 requires a registry to ignore parameters it does not understand, and IPF's own request
     * validator has to do the same -- otherwise a sorted query would be rejected before it ever reaches
     * the wire.
     */
    @Test
    public void testASortedQueryStillValidates() {
        var request = SampleData.createFindDocumentsQuery();
        ((StoredQuery) request.getQuery()).setSortOrder(
            new SortOrder(SortKey.descending(CREATION_TIME)).withStableTiebreaker());

        AdhocQueryRequestValidator.getInstance()
            .validate(transformer.toEbXML(request), ITI_18);
    }

    /**
     * A malformed sort order arrives from a client, so it is bad metadata rather than a programming
     * error: it has to be reported the way every other malformed slot is, as an XDSMetaDataException
     * that the transaction turns into a RegistryError -- not as an unhandled IllegalArgumentException.
     */
    @Test
    public void testAMalformedSortOrderIsReportedAsBadMetadata() {
        var ebXML = transformer.toEbXML(queryWith(null));
        ebXML.addSlot(SortOrder.getSlotName(), "('')");

        var exception = assertThrows(XDSMetaDataException.class, () -> transformer.fromEbXML(ebXML));
        assertEquals(ValidationMessage.INVALID_SORT_ORDER, exception.getValidationMessage());
    }

    /**
     * A key that is nothing but the descending marker names no attribute either.
     */
    @Test
    public void testADanglingDescendingMarkerIsRejected() {
        assertThrows(XDSMetaDataException.class, () -> SortKey.decode("-"));
    }

    // ------------------------------------------------------------------------------------- helpers

    private QueryRegistry queryWith(SortOrder sortOrder) {
        var query = new FindDocumentsQuery();
        query.setSortOrder(sortOrder);
        return new QueryRegistry(query);
    }

    private StoredQuery roundTrip(SortOrder sortOrder) {
        return (StoredQuery) transformer.fromEbXML(transformer.toEbXML(queryWith(sortOrder))).getQuery();
    }

}
