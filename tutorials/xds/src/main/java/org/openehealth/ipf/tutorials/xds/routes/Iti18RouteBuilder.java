/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.tutorials.xds.routes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindFoldersQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindSubmissionSetsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetByIdAndCodesQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetByIdQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetByUuidQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetFromDocumentQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetRelatedDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.StoredQuery;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;
import org.openehealth.ipf.tutorials.xds.authz.AuthorizationDecisionPoint;
import org.openehealth.ipf.tutorials.xds.authz.AuthorizedPager;
import org.openehealth.ipf.tutorials.xds.authz.DemoDecisionPoint;
import org.openehealth.ipf.tutorials.xds.model.QueryContext;
import org.openehealth.ipf.tutorials.xds.store.Search;
import org.openehealth.ipf.tutorials.xds.store.SearchResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_DOCUMENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_DOCUMENTS_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_FOLDERS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_SUBMISSION_SETS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_ASSOCIATIONS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_DOCUMENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_DOCUMENTS_AND_ASSOCIATIONS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_FOLDERS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_FOLDERS_FOR_DOCUMENT;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_FOLDER_AND_CONTENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_RELATED_DOCUMENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_SUBMISSION_SETS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_SUBMISSION_SET_AND_CONTENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrderComparators.documentEntryComparator;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrderComparators.folderComparator;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrderComparators.submissionSetComparator;
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti18RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti18ResponseValidator;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.fail;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.logExchange;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.toObjectRefs;
import static org.openehealth.ipf.tutorials.xds.store.SearchResult.ASSOC;
import static org.openehealth.ipf.tutorials.xds.store.SearchResult.ASSOC_SOURCE;
import static org.openehealth.ipf.tutorials.xds.store.SearchResult.ASSOC_TARGET;
import static org.openehealth.ipf.tutorials.xds.store.SearchResult.DOC_ENTRY;
import static org.openehealth.ipf.tutorials.xds.store.SearchResult.FOLDER;
import static org.openehealth.ipf.tutorials.xds.store.SearchResult.SUB_SET;

/**
 * Route builder for ITI-18.
 * <p>
 * The message body is a {@link QueryContext} from the moment the request is validated. Each stored query
 * has a route of its own, which collects its results into the response by a sequence of searches.
 *
 * @author Jens Riemschneider
 */
@Slf4j
public class Iti18RouteBuilder extends RouteBuilder {

    // ----- The response lists the searches collect into

    private static final Function<QueryContext, List<DocumentEntry>> DOCS = context -> context.getResp().getDocumentEntries();
    private static final Function<QueryContext, List<Folder>> FOLDERS = context -> context.getResp().getFolders();
    private static final Function<QueryContext, List<SubmissionSet>> SETS = context -> context.getResp().getSubmissionSets();
    private static final Function<QueryContext, List<Association>> ASSOCS = context -> context.getResp().getAssociations();

    // ----- Query parameters, by the query type that declares them. Reading one from a query that does not
    //       declare it fails with a ClassCastException, i.e. a query routed to the wrong search.

    private static final Function<QueryContext, String> UNIQUE_ID =
            context -> context.query(GetFromDocumentQuery.class).getUniqueId();
    private static final Function<QueryContext, String> UUID =
            context -> context.query(GetFromDocumentQuery.class).getUuid();
    private static final Function<QueryContext, List<String>> UNIQUE_IDS =
            context -> context.query(GetByIdQuery.class).getUniqueIds();
    private static final Function<QueryContext, List<String>> UUIDS =
            context -> context.query(GetByUuidQuery.class).getUuids();
    private static final Function<QueryContext, List<AssociationType>> ASSOC_TYPES =
            context -> context.query(GetRelatedDocumentsQuery.class).getAssociationTypes();

    /**
     * Fills a window of the result set the requestor is allowed to see, deciding as few document entries
     * as it can. Defaulted so that the route works unconfigured; a deployment replaces the decision point
     * behind it with one that evaluates real policy.
     */
    @Getter @Setter
    private AuthorizedPager pager = defaultPager();

    /**
     * Match set size at or below which {@code totalResultCount} is still reported.
     * <p>
     * The only honest total over an enforced result set is the size of the <em>authorized</em> set.
     * Producing it means deciding the entire match set to answer one window -- the cost the lazy fill
     * exists to avoid, and work thrown away if the consumer never asks for a second page. Reporting the
     * size of the <em>match</em> set instead would be worse than saying nothing: it is not the number the
     * consumer will page to, and the difference between it and what arrives says exactly how many
     * documents are being withheld. So the total is reported in the one case where it is honest anyway --
     * a match set small enough that deciding all of it is cheap regardless -- and omitted above it. An
     * absent total is a defined answer; the consumer detects the end of the set by the short page.
     */
    @Getter @Setter
    private int totalCountThreshold = 50;

    private static <E, R> Search<QueryContext, E, R> search(SearchResult<E, R> result) {
        return Search.search(QueryContext.class, result);
    }

    @Override
    public void configure() {
        errorHandler(noErrorHandler());

        // Entry point for Stored Query
        from("xds-iti18:xds-iti18")
            .process(logExchange(log, exchange -> "received iti18: " + exchange.getIn().getBody(QueryRegistry.class)))
            .process(iti18RequestValidator())
            .transform().body(QueryRegistry.class, request -> new QueryContext(request, new QueryResponse(SUCCESS)))
            // Dispatch to the correct query implementation
            .choice()
                .when(queryType(FIND_DOCUMENTS, FIND_DOCUMENTS_EXCLUDE)).to("direct:findDocs")
                .when(queryType(FIND_SUBMISSION_SETS)).to("direct:findSets")
                .when(queryType(FIND_FOLDERS)).to("direct:findFolders")
                .when(queryType(GET_SUBMISSION_SET_AND_CONTENTS)).to("direct:getSetAndContents")
                .when(queryType(GET_DOCUMENTS)).to("direct:getDocs")
                .when(queryType(GET_FOLDER_AND_CONTENTS)).to("direct:getFolderAndContents")
                .when(queryType(GET_FOLDERS)).to("direct:getFolders")
                .when(queryType(GET_SUBMISSION_SETS)).to("direct:getSets")
                .when(queryType(GET_ASSOCIATIONS)).to("direct:getAssocs")
                .when(queryType(GET_DOCUMENTS_AND_ASSOCIATIONS)).to("direct:getDocsAndAssocs")
                .when(queryType(GET_FOLDERS_FOR_DOCUMENT)).to("direct:getFoldersForDoc")
                .when(queryType(GET_RELATED_DOCUMENTS)).to("direct:getRelatedDocs")
                .otherwise().process(fail(ValidationMessage.UNKNOWN_QUERY_TYPE))
            .end()
            // Apply the ordering, the policy and the window the consumer asked for -- before anything is
            // thrown away, and before a projection to object references takes the metadata the policy
            // reads out of the result
            .process(this::sortAuthorizeAndPage)
            // Convert to object references if requested
            .choice()
                .when().body(QueryContext.class, context -> context.getReq().getReturnType() == QueryReturnType.OBJECT_REF)
                    .to("direct:convertToObjRefs")
            .end()
            .transform().body(QueryContext.class, QueryContext::getResp)
            .process(iti18ResponseValidator())     // This includes the check for RESULT_NOT_SINGLE_PATIENT
            .process(logExchange(log, exchange -> "response iti18: " + exchange.getIn().getBody()));

        // Converts all results into ObjectReferences
        from("direct:convertToObjRefs")
            .process(toObjectRefs(SETS))
            .process(toObjectRefs(DOCS))
            .process(toObjectRefs(FOLDERS))
            .process(toObjectRefs(ASSOCS));

        // FindDocumentsQuery logic -- this also serves FindDocumentsExclude, which searches the same
        // document entries and only adds parameters naming what shall not be returned, so the whole
        // difference is in the matching and no separate search is needed. Note that the option is
        // defined for XDS.b only (ITI TF-1: 10.2.12), and the request validator refuses the query for
        // ITI-38.
        from("direct:findDocs")
            .process(search(DOC_ENTRY)
                    .byQuery(context -> context.getReq().getQuery())
                    .patientId(context -> context.query(FindDocumentsQuery.class).getPatientId())
                    .into(DOCS));

        // FindSubmissionSetsQuery logic
        from("direct:findSets")
            .process(search(SUB_SET)
                    .byQuery(context -> context.getReq().getQuery())
                    .patientId(context -> context.query(FindSubmissionSetsQuery.class).getPatientId())
                    .into(SETS));

        // FindFoldersQuery logic
        from("direct:findFolders")
            .process(search(FOLDER)
                    .byQuery(context -> context.getReq().getQuery())
                    .patientId(context -> context.query(FindFoldersQuery.class).getPatientId())
                    .into(FOLDERS));

        // GetDocumentsQuery logic
        from("direct:getDocs")
            .process(search(DOC_ENTRY).uniqueIds(UNIQUE_IDS).uuids(UUIDS).into(DOCS));

        // GetFoldersQuery logic
        from("direct:getFolders")
            .process(search(FOLDER).uniqueIds(UNIQUE_IDS).uuids(UUIDS).into(FOLDERS));

        // GetDocumentsAndAssociationsQuery logic
        from("direct:getDocsAndAssocs")
            .to("direct:getDocs")
            .process(search(ASSOC).targets(DOCS).into(ASSOCS))
            .process(search(ASSOC).sources(DOCS).into(ASSOCS));

        // GetFoldersForDocumentQuery logic
        from("direct:getFoldersForDoc")
            .process(search(DOC_ENTRY).uniqueId(UNIQUE_ID).uuid(UUID).into(QueryContext::getDoc))
            .process(search(ASSOC_SOURCE).hasMember().targets(QueryContext::getDoc).into(QueryContext::getSources))
            .process(search(FOLDER).uuids(QueryContext::getSources).into(FOLDERS));

        // GetRelatedDocumentsQuery logic
        from("direct:getRelatedDocs")
            .process(search(DOC_ENTRY).uniqueId(UNIQUE_ID).uuid(UUID).into(QueryContext::getDoc))
            .process(search(ASSOC_SOURCE).types(ASSOC_TYPES).targets(QueryContext::getDoc).into(QueryContext::getSources))
            .process(search(DOC_ENTRY).uuids(QueryContext::getSources).into(QueryContext::getSourceDocs))
            .process(search(ASSOC_TARGET).types(ASSOC_TYPES).sources(QueryContext::getDoc).into(QueryContext::getTargets))
            .process(search(DOC_ENTRY).uuids(QueryContext::getTargets).into(QueryContext::getTargetDocs))
            .process(search(ASSOC).types(ASSOC_TYPES).sources(QueryContext::getSourceDocs).targets(QueryContext::getDoc).into(ASSOCS))
            .process(search(ASSOC).types(ASSOC_TYPES).sources(QueryContext::getDoc).targets(QueryContext::getTargetDocs).into(ASSOCS))
            .process(search(DOC_ENTRY).referenced(ASSOCS).into(DOCS));

        // GetSubmissionSetQuery logic
        from("direct:getSets")
            .process(search(ASSOC_SOURCE).hasMember().targetUuids(UUIDS).into(QueryContext::getSources))
            .process(search(SUB_SET).uuids(QueryContext::getSources).into(SETS))
            .process(search(ASSOC).hasMember().sources(SETS).targetUuids(UUIDS).into(ASSOCS));

        // GetAssociationsQuery logic
        from("direct:getAssocs")
            .process(search(ASSOC).sourceUuids(UUIDS).into(ASSOCS))
            .process(search(ASSOC).targetUuids(UUIDS).into(ASSOCS));

        // GetFolderAndContentsQuery logic
        from("direct:getFolderAndContents")
            .process(search(FOLDER).uniqueId(UNIQUE_ID).uuid(UUID).into(FOLDERS))
            .process(search(ASSOC_TARGET).hasMember().sources(FOLDERS).into(QueryContext::getMembers))
            .process(searchMemberDocuments())
            .process(search(ASSOC).hasMember().sources(FOLDERS).targets(DOCS).into(ASSOCS));

        // GetSubmissionSetAndContentsQuery logic
        from("direct:getSetAndContents")
            .process(search(SUB_SET).uniqueId(UNIQUE_ID).uuid(UUID).into(SETS))
            .process(search(ASSOC_TARGET).hasMember().sources(SETS).into(QueryContext::getMembers))
            .process(searchMemberDocuments())
            .process(search(FOLDER).uuids(QueryContext::getMembers).into(FOLDERS))
            .process(search(ASSOC).hasMember().sources(SETS).targets(DOCS).into(ASSOCS))
            .process(search(ASSOC).hasMember().sources(SETS).targets(FOLDERS).into(ASSOCS))
            .process(search(ASSOC).hasMember().sources(FOLDERS).targets(DOCS).into(ASSOCS))
            .process(search(ASSOC).hasMember().sources(SETS).targets(ASSOCS).into(ASSOCS));
    }

    /**
     * The documents among the members of the folder or submission set a Get...AndContents query asks
     * for, restricted by the codes and document entry types the query names.
     */
    private static Search<QueryContext, DocumentEntry, DocumentEntry> searchMemberDocuments() {
        return search(DOC_ENTRY)
                .confCodes(context -> context.query(GetByIdAndCodesQuery.class).getConfidentialityCodes())
                .formatCodes(context -> context.query(GetByIdAndCodesQuery.class).getFormatCodes())
                .entryTypes(context -> context.query(GetByIdAndCodesQuery.class).getDocumentEntryTypes())
                .uuids(QueryContext::getMembers)
                .into(DOCS);
    }

    private static Predicate queryType(QueryType... types) {
        var accepted = Set.of(types);
        return exchange -> accepted.contains(
                exchange.getIn().getBody(QueryContext.class).getReq().getQuery().getType());
    }

    /**
     * Serves the ordering and paging extension of ITI-18: the requested order arrives in the
     * {@code $XDSSortOrder} slot of the stored query, the requested window in the ebRS pagination
     * attributes of the AdhocQueryRequest.
     * <p>
     * Neither is defined by ITI-18, so a registry is free to ignore them -- which is precisely why this
     * reports back what it did. Without {@code honoredSortOrder} a consumer cannot tell an applied order
     * from an ignored one, and would have to assume the worst and sort again itself.
     * <p>
     * Document entries, folders and submission sets are each ordered by their own comparator, because
     * the attribute names differ by object type -- the same concept is
     * {@code $XDSDocumentEntryUniqueId} for a document entry and {@code $XDSFolderUniqueId} for a
     * folder. Associations carry no orderable metadata and are left alone.
     * <p>
     * Windowing, on the other hand, only happens for queries that return a single kind of object, which
     * {@link QueryType#isPageable()} decides. A query such as GetSubmissionSetAndContents returns sets,
     * folders, documents and the associations tying them together, and there is no sensible reading of
     * "the second page" of that: cutting the lists would answer with associations whose endpoints are no
     * longer in the response. The request validator rejects such a request before it gets here; this
     * guard keeps the rule true for a route that does not validate.
     * <p>
     * Results are additionally enforced against the {@link AuthorizationDecisionPoint}, on every path
     * rather than only on the paged one. Whether a consumer asked for a window must not decide whether
     * policy applies. The lazy fill of {@link AuthorizedPager} is an economy a bounded window makes
     * available, not the enforcement itself. A {@code Get...} query -- the second phase of a
     * two-phase query, where the requestor names the UUIDs it already holds -- has to pass the same
     * policy, or the second phase becomes a way around the first.
     * <p>
     * Documents, folders and submission sets are each enforced, because each of them is what a stored
     * query may search for and each carries metadata a policy can read. Associations are the exception
     * this demonstration leaves open: they are returned unfiltered even when they point at a denied
     * object, which hands the consumer the identifier of something it may not see.
     */
    void sortAuthorizeAndPage(Exchange exchange) {
        var context = exchange.getIn().getBody(QueryContext.class);
        var request = context.getReq();
        var response = context.getResp();
        var sortOrder = request.getQuery() instanceof StoredQuery storedQuery ? storedQuery.getSortOrder() : null;

        // Order first -- a window into an undefined order can repeat and skip entries. An order naming
        // document entry attributes yields no folder comparator and vice versa, so in practice only the
        // kind the consumer asked about is reordered. Sorting an empty list does not count as honoring
        // the order: a folder query ordered by a document attribute would otherwise sort no documents,
        // report success, and leave the consumer believing its folders came back ordered.
        var ordered = sortInPlace(response.getDocumentEntries(), documentEntryComparator(sortOrder));
        ordered |= sortInPlace(response.getFolders(), folderComparator(sortOrder));
        ordered |= sortInPlace(response.getSubmissionSets(), submissionSetComparator(sortOrder));
        if (ordered) {
            response.setHonoredSortOrder(sortOrder);
        }

        var requestor = requestorOf(exchange);

        if ((request.getStartIndex() == null && request.getMaxResults() == null)
                || !request.getQuery().getType().isPageable()) {
            // No window to fill, so there is no prefix to find and nothing to stop early for: every
            // match gets decided. This is also the path every Get... query takes.
            RESULT_LISTS.forEach(list -> authorizeAll(requestor, list, response));
            return;
        }

        // A pageable query searches for one kind of object -- FindDocuments for documents, FindFolders
        // for folders, FindSubmissionSets for submission sets -- so the window applies to whichever list
        // came back with something in it. Which one it was no longer matters once the result is empty:
        // the rules below read the same for all three.
        var windowed = RESULT_LISTS.stream()
                .filter(list -> !list.get(response).isEmpty())
                .findFirst()
                .orElse(RESULT_LISTS.get(0));
        RESULT_LISTS.stream()
                .filter(list -> list != windowed)
                .forEach(list -> authorizeAll(requestor, list, response));
        fillWindow(requestor, windowed, request, response);
    }

    private <T extends XDSMetaClass> void fillWindow(String requestor, ResultList<T> windowed,
                                                     QueryRegistry request, QueryResponse response) {
        var matched = windowed.get(response);

        // A start index at or below zero means "from the beginning", and a negative maxResults means
        // unbounded -- ebRS defaults both attributes and cannot tell absent from zero on arrival.
        var from = Math.max(request.getStartIndex() != null ? request.getStartIndex() : 0, 0);
        var limit = (request.getMaxResults() == null || request.getMaxResults() < 0) ? null : request.getMaxResults();

        if (matched.size() <= totalCountThreshold) {
            // Small enough that deciding all of it is cheap regardless, which is the one case in which a
            // total can be reported honestly: it is the size of the authorized set, not of the match set.
            var authorized = authorizeAll(requestor, matched);

            // A window starting past the end is refused rather than answered with an empty page, which
            // the consumer could not tell apart from a query that simply matched nothing. Offset zero is
            // exempt: a search with no matches -- or whose every match is denied -- is a legitimate empty
            // answer, not a window out of range.
            if (from > 0 && from >= authorized.size()) {
                throw new XDSMetaDataException(ValidationMessage.START_INDEX_BEYOND_END, from);
            }

            // in long, because from + limit overflows for a maxResults near Integer.MAX_VALUE
            var to = (limit != null) ? (int) Math.min((long) from + limit, authorized.size()) : authorized.size();
            windowed.set(response, new ArrayList<>(authorized.subList(from, to)));
            response.setTotalResultCount(authorized.size());
            response.setStartIndex(from);
            return;
        }

        // Above the threshold the total is omitted, and with it the only thing that could have answered
        // a count-only request: maxResults=0 asks for a number this registry has declined to produce.
        if (limit != null && limit == 0) {
            windowed.set(response, new ArrayList<>());
            response.setStartIndex(from);
            return;
        }

        var page = pager.fill(requestor, matched.iterator(), from, limit);

        // The one case that costs a full scan and a full pass of enforcement, and there is nothing
        // cheaper: the registry cannot know the size of the authorized set without producing it. It is
        // pathological by construction -- a consumer paging in sequence reaches a short page first.
        if (from > 0 && page.getEntries().isEmpty()) {
            throw new XDSMetaDataException(ValidationMessage.START_INDEX_BEYOND_END, from);
        }

        log.debug("decided {} of {} matched {} to fill a window of {} at {}",
                page.getDecided(), matched.size(), windowed.name(), page.getEntries().size(), from);

        windowed.set(response, page.getEntries());
        response.setStartIndex(from);
    }

    /**
     * A response list a stored query can return a window of.
     */
    private record ResultList<T extends XDSMetaClass>(
            String name,
            Function<QueryResponse, List<T>> getter,
            BiConsumer<QueryResponse, List<T>> setter) {

        List<T> get(QueryResponse response) {
            return getter.apply(response);
        }

        void set(QueryResponse response, List<T> entries) {
            setter.accept(response, entries);
        }
    }

    /**
     * The response lists a stored query can return a window of, in the order they are looked at.
     * Associations are not among them: they tie other objects together, so a window over them would
     * answer with associations whose endpoints are no longer in the response.
     */
    private static final List<ResultList<?>> RESULT_LISTS = List.of(
            new ResultList<>("documentEntries", QueryResponse::getDocumentEntries, QueryResponse::setDocumentEntries),
            new ResultList<>("folders", QueryResponse::getFolders, QueryResponse::setFolders),
            new ResultList<>("submissionSets", QueryResponse::getSubmissionSets, QueryResponse::setSubmissionSets));

    private <T extends XDSMetaClass> void authorizeAll(String requestor, ResultList<T> list, QueryResponse response) {
        list.set(response, authorizeAll(requestor, list.get(response)));
    }

    /**
     * Enforces the policy over a whole list, in blocks. Used where no window bounds the work: a query
     * that asked for no page, and every {@code Get...} query.
     */
    private <T extends XDSMetaClass> List<T> authorizeAll(String requestor, List<T> entries) {
        return entries.isEmpty() ? entries : pager.fill(requestor, entries.iterator(), 0, null).getEntries();
    }

    /**
     * Whoever the query arrived on behalf of. This tutorial has no security context, so everyone is the
     * same anonymous requestor unless a header says otherwise; a registry reads the subject of the SAML
     * assertion or JWT the transaction carries, and its entitlements are what the decision point decides
     * against.
     */
    static String requestorOf(Exchange exchange) {
        return exchange.getIn().getHeader("requestor", "anonymous", String.class);
    }

    /**
     * @param results    the results to order, in place
     * @param comparator the order to apply, or null if the requested one names none of this kind's
     *                   attributes
     * @return whether results were actually ordered, which is what the registry reports back as honored
     */
    private static <T> boolean sortInPlace(List<T> results, Comparator<? super T> comparator) {
        if (comparator == null || results.isEmpty()) {
            return false;
        }
        results.sort(comparator);
        return true;
    }

    private static AuthorizedPager defaultPager() {
        var pager = new AuthorizedPager();
        pager.setDecisionPoint(new DemoDecisionPoint());
        return pager;
    }
}
