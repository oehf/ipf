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
package org.openehealth.ipf.commons.test.performance.throughput

import groovy.xml.MarkupBuilder
import org.openehealth.ipf.commons.test.performance.Statistics
import static org.openehealth.ipf.commons.test.performance.utils.NumberUtils.format

/**
 * @author Mitko Kolev
 */
class ThroughputDistributionStatisticsRenderer extends ThroughputStatisticsRenderer {
    
    String binDatePattern = 'HH:mm:ss'  
    
    String render(Statistics model) {
        StringWriter writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)
        ThroughputDistribution distribution = model.throughputDistribution
        
        //render the cumulative throughput
        writer.write(renderThroughput(distribution.cumulativeThroughput))
        
        builder.throughputDistribution(){
            h3('Throughput distribution')
            table(border:'1'){
                th('Time interval')
                th('Count of processed messages')
                th('From')
                th('To')
                th('Duration (seconds)')
                th('Throughput (messages / second)')
                
                int binIndex = 1
                for (Throughput t : distribution.throughput){
                    tr(){
                        td(binIndex)
                        td(t.count)
                        td(t.from.format(binDatePattern))
                        td(t.to.format(binDatePattern))
                        td(format(t.durationSeconds))
                        td(format(t.valueSeconds))
                    }
                    binIndex ++
                }
            }
        }
        writer.toString()
    }
}    
