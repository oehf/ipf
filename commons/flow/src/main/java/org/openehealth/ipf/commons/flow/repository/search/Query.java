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
package org.openehealth.ipf.commons.flow.repository.search;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowPart;

/**
 * @author Martin Krasser
 */
class Query {

    public static final Analyzer DEFAULT_INBOUND_TEXT_ANALYZER = new StandardAnalyzer();
    public static final Analyzer DEFAULT_OUTBOUND_TEXT_ANALYZER = new StandardAnalyzer();

    private static final String INBOUND_TEXT_FIELD = "flowMessage.text";
    private static final String OUTBOUND_TEXT_FIELD = "flowPartMessage.text";

    private final Session session;
    
    private final Analyzer analyzer;

    private final Class<?> domainClass;
    
    private final String field;
    
    private Query(Session session, Analyzer analyzer, Class<?> domainClass, String field) {
        this.session = session;
        this.analyzer = analyzer;
        this.domainClass = domainClass;
        this.field = field;
    }
    
    public static Query flowQuery(Session session) {
        return flowQuery(session, DEFAULT_INBOUND_TEXT_ANALYZER);
    }
    
    public static Query flowPartQuery(Session session) {
        return flowPartQuery(session, DEFAULT_OUTBOUND_TEXT_ANALYZER);
    }
    
    public static Query flowQuery(Session session, Analyzer analyzer) {
        return new Query(session, analyzer, Flow.class, INBOUND_TEXT_FIELD);
    }
    
    public static Query flowPartQuery(Session session, Analyzer analyzer) {
        return new Query(session, analyzer, FlowPart.class, OUTBOUND_TEXT_FIELD);
    }
    
    @SuppressWarnings("unchecked")
    public List<Flow> listFlows(String query) {
        return createFullTextQuery(query).list();
    }
    
    @SuppressWarnings("unchecked")
    public List<FlowPart> listFlowParts(String query) {
        return createFullTextQuery(query).list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Flow> listFlows(String query, Criteria criteria) {
        if (criteria == null) {
            return listFlows(query);
        }
        return createFullTextQuery(query).setCriteriaQuery(criteria).list();
    }
    
    @SuppressWarnings("unchecked")
    public List<FlowPart> listFlowParts(String query, Criteria criteria) {
        if (criteria == null) {
            return listFlowParts(query);
        }
        return createFullTextQuery(query).setCriteriaQuery(criteria).list();
    }
    
    private FullTextQuery createFullTextQuery(String query) {
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(
                createLuceneQuery(query), domainClass);
        return fullTextQuery.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    }
    
    private org.apache.lucene.search.Query createLuceneQuery(String query) {
        try {
            return MultiFieldQueryParser.parse(
                    new String[] {query}, 
                    new String[] {field}, 
                    analyzer);
        } catch (ParseException e) {
            throw new QueryException("Cannot parse query", e);
        }
    }

}
