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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetSubmissionSetAndContentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortKey;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrder;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.AdhocQueryRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_18;

/**
 * The window travels in the ebRS 3.0 pagination attributes and the total comes back in
 * {@code totalResultCount}. ITI-18 §3.18.4.1.2.6 discourages combining stored queries with ebRS
 * pagination, so what matters most here is that a request which asks for no window looks exactly as it
 * did before -- a registry must not see the extension unless it was asked for.
 *
 * @author Christian Ohr
 */
public class PagingTransformerTest {

    private static final String CREATION_TIME = "$XDSDocumentEntryCreationTime";

    private QueryRegistryTransformer requestTransformer;
    private QueryResponseTransformer responseTransformer;

    @BeforeEach
    public void setUp() {
        requestTransformer = new QueryRegistryTransformer();
        responseTransformer = new QueryResponseTransformer(new EbXMLFactory30());
    }

    // ------------------------------------------------------------------------------- request side

    @Test
    public void testTheWindowSurvivesTheRoundTrip() {
        var request = new QueryRegistry(new FindDocumentsQuery());
        request.setStartIndex(100);
        request.setMaxResults(50);
        request.setRequestId("urn:uuid:1a2b3c");

        var ebXML = requestTransformer.toEbXML(request);
        assertEquals(BigInteger.valueOf(100), ebXML.getInternal().getStartIndex());
        assertEquals(BigInteger.valueOf(50), ebXML.getInternal().getMaxResults());
        assertEquals("urn:uuid:1a2b3c", ebXML.getInternal().getId());

        var result = requestTransformer.fromEbXML(ebXML);
        assertEquals(100, result.getStartIndex());
        assertEquals(50, result.getMaxResults());
        assertEquals("urn:uuid:1a2b3c", result.getRequestId());
    }

    /**
     * A request that asks for no window must produce the same XML as before the extension existed: no
     * attributes written, so nothing for a registry to trip over.
     */
    @Test
    public void testNoWindowWritesNoAttributes() {
        var ebXML = requestTransformer.toEbXML(new QueryRegistry(new FindDocumentsQuery()));

        assertNull(ebXML.getInternal().getId());
        assertNull(getRawStartIndex(ebXML.getInternal().getStartIndex()));
        assertNull(getRawMaxResults(ebXML.getInternal().getMaxResults()));

        var result = requestTransformer.fromEbXML(ebXML);
        assertNull(result.getStartIndex());
        assertNull(result.getMaxResults());
        assertNull(result.getRequestId());
    }

    /**
     * ebRS defaults startIndex to 0 and maxResults to -1, and the stub getters report those defaults
     * rather than null, so both have to be read as "no window was asked for".
     */
    @Test
    public void testTheEbrsDefaultsMeanNoWindow() {
        var ebXML = requestTransformer.toEbXML(new QueryRegistry(new FindDocumentsQuery()));
        ebXML.getInternal().setStartIndex(BigInteger.ZERO);
        ebXML.getInternal().setMaxResults(BigInteger.valueOf(-1));

        assertNull(ebXML.getStartIndex());
        assertNull(ebXML.getMaxResults());
    }

    @Test
    public void testAskingForNothingIsNotTheSameAsAskingForEverything() {
        var request = new QueryRegistry(new FindDocumentsQuery());
        request.setMaxResults(0);

        var ebXML = requestTransformer.toEbXML(request);

        assertEquals(0, ebXML.getMaxResults(), "maxResults=0 asks for the count alone, not for everything");
        assertEquals(0, requestTransformer.fromEbXML(ebXML).getMaxResults());
    }

    /**
     * A window is only meaningful over a result set that is a list. GetSubmissionSetAndContents returns
     * sets, folders, documents and the associations tying them together, so cutting it would answer with
     * associations whose endpoints are missing -- the request is refused rather than answered wrongly.
     */
    @Test
    public void testAWindowOnAGraphQueryIsRejected() {
        var query = new GetSubmissionSetAndContentsQuery();
        query.setUniqueId("1.2.3.4");
        var request = new QueryRegistry(query);
        request.setMaxResults(50);

        var exception = assertThrows(XDSMetaDataException.class,
            () -> AdhocQueryRequestValidator.getInstance().validate(requestTransformer.toEbXML(request), ITI_18));
        assertEquals(ValidationMessage.QUERY_TYPE_NOT_PAGEABLE, exception.getValidationMessage());
    }

    /**
     * The same query without a window is untouched by the rule: the extension is only refused when it
     * was actually asked for.
     */
    @Test
    public void testAGraphQueryWithoutAWindowIsFine() {
        var query = new GetSubmissionSetAndContentsQuery();
        query.setUniqueId("1.2.3.4");

        AdhocQueryRequestValidator.getInstance()
            .validate(requestTransformer.toEbXML(new QueryRegistry(query)), ITI_18);
    }

    /**
     * Only an open-ended search can return more than the requester bargained for, so only a search is
     * worth windowing.
     */
    @Test
    public void testWhichQueriesCanBePaged() {
        assertTrue(QueryType.FIND_DOCUMENTS.isPageable());
        assertTrue(QueryType.FIND_FOLDERS.isPageable());
        assertTrue(QueryType.FIND_SUBMISSION_SETS.isPageable());
        assertTrue(QueryType.FETCH.isPageable());

        // a Get query returns the objects whose ids the requester passed in, so it already knows how
        // many there are -- a window buys nothing
        assertFalse(QueryType.GET_DOCUMENTS.isPageable());
        assertFalse(QueryType.GET_SUBMISSION_SETS.isPageable());
        assertFalse(QueryType.GET_ASSOCIATIONS.isPageable());
        assertEquals(QueryType.QueryKind.LOOKUP, QueryType.GET_DOCUMENTS.getKind());

        // and for these a window would additionally be corrupting, the result being a graph held
        // together by associations rather than a list
        assertFalse(QueryType.GET_ALL.isPageable());
        assertFalse(QueryType.GET_SUBMISSION_SET_AND_CONTENTS.isPageable());
        assertFalse(QueryType.GET_FOLDER_AND_CONTENTS.isPageable());
        assertFalse(QueryType.GET_DOCUMENTS_AND_ASSOCIATIONS.isPageable());
        assertFalse(QueryType.GET_RELATED_DOCUMENTS.isPageable());

        // a subscription filter is never executed against a registry, so there is nothing to page
        assertFalse(QueryType.SUBSCRIPTION_FOR_FOLDER.isPageable());
        assertEquals(QueryType.QueryKind.SUBSCRIPTION, QueryType.SUBSCRIPTION_FOR_FOLDER.getKind());
    }

    // ------------------------------------------------------------------------------ response side

    @Test
    public void testTheTotalAndTheWindowComeBack() {
        var response = new QueryResponse(Status.SUCCESS);
        response.setStartIndex(100);
        response.setTotalResultCount(4711);
        response.setRequestId("urn:uuid:1a2b3c");

        var result = responseTransformer.fromEbXML(responseTransformer.toEbXML(response));

        assertEquals(100, result.getStartIndex());
        assertEquals(4711, result.getTotalResultCount());
        assertEquals("urn:uuid:1a2b3c", result.getRequestId());
    }

    /**
     * A registry that does not count says nothing, which must not be read as zero -- the difference
     * between "no results" and "I did not count" matters to whoever renders a total.
     */
    @Test
    public void testAnAbsentTotalStaysAbsent() {
        var result = responseTransformer.fromEbXML(
            responseTransformer.toEbXML(new QueryResponse(Status.SUCCESS)));

        assertNull(result.getTotalResultCount());
        assertNull(result.getStartIndex());
        assertNull(result.getRequestId());
    }

    /**
     * The acknowledgement: without it, a registry that ignored the sort order is indistinguishable from
     * one that honoured it.
     */
    @Test
    public void testTheHonoredSortOrderComesBack() {
        var response = new QueryResponse(Status.SUCCESS);
        response.setHonoredSortOrder(
            new SortOrder(SortKey.descending(CREATION_TIME)).withStableTiebreaker());

        var ebXML = responseTransformer.toEbXML(response);
        assertEquals(List.of("('-" + CREATION_TIME + "')", "('$XDSDocumentEntryEntryUUID')"),
            ebXML.getResponseSlotValues(SortOrder.getSlotName()));

        assertEquals(response.getHonoredSortOrder(),
            responseTransformer.fromEbXML(ebXML).getHonoredSortOrder());
    }

    @Test
    public void testAnUnacknowledgedSortOrderIsNull() {
        var result = responseTransformer.fromEbXML(
            responseTransformer.toEbXML(new QueryResponse(Status.SUCCESS)));

        assertNull(result.getHonoredSortOrder(),
            "a registry that said nothing must not look like one that honoured an empty order");
    }

    // ------------------------------------------------------------------------------------ helpers

    private BigInteger getRawStartIndex(BigInteger reported) {
        return BigInteger.ZERO.equals(reported) ? null : reported;
    }

    private BigInteger getRawMaxResults(BigInteger reported) {
        return BigInteger.valueOf(-1).equals(reported) ? null : reported;
    }

}
