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
import org.hibernate.Session;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowPart;

/**
 * @author Martin Krasser
 */
public class DefaultSearchCallback implements FlowSearchCallback {

    private Analyzer inboundTextAnalyzer = Query.DEFAULT_INBOUND_TEXT_ANALYZER;
    private Analyzer outboundTextAnalyzer = Query.DEFAULT_OUTBOUND_TEXT_ANALYZER;
    
    public void setInboundTextAnalyzer(Analyzer inboundTextAnalyzer) {
        this.inboundTextAnalyzer = inboundTextAnalyzer;
    }

    public void setOutboundTextAnalyzer(Analyzer outboundTextAnalyzer) {
        this.outboundTextAnalyzer = outboundTextAnalyzer;
    }
    
    @Override
    public List<Flow> findFlows(Session session, FlowSearchCriteria criteria) {
        return Query.flowQuery(session, inboundTextAnalyzer).listFlows(
                criteria.getInboundMessageQuery(), 
                criteria.getHibernateCriteria());
    }
    
    public List<Flow> findFlowsByMessageQuery(Session session, String inboundMessageQuery) {
        return Query.flowQuery(session, inboundTextAnalyzer).listFlows(inboundMessageQuery);
    }
    
    public List<FlowPart> findFlowPartsByMessageQuery(Session session, String outboundMessageQuery) {
        return Query.flowPartQuery(session, outboundTextAnalyzer).listFlowParts(outboundMessageQuery);
    }
    
}
