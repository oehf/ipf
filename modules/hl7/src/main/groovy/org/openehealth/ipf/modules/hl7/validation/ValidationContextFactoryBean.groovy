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
package org.openehealth.ipf.modules.hl7.validation

import org.springframework.beans.factory.config.AbstractFactoryBean
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext

import org.openehealth.ipf.modules.hl7.validation.builder.ValidationContextBuilder

/**
 * FactoryBean to be used if a DefaultValidationContext shall be initialized from a
 * Spring application context.
 * By default, this factory bean retrieves all ValidationContextBuilder beans from the
 * application context calls their configure() method in order to add the 
 * rules to the DefaultValidationContext.
 * You can also set the ValidationContextBuilders directly to the <code>builders</code> 
 * collection property to override autodetection.
 * 
 * @author Christian Ohr
 */
public class ValidationContextFactoryBean extends AbstractFactoryBean implements ApplicationContextAware {

    private ApplicationContext applicationContext
    private Collection<ValidationContextBuilder> builders
    
    public void setApplicationContext(ApplicationContext context) {
        this.applicationContext = context
    }
    
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#getObjectType()
     */
    public Class getObjectType(){
        DefaultValidationContext.class
    }
    
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
     */
    public Object createInstance(){
        def validationContext = new DefaultValidationContext()
        if (builders == null || builders.size() == 0) {
            builders = applicationContext.getBeansOfType(ValidationContextBuilder.class)?.values()
        }
        builders?.each { builder ->
            builder.forContext(validationContext)
        }
        return validationContext
    }

    public void setBuilders(Collection<ValidationContextBuilder> builders) {
        this.builders = builders
    }
    
    public Collection<ValidationContextBuilder> getBuilders() {
        return builders
    }
    
}
