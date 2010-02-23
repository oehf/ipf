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
package org.openehealth.ipf.commons.test.performance.throughput;

import java.util.List;


/**
 * @author Mitko Kolev
 */
public class ThroughputDistribution {

    private final List<Throughput> intervalThroughputs;

    private final Throughput cumulativeThroughput;
    
    public ThroughputDistribution(Throughput cumulativeThroughput, List<Throughput> intervalThroughput) {
        intervalThroughputs = intervalThroughput;
        this.cumulativeThroughput = cumulativeThroughput;
    }

    public List<Throughput> getThroughput() {
        return intervalThroughputs;
    }
  
    /**
     * @return the cumulativeThroughput
     */
    public Throughput getCumulativeThroughput() {
        return cumulativeThroughput;
    }
}
