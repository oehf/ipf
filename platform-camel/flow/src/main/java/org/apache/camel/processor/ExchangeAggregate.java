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
package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.processor.MulticastProcessor.ProcessorExchangePair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;


/**
 * An {@link Aspect} to preserve the {@link SplitHistory} of exchanges that are
 * processed by the {@link MulticastProcessor#process(Exchange)} method. This
 * aspect is by default not active unless {@link #ASPECT_ACTIVE_SYSTEM_PROPERTY}
 * is set to <code>true</code>.
 * 
 * @author Martin Krasser
 */
@Aspect
public class ExchangeAggregate {

    @SuppressWarnings("unused") private MulticastProcessor multicast;
    @SuppressWarnings("unused") private ProcessorExchangePair pair;
    
    @SuppressWarnings("unused")
    @Pointcut("execution(void MulticastProcessor.process(Exchange))")
    private void multicastExchangeProcess() {}
    
    /**
     * Delegates to {@link #doAroundExchangeProcess(ProceedingJoinPoint)} only
     * if this aspect is activated.
     * 
     * @param pjp
     *            proceeding join point.
     * @throws Throwable
     */
    @Around("multicastExchangeProcess()")
    public void aroundExchangeProcess(ProceedingJoinPoint pjp) throws Throwable {
        doAroundExchangeProcess(pjp);
    }
    
    /**
     * Preserves the {@link SplitHistory} of exchanges that are processed by the
     * {@link MulticastProcessor#process(Exchange)} method. 
     * 
     * @param pjp
     *            proceeding join point.
     * @throws Throwable
     */
    protected void doAroundExchangeProcess(ProceedingJoinPoint pjp) throws Throwable {
        ManagedMessage message = getMessage(pjp);
        SplitHistory original = message.getSplitHistory();
        pjp.proceed(pjp.getArgs());
        SplitHistory current = message.getSplitHistory();
        if (!original.equals(current)) {
            message.setSplitHistory(original);
        }
    }
    
    private ManagedMessage getMessage(ProceedingJoinPoint pjp) {
        return new PlatformMessage((Exchange)pjp.getArgs()[0]);
    }
    
}
