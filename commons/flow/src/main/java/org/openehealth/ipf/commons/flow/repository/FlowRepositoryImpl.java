/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.flow.repository;

import static org.hibernate.criterion.Restrictions.*;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openehealth.ipf.commons.flow.FlowException;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowStatus;
import org.openehealth.ipf.commons.flow.repository.FlowPurgeCriteria.PurgeMode;
import org.openehealth.ipf.commons.flow.repository.search.DefaultSearchCallback;
import org.openehealth.ipf.commons.flow.repository.search.FlowSearchCallback;
import org.openehealth.ipf.commons.flow.repository.search.FlowSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

/**
 * @author Martin Krasser
 * @author Mitko Kolev 
 */
public class FlowRepositoryImpl extends HibernateDaoSupport implements FlowRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FlowRepositoryImpl.class);

    @Autowired(required = false)
    private SequenceRepository sequenceRepository;

    @Autowired(required = false)
    private final FlowSearchCallback flowSearchCallback;
    
    public FlowRepositoryImpl() {
        flowSearchCallback = new DefaultSearchCallback();
    }
    
    @Override
    public void initDao() throws Exception {
        super.initDao();
        if (sequenceRepository == null) {
            sequenceRepository = new SequenceRepositoryMock();
            LOG.warn("no sequence repository injected, using mock repository");
        }
        sequenceRepository.initSequence();
    }

    @Override
    public void persist(Flow flow) {
        // Generate a sequence number for flow
        flow.setIdentifier(sequenceRepository.nextNumber());
        // Persist flow using the assigned number
        getHibernateTemplate().persist(flow);
    }

    @Override
    public void merge(Flow flow) {
        getHibernateTemplate().merge(flow);
    }

    @Override
    public void remove(Flow flow) {
        getHibernateTemplate().delete(flow);
    }

    void removeAll(Collection<Flow> flows) {
        getHibernateTemplate().deleteAll(flows);
    }

    @Override
    public Flow find(Long id) {
        Flow flow = getHibernateTemplate().get(Flow.class, id);
        if (flow == null) {
            throw new FlowException("no flow with id " + id);
        }
        return flow;
    }

    @Override
    public Flow lock(Long id) {
        try {
            return getHibernateTemplate().load(Flow.class, id, LockMode.UPGRADE);
        } catch (HibernateObjectRetrievalFailureException e) {
            throw new FlowException("no flow with id " + id);
        }
    }

    @Override
    public int purgeFlows(FlowPurgeCriteria purgeCriteria) {
        final List<Flow> purgeCandidates = findPurgeCandidates(purgeCriteria);
        getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                for (Flow object : purgeCandidates){
                    session.delete(object);
                }
                session.flush();
                return null;
            }
        });
        return purgeCandidates.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Flow> findFlows(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().<List<Flow>>executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(finderCriteria, createFlowsCriteria(finderCriteria), session, false);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Flow> findErrorFlows(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().<List<Flow>>executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(finderCriteria, createErrorFlowsCriteria(finderCriteria), session, false);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Flow> findUnackFlows(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().<List<Flow>>executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(finderCriteria, createUnackFlowsCriteria(finderCriteria), session, false);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Long> findFlowIds(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().<List<Long>>executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(finderCriteria, createFlowsCriteria(finderCriteria), session, true);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Long> findErrorFlowIds(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().<List<Long>>executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(finderCriteria, createErrorFlowsCriteria(finderCriteria), session, true);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Long> findUnackFlowIds(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().<List<Long>>executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(finderCriteria, createUnackFlowsCriteria(finderCriteria),session, true);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private List<Flow> findPurgeCandidates(final FlowPurgeCriteria purgeCriteria) {
        return getHibernateTemplate().<List<Flow>>executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(purgeCriteria, createPurgeCriteria(purgeCriteria), session);
            }
        });
    }

    private Object execute(FlowFinderCriteria flowFinderCriteria,
            DetachedCriteria flowStatusCriteria, Session session,
            boolean idProjection) {

        Criteria criteria = flowStatusCriteria.getExecutableCriteria(session);

        if (idProjection) {
            criteria.setProjection(Projections.id());
        }
        
        int maxResults = flowFinderCriteria.getMaxResults();
        if (maxResults != FlowFinderCriteria.DEFAULT_MAX_RESULTS) {
            criteria.setMaxResults(maxResults);
        }

        if (flowFinderCriteria.hasMessageQuery()) {
            FlowSearchCriteria flowSearchCriteria = new FlowSearchCriteria();
            flowSearchCriteria.setHibernateCriteria(criteria);
            flowSearchCriteria.setInboundMessageQuery(flowFinderCriteria.getInboundMessageQuery());
            flowSearchCriteria.setOutboundMessageQuery(flowFinderCriteria.getOutboundMessageQuery());
            return flowSearchCallback.findFlows(session, flowSearchCriteria);
        } else {
            return criteria.list();
        }
    }

    private Object execute(FlowPurgeCriteria flowPurgeCriteria,
            DetachedCriteria flowStatusCriteria, Session session) {
        Criteria criteria = flowStatusCriteria.getExecutableCriteria(session);

        int purgeCount = flowPurgeCriteria.getMaxPurgeCount();
        if (purgeCount != FlowPurgeCriteria.DEFAULT_MAX_PURGE_COUNT) {
            // Setting max results adds "fetch first 100 rows only" to the select query
            // Works on Derby version 1.5.x and above with Hibernate version 3.5.x and above
            criteria.setMaxResults(purgeCount);
        }
        return criteria.list();
    }
    
    private static DetachedCriteria createFlowsCriteria(FlowFinderCriteria finderCriteria) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Flow.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .setFetchMode("parts", FetchMode.JOIN) // eager
                .add(ge("creationTime", finderCriteria.getFrom()))
                .addOrder(Order.desc("identifier"));

        if (finderCriteria.getApplication() != null) {
            // constrain query to a certain application name
            criteria.add(eq("application", finderCriteria
                    .getApplication()));
        }
        if (finderCriteria.getTo() != null) {
            // there's an upper limit to creationTime property
            criteria.add(Restrictions
                    .le("creationTime", finderCriteria.getTo()));
        }

        return criteria;
    }

    private static DetachedCriteria createErrorFlowsCriteria(FlowFinderCriteria finderCriteria) {
        return createFlowsCriteria(finderCriteria).createAlias("parts", "p").add(
                eq("p.status", FlowStatus.ERROR));
    }

    private static DetachedCriteria createUnackFlowsCriteria(FlowFinderCriteria finderCriteria) {
        return createFlowsCriteria(finderCriteria).add(
                isEmpty("parts"));
    }

    private static DetachedCriteria createPurgeCriteria(FlowPurgeCriteria purgeCriteria) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Flow.class)
            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
            // purge all flows with a creation time older than time limit
            .add(lt("creationTime", purgeCriteria.getTimeLimit()))
            // purge oldest flows first
            .addOrder(Order.asc("creationTime"));

        if (purgeCriteria.getApplication() != null) {
            criteria.add(eq("application", purgeCriteria.getApplication()));
        }
        
        if (purgeCriteria.getPurgeMode() == PurgeMode.CLEAN) {
            // omit flows with error status
            criteria.add(
                or(
                    eq("derivedStatus", FlowStatus.CLEAN),
                    isNull("derivedStatus") // old flows (both CLEAN and ERROR)
                ) 
            );
            
        }
        return criteria;
    }

}
