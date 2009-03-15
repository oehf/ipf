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
package org.openehealth.ipf.platform.camel.core.support.transform.min;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.transform.Result;

import org.openehealth.ipf.commons.core.modules.api.Renderer;

/**
 * @author Martin Krasser
 */
public class TestRenderer implements Renderer<String> {

    public Result render(String model, Result result, Object... params) {
        throw new UnsupportedOperationException("not implemented");
    }

    public OutputStream render(String model, OutputStream result, Object... params) throws IOException {
        throw new UnsupportedOperationException("not implemented");
    }

    public Writer render(String model, Writer result, Object... params) throws IOException {
        throw new UnsupportedOperationException("not implemented");
    }

    public String render(String model, Object... params) {
        return "rendered: " + model;
    }

}
