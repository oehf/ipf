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

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Martin Krasser
 */
public class FlowRepositoryImpl extends HibernateDaoSupport implements FlowRepository {

    private static final Log LOG = LogFactory.getLog(FlowRepositoryImpl.class);
    
    @Autowired(required = false)
    private SequenceRepository sequenceRepository;
    
    @PostConstruct
    public void init() {
        if (sequenceRepository == null) {
            sequenceRepository = new SequenceRepositoryMock();
            LOG.warn("no sequence repository injected, using mock repository");
        }
        sequenceRepository.initSequence();
    }
    
    public void persist(Flow flow) {
        // Generate a sequence number for flow
        flow.setIdentifier(sequenceRepository.nextNumber());
        // Persist flow using the assigned number
        getHibernateTemplate().persist(flow);
    }

    public void merge(Flow flow) {
        getHibernateTemplate().merge(flow);
    }

    public void remove(Flow flow) {
        getHibernateTemplate().delete(flow);
    }

    public Flow find(Long id) {
        Flow flow = (Flow)getHibernateTemplate().get(Flow.class, id);
        if (flow == null) {
            throw new FlowException("no flow with id " + id);
        }
        return flow;
    }

    public Flow lock(Long id) {
        try {
            return (Flow)getHibernateTemplate().load(Flow.class, id, LockMode.UPGRADE);
        } catch (HibernateObjectRetrievalFailureException e) {
            throw new FlowException("no flow with id " + id);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Flow> findFlows(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(createFlowsCriteria(finderCriteria), session, 
                        finderCriteria.getMaxResults(), false);
            }
        });        
    }
    
    @SuppressWarnings("unchecked")
    public List<Flow> findErrorFlows(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(createErrorFlowsCriteria(finderCriteria), session, 
                        finderCriteria.getMaxResults(), false);
            }
        });        
    }
    
    @SuppressWarnings("unchecked")
    public List<Flow> findUnackFlows(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(createUnackFlowsCriteria(finderCriteria), session, 
                        finderCriteria.getMaxResults(), false);
            }
        });        
    }
    
    @SuppressWarnings("unchecked")
    public List<Long> findFlowIds(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(createFlowsCriteria(finderCriteria), session, 
                        finderCriteria.getMaxResults(), true);
            }
        });        
    }

    @SuppressWarnings("unchecked")
    public List<Long> findErrorFlowIds(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(createErrorFlowsCriteria(finderCriteria), session, 
                        finderCriteria.getMaxResults(), true);
            }
        });        
    }

    @SuppressWarnings("unchecked")
    public List<Long> findUnackFlowIds(final FlowFinderCriteria finderCriteria) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return execute(createUnackFlowsCriteria(finderCriteria), session, 
                        finderCriteria.getMaxResults(), true);
            }
        });        
    }

    private static Object execute(DetachedCriteria detachedCriteria, Session session, 
            int maxResults, boolean idProjection) {
        
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        
        if (idProjection) {
            criteria.setProjection(Projections.id());
        }
        if (maxResults != FlowFinderCriteria.DEFAULT_MAX_RESULTS) {
            criteria.setMaxResults(maxResults);
        }
       
        return criteria.list();
    }
    
    private static DetachedCriteria createFlowsCriteria(FlowFinderCriteria finderCriteria) {
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Flow.class)
            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
            .setFetchMode("parts", FetchMode.JOIN) // eager
            .add(Restrictions.ge("creationTime", finderCriteria.getFrom()))
            .addOrder(Order.desc("identifier"));
        
        if (finderCriteria.getApplication() != null) {
            // constrain query to a certain application name
            criteria.add(Restrictions.eq("application", finderCriteria.getApplication()));
        }
        if (finderCriteria.getTo() != null) {
            // there's an upper limit to creationTime property
            criteria.add(Restrictions.le("creationTime", finderCriteria.getTo()));
        }
        
        return criteria;
    }
    
    private static DetachedCriteria createErrorFlowsCriteria(FlowFinderCriteria finderCriteria) {
        return createFlowsCriteria(finderCriteria)
            .createAlias("parts", "p")
            .add(Restrictions.eq("p.status", FlowStatus.ERROR));
    }
    
    private static DetachedCriteria createUnackFlowsCriteria(FlowFinderCriteria finderCriteria) {
        return createFlowsCriteria(finderCriteria)
            .add(Restrictions.isEmpty("parts"));
    }
    
}
