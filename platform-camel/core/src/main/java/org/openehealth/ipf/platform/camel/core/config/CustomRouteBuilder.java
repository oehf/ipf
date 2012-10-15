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
package org.openehealth.ipf.platform.camel.core.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringRouteBuilder;

/**
 * RouteBuilders, Interceptors, ExceptionClauses should extend this
 * class in order to be picked up and configured/customized from the
 * {@link CustomRouteBuilderConfigurer}. 
 * 
 * @author Boris Stanojevic
 */
public abstract class CustomRouteBuilder extends SpringRouteBuilder
       implements Comparable<CustomRouteBuilder> {

    private RouteBuilder intercepted;


    @Override
    public int compareTo(CustomRouteBuilder customRouteBuilder) {
        if (customRouteBuilder.getIntercepted() == null
                && getIntercepted() != null){
            return -1;
        }else if(customRouteBuilder.getIntercepted() != null
                && getIntercepted() == null){
            return 1;
        }else{
            return 0;
        }
    }

    public RouteBuilder getIntercepted() {
        return intercepted;
    }

    public void setIntercepted(RouteBuilder intercepted) {
        this.intercepted = intercepted;
    }

}