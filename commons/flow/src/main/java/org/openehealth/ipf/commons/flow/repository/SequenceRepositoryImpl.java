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

import static org.openehealth.ipf.commons.flow.domain.FlowNumber.DEFAULT_SEQUENCE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.LockMode;
import org.openehealth.ipf.commons.flow.domain.FlowNumber;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Martin Krasser
 */
public class SequenceRepositoryImpl extends HibernateDaoSupport implements SequenceRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SequenceRepositoryImpl.class);
    
    @Override
    public void initSequence() {
        FlowNumber num = lockNumber(); 
        if (num == null) {
            getHibernateTemplate().persist(new FlowNumber());
            LOG.info("initialized sequence " + DEFAULT_SEQUENCE);
        } else {
            LOG.info("using stored sequence " + DEFAULT_SEQUENCE);
        }
    }
    
    @Override
    public Long nextNumber() {
        return lockNumber().incrementAndGet();
    }
    
    private FlowNumber lockNumber() {
        return (FlowNumber)getHibernateTemplate().get(FlowNumber.class, DEFAULT_SEQUENCE, LockMode.UPGRADE);
    }
    
}
