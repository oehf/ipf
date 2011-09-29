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
package org.openehealth.ipf.commons.test.performance;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Mitko Kolev
 */
public class StatisticsManager {

    private final static Log LOG = LogFactory.getLog(StatisticsManager.class);

    private Map<Statistics, StatisticsRenderer> bindings;

    public StatisticsManager() {
        bindings = new HashMap<Statistics, StatisticsRenderer>();
    }

    /**
     * Updates all contained statistics with the given
     * <code>measurementHistory</code>.
     * 
     * @param measurementHistory
     *            a not-null {@link MeasurementHistory} instance.
     */
    public void updateStatistics(MeasurementHistory measurementHistory) {
        notNull(measurementHistory, "The measurementHistory must not be null!");
        Set<Statistics> statistics = bindings.keySet();
        for (Statistics ms : statistics) {
            ms.update(measurementHistory);
        }
    }

    /**
     * Resets the contained statistics.
     */
    public void resetStatistics() {
        for (Statistics ms : bindings.keySet()) {
            ms.reset();
        }
        LOG.info("Statistics reset");
    }
    public  List<Statistics> getStatistics() {
        return new ArrayList<Statistics>(bindings.keySet());
    }
    
    public  StatisticsRenderer getRenderer(Statistics statistics) {
        return bindings.get(statistics);
    }
    
    protected Map<Statistics, StatisticsRenderer> getBindings() {
        return bindings;
    }
    /**
     * @param bindings
     *            the bindings to set
     */
    public void setBindings(Map<Statistics, StatisticsRenderer> bindings) {
        notEmpty(bindings, "The bindings must not be empty!");
        this.bindings = bindings;
    }
}
