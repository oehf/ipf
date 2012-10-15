package org.openehealth.ipf.commons.core.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;


public class CommonsCoreBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ContextFacade.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        builder
            .addConstructorArgValue(new SpringRegistry())
            .setScope(BeanDefinition.SCOPE_SINGLETON);
    }
    
}
