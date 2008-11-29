package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import java.util.Collection;
import java.util.List;

import org.openehealth.ipf.commons.core.modules.api.Aggregator;

/**
 * An aggregator that delegates to a {@link Closure}.
 */
public class DelegatingAggregator extends ClosureAdapter implements Aggregator<Object, Object> {

    public DelegatingAggregator(Closure closure) {
        super(closure);
    }

    public Object zap(Collection<Object> input, Object... params) {
        List<Object> list = (List<Object>)input;
        if (getClosure().getParameterTypes().length == 3) {
            return call(new Object[] {list.get(0), list.get(1), params});
        } else {
            return call(new Object[] {list.get(0), list.get(1)});
        }
    }
    
}