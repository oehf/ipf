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

import static org.openehealth.ipf.commons.test.performance.utils.NumberUtils.format

import java.io.IOException
import java.io.OutputStream
import java.io.Writer
import java.util.List
import java.util.concurrent.TimeUnit
import javax.xml.transform.Result

import groovy.xml.MarkupBuilder

import org.apache.commons.lang.NotImplementedException
import org.openehealth.ipf.commons.test.performance.Statistics
import org.openehealth.ipf.commons.test.performance.StatisticsRenderer

/**
 * @author Mitko Kolev
 */
class ThroughputStatisticsRenderer implements StatisticsRenderer {
    
    static String DATE_PATTERN = 'd MMM HH:mm:ss'
    
    String render(Statistics model) {
        Throughput throughput = model.throughput
        renderThroughput(throughput)
    }
    
    String renderThroughput(Throughput throughput){
        StringWriter writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)
        
        builder.throughput(){
            h3('Throughput')
            table(border:'1'){
                th('Count of processed messages')
                th('From')
                th('To')
                th('Duration (seconds)')
                th('Throughput (messages / second)')
                tr(){
                    td(throughput.count)
                    td(throughput.from.format(DATE_PATTERN))
                    td(throughput.to.format(DATE_PATTERN))
                    td(format(throughput.durationSeconds))
                    td(format(throughput.valueSeconds))
                }
            }
        }
        writer.toString()
    }
    
}
