/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _BatchResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "batchResponse");
    private final static QName _BatchRequest_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "batchRequest");
    private final static QName _FilterSetAnd_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "and");
    private final static QName _FilterSetNot_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "not");
    private final static QName _FilterSetOr_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "or");
    private final static QName _FilterSetExtensibleMatch_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "extensibleMatch");
    private final static QName _FilterSetGreaterOrEqual_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "greaterOrEqual");
    private final static QName _FilterSetEqualityMatch_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "equalityMatch");
    private final static QName _FilterSetSubstrings_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "substrings");
    private final static QName _FilterSetPresent_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "present");
    private final static QName _FilterSetLessOrEqual_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "lessOrEqual");
    private final static QName _FilterSetApproxMatch_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "approxMatch");
    private final static QName _BatchResponseSearchResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "searchResponse");
    private final static QName _BatchResponseModifyResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "modifyResponse");
    private final static QName _BatchResponseAddResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "addResponse");
    private final static QName _BatchResponseAuthResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "authResponse");
    private final static QName _BatchResponseCompareResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "compareResponse");
    private final static QName _BatchResponseExtendedResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "extendedResponse");
    private final static QName _BatchResponseDelResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "delResponse");
    private final static QName _BatchResponseModDNResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "modDNResponse");
    private final static QName _BatchResponseErrorResponse_QNAME = new QName("urn:oasis:names:tc:DSML:2:0:core", "errorResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ErrorResponse }
     * 
     */
    public ErrorResponse createErrorResponse() {
        return new ErrorResponse();
    }

    /**
     * Create an instance of {@link DsmlModification }
     * 
     */
    public DsmlModification createDsmlModification() {
        return new DsmlModification();
    }

    /**
     * Create an instance of {@link SearchRequest }
     * 
     */
    public SearchRequest createSearchRequest() {
        return new SearchRequest();
    }

    /**
     * Create an instance of {@link BatchRequest }
     * 
     */
    public BatchRequest createBatchRequest() {
        return new BatchRequest();
    }

    /**
     * Create an instance of {@link BatchResponse }
     * 
     */
    public BatchResponse createBatchResponse() {
        return new BatchResponse();
    }

    /**
     * Create an instance of {@link AuthRequest }
     * 
     */
    public AuthRequest createAuthRequest() {
        return new AuthRequest();
    }

    /**
     * Create an instance of {@link FilterSet }
     * 
     */
    public FilterSet createFilterSet() {
        return new FilterSet();
    }

    /**
     * Create an instance of {@link AbandonRequest }
     * 
     */
    public AbandonRequest createAbandonRequest() {
        return new AbandonRequest();
    }

    /**
     * Create an instance of {@link SearchResultReference }
     * 
     */
    public SearchResultReference createSearchResultReference() {
        return new SearchResultReference();
    }

    /**
     * Create an instance of {@link AttributeDescriptions }
     * 
     */
    public AttributeDescriptions createAttributeDescriptions() {
        return new AttributeDescriptions();
    }

    /**
     * Create an instance of {@link ModifyRequest }
     * 
     */
    public ModifyRequest createModifyRequest() {
        return new ModifyRequest();
    }

    /**
     * Create an instance of {@link AddRequest }
     * 
     */
    public AddRequest createAddRequest() {
        return new AddRequest();
    }

    /**
     * Create an instance of {@link ModifyDNRequest }
     * 
     */
    public ModifyDNRequest createModifyDNRequest() {
        return new ModifyDNRequest();
    }

    /**
     * Create an instance of {@link Control }
     * 
     */
    public Control createControl() {
        return new Control();
    }

    /**
     * Create an instance of {@link DsmlAttr }
     * 
     */
    public DsmlAttr createDsmlAttr() {
        return new DsmlAttr();
    }

    /**
     * Create an instance of {@link AttributeValueAssertion }
     * 
     */
    public AttributeValueAssertion createAttributeValueAssertion() {
        return new AttributeValueAssertion();
    }

    /**
     * Create an instance of {@link DsmlMessage }
     * 
     */
    public DsmlMessage createDsmlMessage() {
        return new DsmlMessage();
    }

    /**
     * Create an instance of {@link SubstringFilter }
     * 
     */
    public SubstringFilter createSubstringFilter() {
        return new SubstringFilter();
    }

    /**
     * Create an instance of {@link LDAPResult }
     * 
     */
    public LDAPResult createLDAPResult() {
        return new LDAPResult();
    }

    /**
     * Create an instance of {@link DelRequest }
     * 
     */
    public DelRequest createDelRequest() {
        return new DelRequest();
    }

    /**
     * Create an instance of {@link SearchResponse }
     * 
     */
    public SearchResponse createSearchResponse() {
        return new SearchResponse();
    }

    /**
     * Create an instance of {@link AttributeDescription }
     * 
     */
    public AttributeDescription createAttributeDescription() {
        return new AttributeDescription();
    }

    /**
     * Create an instance of {@link CompareRequest }
     * 
     */
    public CompareRequest createCompareRequest() {
        return new CompareRequest();
    }

    /**
     * Create an instance of {@link Filter }
     * 
     */
    public Filter createFilter() {
        return new Filter();
    }

    /**
     * Create an instance of {@link ExtendedResponse }
     * 
     */
    public ExtendedResponse createExtendedResponse() {
        return new ExtendedResponse();
    }

    /**
     * Create an instance of {@link ExtendedRequest }
     * 
     */
    public ExtendedRequest createExtendedRequest() {
        return new ExtendedRequest();
    }

    /**
     * Create an instance of {@link MatchingRuleAssertion }
     * 
     */
    public MatchingRuleAssertion createMatchingRuleAssertion() {
        return new MatchingRuleAssertion();
    }

    /**
     * Create an instance of {@link ResultCode }
     * 
     */
    public ResultCode createResultCode() {
        return new ResultCode();
    }

    /**
     * Create an instance of {@link SearchResultEntry }
     * 
     */
    public SearchResultEntry createSearchResultEntry() {
        return new SearchResultEntry();
    }

    /**
     * Create an instance of {@link ErrorResponse.Detail }
     * 
     */
    public ErrorResponse.Detail createErrorResponseDetail() {
        return new ErrorResponse.Detail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BatchResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "batchResponse")
    public JAXBElement<BatchResponse> createBatchResponse(BatchResponse value) {
        return new JAXBElement<BatchResponse>(_BatchResponse_QNAME, BatchResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BatchRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "batchRequest")
    public JAXBElement<BatchRequest> createBatchRequest(BatchRequest value) {
        return new JAXBElement<BatchRequest>(_BatchRequest_QNAME, BatchRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FilterSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "and", scope = FilterSet.class)
    public JAXBElement<FilterSet> createFilterSetAnd(FilterSet value) {
        return new JAXBElement<FilterSet>(_FilterSetAnd_QNAME, FilterSet.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Filter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "not", scope = FilterSet.class)
    public JAXBElement<Filter> createFilterSetNot(Filter value) {
        return new JAXBElement<Filter>(_FilterSetNot_QNAME, Filter.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FilterSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "or", scope = FilterSet.class)
    public JAXBElement<FilterSet> createFilterSetOr(FilterSet value) {
        return new JAXBElement<FilterSet>(_FilterSetOr_QNAME, FilterSet.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MatchingRuleAssertion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "extensibleMatch", scope = FilterSet.class)
    public JAXBElement<MatchingRuleAssertion> createFilterSetExtensibleMatch(MatchingRuleAssertion value) {
        return new JAXBElement<MatchingRuleAssertion>(_FilterSetExtensibleMatch_QNAME, MatchingRuleAssertion.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeValueAssertion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "greaterOrEqual", scope = FilterSet.class)
    public JAXBElement<AttributeValueAssertion> createFilterSetGreaterOrEqual(AttributeValueAssertion value) {
        return new JAXBElement<AttributeValueAssertion>(_FilterSetGreaterOrEqual_QNAME, AttributeValueAssertion.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeValueAssertion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "equalityMatch", scope = FilterSet.class)
    public JAXBElement<AttributeValueAssertion> createFilterSetEqualityMatch(AttributeValueAssertion value) {
        return new JAXBElement<AttributeValueAssertion>(_FilterSetEqualityMatch_QNAME, AttributeValueAssertion.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubstringFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "substrings", scope = FilterSet.class)
    public JAXBElement<SubstringFilter> createFilterSetSubstrings(SubstringFilter value) {
        return new JAXBElement<SubstringFilter>(_FilterSetSubstrings_QNAME, SubstringFilter.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeDescription }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "present", scope = FilterSet.class)
    public JAXBElement<AttributeDescription> createFilterSetPresent(AttributeDescription value) {
        return new JAXBElement<AttributeDescription>(_FilterSetPresent_QNAME, AttributeDescription.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeValueAssertion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "lessOrEqual", scope = FilterSet.class)
    public JAXBElement<AttributeValueAssertion> createFilterSetLessOrEqual(AttributeValueAssertion value) {
        return new JAXBElement<AttributeValueAssertion>(_FilterSetLessOrEqual_QNAME, AttributeValueAssertion.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeValueAssertion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "approxMatch", scope = FilterSet.class)
    public JAXBElement<AttributeValueAssertion> createFilterSetApproxMatch(AttributeValueAssertion value) {
        return new JAXBElement<AttributeValueAssertion>(_FilterSetApproxMatch_QNAME, AttributeValueAssertion.class, FilterSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "searchResponse", scope = BatchResponse.class)
    public JAXBElement<SearchResponse> createBatchResponseSearchResponse(SearchResponse value) {
        return new JAXBElement<SearchResponse>(_BatchResponseSearchResponse_QNAME, SearchResponse.class, BatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "modifyResponse", scope = BatchResponse.class)
    public JAXBElement<LDAPResult> createBatchResponseModifyResponse(LDAPResult value) {
        return new JAXBElement<LDAPResult>(_BatchResponseModifyResponse_QNAME, LDAPResult.class, BatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "addResponse", scope = BatchResponse.class)
    public JAXBElement<LDAPResult> createBatchResponseAddResponse(LDAPResult value) {
        return new JAXBElement<LDAPResult>(_BatchResponseAddResponse_QNAME, LDAPResult.class, BatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "authResponse", scope = BatchResponse.class)
    public JAXBElement<LDAPResult> createBatchResponseAuthResponse(LDAPResult value) {
        return new JAXBElement<LDAPResult>(_BatchResponseAuthResponse_QNAME, LDAPResult.class, BatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "compareResponse", scope = BatchResponse.class)
    public JAXBElement<LDAPResult> createBatchResponseCompareResponse(LDAPResult value) {
        return new JAXBElement<LDAPResult>(_BatchResponseCompareResponse_QNAME, LDAPResult.class, BatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtendedResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "extendedResponse", scope = BatchResponse.class)
    public JAXBElement<ExtendedResponse> createBatchResponseExtendedResponse(ExtendedResponse value) {
        return new JAXBElement<ExtendedResponse>(_BatchResponseExtendedResponse_QNAME, ExtendedResponse.class, BatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "delResponse", scope = BatchResponse.class)
    public JAXBElement<LDAPResult> createBatchResponseDelResponse(LDAPResult value) {
        return new JAXBElement<LDAPResult>(_BatchResponseDelResponse_QNAME, LDAPResult.class, BatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "modDNResponse", scope = BatchResponse.class)
    public JAXBElement<LDAPResult> createBatchResponseModDNResponse(LDAPResult value) {
        return new JAXBElement<LDAPResult>(_BatchResponseModDNResponse_QNAME, LDAPResult.class, BatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErrorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:DSML:2:0:core", name = "errorResponse", scope = BatchResponse.class)
    public JAXBElement<ErrorResponse> createBatchResponseErrorResponse(ErrorResponse value) {
        return new JAXBElement<ErrorResponse>(_BatchResponseErrorResponse_QNAME, ErrorResponse.class, BatchResponse.class, value);
    }

}
