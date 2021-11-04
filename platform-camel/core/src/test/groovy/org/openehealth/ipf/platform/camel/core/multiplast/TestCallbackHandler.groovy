/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.multiplast

import javax.security.auth.callback.CallbackHandler
import javax.security.auth.callback.Callback
import javax.security.auth.callback.NameCallback
import javax.security.auth.callback.PasswordCallback

/**
 * @author Boris Stanojevic
 */
class TestCallbackHandler implements CallbackHandler {

    void handle(Callback[] callbacks) {

        try {
            for (Callback callback : callbacks){
                if (callback instanceof NameCallback){
                    NameCallback nc = (NameCallback)callback
                    String name = 'ipf-client-key'
                    nc.setName(name)
                    println "${nc.prompt} ${nc.name}"
                } else if (callback instanceof PasswordCallback){
                    PasswordCallback pc = (PasswordCallback)callback
                    String pw = 'changeit'
                    pc.setPassword(pw.toCharArray())
                    println "${pc.prompt} :)"
                    pw = null
                }
            }
        } catch (IOException e){
            println "Exception: ${e}"
        }

    }

}
