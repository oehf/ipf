/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws;

import java.util.List;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

/**
 * Helper methods for handling user-defined custom interceptors.
 * @author Dmytro Rud
 */
abstract public class InterceptorUtils {

    private InterceptorUtils() {
        throw new IllegalStateException("Cannot instantiate utility class");
    }

    
    public static void copyInterceptorsFromProvider(
            InterceptorProvider source, 
            InterceptorProvider target) 
    {
        if (source != null) {
            copyInterceptorsFromList(source.getInInterceptors(), 
                                     target.getInInterceptors());
            copyInterceptorsFromList(source.getInFaultInterceptors(), 
                                     target.getInFaultInterceptors());
            copyInterceptorsFromList(source.getOutInterceptors(), 
                                     target.getOutInterceptors());
            copyInterceptorsFromList(source.getOutFaultInterceptors(), 
                                     target.getOutFaultInterceptors());
        }
    }

    private static void copyInterceptorsFromList(
            List<Interceptor<? extends Message>> source, 
            List<Interceptor<? extends Message>> target) 
    {
        if (source != null) {
            target.addAll(source);
        }
    }


    /**
     * Searches for a property in all available contexts
     * associated with the given SOAP message.
     *
     * @param message
     *      CXF message.
     * @param propertyName
     *      name of the property.
     * @param <T>
     *      type of the property.
     * @return
     *      property value, or <code>null</code> when not found.
     */
    public static <T> T findContextualProperty(Message message, String propertyName) {
        Exchange exchange = message.getExchange();
        Message[] messages = new Message[] {
                message,
                exchange.getInMessage(),
                exchange.getOutMessage(),
                exchange.getInFaultMessage(),
                exchange.getOutFaultMessage()
        };

        for (Message m : messages) {
            if (m != null) {
                T t  = (T) m.getContextualProperty(propertyName);
                if (t != null) {
                    return t;
                }
            }
        }
        return null;
    }

}
