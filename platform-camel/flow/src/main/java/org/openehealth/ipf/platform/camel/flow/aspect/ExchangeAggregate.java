/*
 * Copyright 2008-2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.flow.aspect;

import org.apache.camel.Exchange;
import org.apache.camel.processor.MulticastProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;


/**
 * An {@link Aspect} to preserve the {@link SplitHistory} of exchanges that are
 * processed by the {@link MulticastProcessor#process(Exchange)} method. 
 * 
 * @author Martin Krasser
 */
@Aspect
public class ExchangeAggregate {

    @SuppressWarnings("unused")
    @Pointcut("execution(void org.apache.camel.processor.MulticastProcessor.process" +
    		  "(org.apache.camel.Exchange))")
    private void multicastExchangeProcess() {}

    @SuppressWarnings("unused")
    @Pointcut("execution(boolean org.apache.camel.processor.MulticastProcessor.process" +
              "(org.apache.camel.Exchange, org.apache.camel.AsyncCallback))")
    private void multicastExchangeProcessWithCallback() {}

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
     * Delegates to {@link #doAroundExchangeProcess(ProceedingJoinPoint)} only
     * if this aspect is activated.
     *
     * @param pjp
     *            proceeding join point.
     * @return result of target method call
     * @throws Throwable
     */
    @Around("multicastExchangeProcessWithCallback()")
    public boolean aroundExchangeProcessWithCallback(ProceedingJoinPoint pjp) throws Throwable {
        return (Boolean)doAroundExchangeProcess(pjp);
    }

    /**
     * Preserves the {@link SplitHistory} of exchanges that are processed by the
     * {@link MulticastProcessor#process(Exchange)} method. 
     * 
     * @param pjp
     *            proceeding join point.
     * @throws Throwable
     */
    protected Object doAroundExchangeProcess(ProceedingJoinPoint pjp) throws Throwable {
        ManagedMessage message = getMessage(pjp);
        SplitHistory original = message.getSplitHistory();
        Object result = pjp.proceed(pjp.getArgs());
        SplitHistory current = message.getSplitHistory();
        if (!original.equals(current)) {
            message.setSplitHistory(original);
        }
        return result;
    }
    
    private ManagedMessage getMessage(ProceedingJoinPoint pjp) {
        return new PlatformMessage((Exchange)pjp.getArgs()[0]);
    }
    
}
