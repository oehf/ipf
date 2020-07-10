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

import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.BlockingQueue
import javax.security.auth.Subject
import java.security.AccessControlContext
import java.security.AccessController
import java.security.PrivilegedAction
import java.util.concurrent.ThreadFactory

/**
 * @author Michael Baumann
 */
class PrivilegedTestExecutorService extends ThreadPoolExecutor {

        PrivilegedTestExecutorService(int corePoolSize, int maximumPoolSize,
                                   long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory factory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, factory
            )
        }

        void execute(Runnable command) {
            super.execute(new SubjectAwareRunnable(command))
        }

        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r)
        }

        private class SubjectAwareRunnable implements Runnable {
            private final Runnable runnable
            private final Subject subject
            private final AccessControlContext ctx

            private SubjectAwareRunnable(Runnable runnable) {
                this.runnable = runnable
                this.subject = currentSubject()
                this.ctx = currentCtx()
            }

            private AccessControlContext currentCtx() {
                return AccessController.getContext()
            }

            private Subject currentSubject() {
                return Subject.getSubject(AccessController.getContext())
            }

            void run() {
                if(subject == null) {
                    runnable.run()
                } else {
                    Subject.doAsPrivileged(
                            subject,
                            new PrivilegedAction<Void>() {
                                Void run() {
                                    runnable.run()
                                    return null
                                }
                            },
                            ctx)
                }
            }
        }
}


