package org.openehealth.ipf.commons.core.config;

/**
 * Base class for all custom configurers which have to implement their own strategy
 * for lookup and configure.
 * 
 * @author Boris Stanojevic
 */
public abstract class OrderedConfigurer<T, R extends Registry> implements Configurer<T, R>, Comparable<OrderedConfigurer<T, R>>{

    private int order = Integer.MAX_VALUE;
    
    @Override
    public int compareTo(OrderedConfigurer<T, R> configurer) {
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
