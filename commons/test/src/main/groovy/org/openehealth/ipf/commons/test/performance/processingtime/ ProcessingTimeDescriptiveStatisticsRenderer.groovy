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
package org.openehealth.ipf.commons.test.performance.processingtime

import static org.openehealth.ipf.commons.test.performance.utils.NumberUtils.format

import java.io.File
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
class ProcessingTimeDescriptiveStatisticsRenderer implements StatisticsRenderer {
    
    String render(Statistics model) {
        if (!model instanceof ProcessingTimeDescriptiveStatistics){
            throw new IllegalArgumentException('Only instances of ProcessingTimeDescriptiveStatistics are allowed!')
        }
        StringWriter writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)
        List names = model.getMeasurementNames()
        builder.processingtime(){
            h3('Processing time data')
            table(border:'1', width:'1000'){
                for (name in names){
                    def summary = model.getStatisticalSummaryByName(name)
                    def data = model.getData(name)
                    StringBuilder measurements = new StringBuilder()
                    for (double d in data){
                        measurements.append(format(d, 0)).append(', ')
                    }
                    measurements.delete(measurements.length() - 2, measurements.length())
                    tr(){th(name)}
                    tr(){ td(measurements) }
                }
            }
        }
        
        writer.toString()
    }
    
    String createFile(Object data, String name){
        StringBuilder measurements = new StringBuilder()
        for (double d in data){
            measurements.append(format(d, 0)).append(', ')
        }
        measurements.delete(measurements.length() - 2, measurements.length())
        String nameNormalized = name.replaceAll(' ', '_')
        String filename = "measurement-location-${nameNormalized}-data.csv";
        File f = new File(filename)
        f.createNewFile()
        f.setWritable(true)
        f.write(measurements.toString())
        filename 
    }
}
