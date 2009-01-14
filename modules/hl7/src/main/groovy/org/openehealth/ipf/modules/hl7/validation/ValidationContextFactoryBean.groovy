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

/**
 * FactoryBean to be used if a DefaultValidationContext shall be initialized from a
 * Spring application context.
 * This factory bean retrieves all ValidationContextBuilder beans from the
 * application context calls their configure() method in order to add the 
 * rules to the DefaultValidationContext.
 * 
 * @author Christian Ohr
 */
public class ValidationContextFactoryBean extends AbstractFactoryBean implements ApplicationContextAware {

    private ApplicationContext applicationContext
    
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
        Map builders = applicationContext.getBeansOfType(ValidationContextBuilder.class)
        builders?.values().each { builder ->
            builder.forContext(validationContext)
        }
        return validationContext
    }
    
}
