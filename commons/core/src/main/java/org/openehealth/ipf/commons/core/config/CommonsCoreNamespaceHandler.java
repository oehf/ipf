package org.openehealth.ipf.commons.core.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class CommonsCoreNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("globalContext", new CommonsCoreBeanDefinitionParser());   
    }

}
