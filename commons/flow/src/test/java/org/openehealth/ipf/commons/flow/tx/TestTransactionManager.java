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
package org.openehealth.ipf.commons.flow.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author Martin Krasser
 */
public class TestTransactionManager {

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    private TransactionStatus transactionStatus;

    private int isolationLevel;
    
    public TestTransactionManager() {
        this(null);
    }
    
    public TestTransactionManager(PlatformTransactionManager transactionManager) {
        this(transactionManager, DefaultTransactionDefinition.ISOLATION_DEFAULT);
    }
    
    public TestTransactionManager(PlatformTransactionManager transactionManager, int isolationLevel) {
        this.transactionManager = transactionManager;
        this.isolationLevel = isolationLevel;
    }
    
    public void beginTransaction() {
        DefaultTransactionDefinition td = new DefaultTransactionDefinition();
        td.setIsolationLevel(isolationLevel);
        transactionStatus = transactionManager.getTransaction(td);
    }
    
    public void endTransaction() {
        if (transactionStatus.isRollbackOnly()) {
            rollbackTransaction();
        } else {
            commitTransaction();
        }
    }

    public void commitTransaction() {
        if (!transactionStatus.isCompleted()) {
            transactionManager.commit(transactionStatus);
        }
    }
    
    public void rollbackTransaction() {
        if (!transactionStatus.isCompleted()) {
            transactionManager.rollback(transactionStatus);
        }
    }
    
}
