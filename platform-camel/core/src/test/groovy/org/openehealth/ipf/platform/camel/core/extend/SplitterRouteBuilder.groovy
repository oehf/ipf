/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.extend

import static org.apache.camel.builder.Builder.*

import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder

import org.openehealth.ipf.platform.camel.core.process.splitter.SplitterTest
import org.openehealth.ipf.platform.camel.core.process.splitter.support.TextFileIterator
import org.openehealth.ipf.platform.camel.core.process.splitter.support.SplitStringLineSplitterLogic

/**
 * @author Jens Riemschneider
 */
class SplitterRouteBuilder extends SpringRouteBuilder {

     void configure() {
         
         errorHandler(noErrorHandler())
          
          from('direct:split_rule_as_closure') 
              .split { exchange -> new SplitterTest.TestSplitRule().evaluate(exchange) }
              .aggregate { oldExchange, newExchange -> 
                  new SplitterTest.TestAggregationStrategy().aggregate(oldExchange, newExchange) }  
              .to('mock:output')

          from('direct:split_rule_returns_array') 
              .split { exchange -> exchange.in.body.split(',') }
              .aggregate { oldExchange, newExchange -> 
                  new SplitterTest.TestAggregationStrategy().aggregate(oldExchange, newExchange) }  
              .to('mock:output')
              
          from('direct:split_rule_as_type') 
              .split( new SplitterTest.TestSplitRule() )
              .aggregate { oldExchange, newExchange -> 
                  new SplitterTest.TestAggregationStrategy().aggregate(oldExchange, newExchange) }  
              .to('mock:output')

          from('direct:aggregation_via_closure') 
              .split { exchange -> new SplitterTest.TestSplitRule().evaluate(exchange) }
              .aggregate { oldExchange, newExchange ->
                  String oldContent = oldExchange.in.body
                  String newContent = newExchange.in.body
                  Exchange aggregate = oldExchange.copy()
                  aggregate.in.body = oldContent + ":" + newContent
                  aggregate
              }
              .to('mock:output')
              
          from('direct:split_rule_via_bean')
              .split('sampleSplitRule')
              .to('mock:output')
              
          from('direct:split_default_aggr') 
              .split( new SplitterTest.TestSplitRule() )
              .to('mock:output')
              
          from('direct:split_only_once_iterator') 
              .split( new SplitterTest.TestSplitRuleSingleUse() )
              .to('mock:output')
              
          from('direct:split_default_update') 
              .split( new SplitterTest.TestSplitRuleSingleUse() )
              .to('mock:output')
              
          from('direct:split_read_file_lines')
              .split { exchange -> 
                  return new TextFileIterator(exchange.in.body);
              }
              .to('mock:output')

          from('direct:split_huge_file')
              .split { exchange -> 
                  String filename = exchange.in.body;
                  return new TextFileIterator(filename, new SplitStringLineSplitterLogic(','));
              }
              .filter { exchange -> exchange.in.body == 'Line 1' }
              .to('mock:output')
              
          from('direct:split_complex_route') 
              .split( new SplitterTest.TestSplitRule() )              
              .filter { exchange -> exchange.in.body == 'blu' }
              .setHeader("foo", "bar")
              .setHeader("smurf", "blue")
              .to('mock:output')
              
     }
    
}
