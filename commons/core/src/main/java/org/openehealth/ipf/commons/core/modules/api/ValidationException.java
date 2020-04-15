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
package org.openehealth.ipf.commons.core.modules.api;

import java.util.List;

/**
 * @author Christian Ohr
 */
@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {

    private Throwable[] causes;

    public ValidationException(Throwable cause) {
        super(cause);
        causes = new Throwable[] { cause };
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        causes = new Throwable[] { cause };
    }

    public ValidationException(String message, Throwable... causes) {
        super(message, ((causes == null) || (causes.length == 0)) ? null : causes[0]);
        this.causes = causes;
    }

    public ValidationException(String message, List<? extends Throwable> causes) {
        this(message, causes.toArray(new Throwable[0]));
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable... causes) {
        this(null, causes);
    }

    public ValidationException(List<? extends Throwable> causes) {
        this(null, causes);
    }

    public Throwable[] getCauses() {
        return causes;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        
        String msg = super.getMessage();
        if(msg != null) {
            sb.append(msg).append('\n');
        }

        if(causes != null) {
            for(Throwable t : causes) {
                if(t != null) {
                    msg = t.getMessage();
                    sb.append((msg == null) ? t.getClass().getName() : msg).append('\n');
                }
            }
        }

        int len = sb.length(); 
        return (len == 0) ? getClass().getName() : sb.deleteCharAt(len - 1).toString();
    }

}
