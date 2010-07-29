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

    @SuppressWarnings("unchecked")
    private static void copyInterceptorsFromList(
            List<Interceptor> source, 
            List<Interceptor> target) 
    {
        if (source != null) {
            target.addAll(source);
        }
    }
}
