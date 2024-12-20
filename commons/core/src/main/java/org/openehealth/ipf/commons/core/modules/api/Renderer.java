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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.transform.Result;

/**
 * Interface for creating an externalized version of an internal object model.
 * 
 * @author Christian Ohr
 * 
 * @param <T>
 *            the external representation
 */
public interface Renderer<T> {

    /**
     * Renders the model into its external representation.
     * 
     * @param model model to be rendered
     * @param params
     *            dynamic information used during rendering. See the respective
     *            implementation class documentation if this is required or
     *            supported.
     * @return the external representation of the model
     */
    Result render(final T model, Result result, final Object... params) throws IOException;

    /**
     * Renders the model into its external representation.
     * 
     * @param model model to be rendered
     * @param params
     *            dynamic information used during rendering. See the respective
     *            implementation class documentation if this is required or
     *            supported.
     * @return the external representation of the model
     */
    OutputStream render(final T model, OutputStream result, final Object... params) throws IOException;

    /**
     * Renders the model into its external representation.
     * 
     * @param model model to be rendered
     * @param params
     *            dynamic information used during rendering. See the respective
     *            implementation class documentation if this is required or
     *            supported.
     * @return the external representation of the model
     */
    Writer render(final T model, Writer result, final Object... params) throws IOException;

    /**
     * Renders the model into its external representation.
     * 
     * @param model model to be rendered
     * @param params
     *            dynamic information used during rendering. See the respective
     *            implementation class documentation if this is required or
     *            supported.
     * @return the external representation of the model
     */
    String render(final T model, final Object... params);

}
