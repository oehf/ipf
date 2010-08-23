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
package org.openehealth.ipf.tutorials.xds

import java.util.concurrent.Callable


class Task implements Callable<Throwable> {
    def code
    def container
    
    @Override
    public Throwable call() throws Exception {
        def start = new Date().time
        try {
            code()
            null               
        }
        catch (Throwable e) {
            println(Thread.currentThread().id + ' -> Exception')
            e
        }
        finally {
            def end = new Date().time
            println (Thread.currentThread().id + ' > Task (' + container.taskCounter.getAndIncrement() + ') finished (' + (end - start) + 'ms): ' + this)
        }        
    }
}

