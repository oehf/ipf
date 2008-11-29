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
        this.causes = new Throwable[] { cause };
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.causes = new Throwable[] { cause };
    }

    public ValidationException(String message, Throwable[] causes) {
        super(message, causes[0]);
        this.causes = causes;
    }

    public ValidationException(String message, List<? extends Throwable> causes) {
        this(message, (Throwable[]) causes.toArray(new Throwable[causes.size()]));
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable[] causes) {
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
        String msg = super.getMessage();
        if (causes != null) {
            for (int i = 0; i < causes.length; i++) {
                msg += "\n" + causes[i].getMessage();
            }
        }
        return msg;
    }

}
