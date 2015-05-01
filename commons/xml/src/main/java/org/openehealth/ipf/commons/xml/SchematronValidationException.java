/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.xml;

import lombok.Getter;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.xml.svrl.SchematronOutput;

import java.util.List;

/**
 * @author Dmytro Rud
 */
public class SchematronValidationException extends ValidationException {
    private static final long serialVersionUID = 5786460480736649392L;

    @Getter private final SchematronOutput svrl;

    public SchematronValidationException(List<? extends Throwable> causes, SchematronOutput svrl) {
        super(causes);
        this.svrl = svrl;
    }
}
