/*
 * Copyright 2010 the original author or authors.
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
package config

import org.openehealth.ipf.commons.core.modules.api.Transmogrifier
import groovy.xml.MarkupBuilder


class HtmlTransmogrifier implements Transmogrifier {

    Object zap(Object msg, Object... params) {
        StringWriter writer = new StringWriter()
        def build = new MarkupBuilder(writer)
        build.html{
            head{
            style(type:"text/css", '''
            .normalCell {
                background-color: #CCCCCC
            }
            .headerCell {
                background-color: #6699FF;
                font-weight: bold;
                text-align: center
            }
            ''')
            }
            body{
                table{
                    tr{
                        td('class':'headerCell', 'Date')
                        td('class':'headerCell', 'Message')
                        td('class':'headerCell', 'Info')
                    }
                    tr{  
                        td('class':'normalCell', "${new Date(System.currentTimeMillis())}")
                        td('class':'normalCell', "${new String(msg)}")
                        td('class':'normalCell'){
                            a(href:'http://oehf.github.io/ipf/', 'read more about IPF')
                        }
                    }
                }
            }
        }
        msg = writer.toString()
    }

}