package org.openehealth.ipf.commons.core.config;

import java.util.Collection;
import org.springframework.beans.factory.ListableBeanFactory;
/**
 * Base class for all custom configurers which have to implement their own strategy
 * for lookup and configure.
 * 
 * @author Boris Stanojevic
 */
public abstract class SpringConfigurer<T> implements Configurer<T>, Comparable<SpringConfigurer<T>>{

    private int order = Integer.MAX_VALUE;
    
    /**
     *
     * @param beanFactory a BeanFactory to make lookup-by-type on
     * @return Collection of custom configuration objects
     */
    public abstract Collection<T> lookup(ListableBeanFactory beanFactory);

    @Override
    public int compareTo(SpringConfigurer<T> configurer) {
        if (configurer.getOrder() < getOrder()){
            return 1;
        }else if(configurer.getOrder() > getOrder()){
            return -1;
        }else{
            return 0;
        }
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
