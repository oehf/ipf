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

import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.process.splitter.SplitterTest
import org.openehealth.ipf.platform.camel.core.process.splitter.support.SplitStringLineSplitterLogic
import org.openehealth.ipf.platform.camel.core.process.splitter.support.TextFileIterator

/**
 * @author Jens Riemschneider
 */
class SplitterRouteBuilder extends SpringRouteBuilder {

     void configure() {
         
         errorHandler(noErrorHandler())
          
          from('direct:split_rule_as_closure') 
              .ipf().split { exchange -> new SplitterTest.TestSplitRule().evaluate(exchange, Object.class) }
              .aggregationStrategy { oldExchange, newExchange -> 
                  new SplitterTest.TestAggregationStrategy().aggregate(oldExchange, newExchange) }  
              .to('mock:output')

          from('direct:split_rule_returns_array') 
              .ipf().split { exchange -> exchange.in.body.split(',') }
              .aggregationStrategy { oldExchange, newExchange -> 
                  new SplitterTest.TestAggregationStrategy().aggregate(oldExchange, newExchange) }  
              .to('mock:output')
              
          from('direct:split_rule_as_type') 
              .ipf().split( new SplitterTest.TestSplitRule() )
              .aggregationStrategy { oldExchange, newExchange -> 
                  new SplitterTest.TestAggregationStrategy().aggregate(oldExchange, newExchange) }  
              .to('mock:output')

          from('direct:aggregation_via_closure') 
              .ipf().split { exchange -> new SplitterTest.TestSplitRule().evaluate(exchange, Object.class) }
              .aggregationStrategy { oldExchange, newExchange ->
                  String oldContent = oldExchange.in.body
                  String newContent = newExchange.in.body
                  Exchange aggregate = oldExchange.copy()
                  aggregate.in.body = oldContent + ":" + newContent
                  aggregate
              }
              .to('mock:output')
              
          from('direct:split_rule_via_bean')
              .ipf().split('sampleSplitRule')
              .to('mock:output')
              
          from('direct:split_default_aggr') 
              .ipf().split( new SplitterTest.TestSplitRule() )
              .to('mock:output')
              
          from('direct:split_only_once_iterator') 
              .ipf().split( new SplitterTest.TestSplitRuleSingleUse() )
              .to('mock:output')
              
          from('direct:split_default_update') 
              .ipf().split( new SplitterTest.TestSplitRuleSingleUse() )
              .to('mock:output')
              
          from('direct:split_read_file_lines')
              .ipf().split { exchange ->
                  String filename = exchange.in.body
                  new TextFileIterator(filename)
              }
              .to('mock:output')

          from('direct:split_huge_file')
              .ipf().split { exchange -> 
                  String filename = exchange.in.body
                  new TextFileIterator(filename, new SplitStringLineSplitterLogic(','))
              }
              .filter { exchange -> exchange.in.body == 'Line 1' }
              .to('mock:output')
              
          from('direct:split_complex_route') 
              .ipf().split( new SplitterTest.TestSplitRule() )              
              .filter { exchange -> exchange.in.body == 'blu' }
              .setHeader('foo', constant('bar'))
              .setHeader('smurf', constant('blue'))
              .to('mock:output')
              
     }
    
}
