/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.core;

import org.apache.camel.Component;
import org.openehealth.ipf.commons.ihe.core.InteractionId;

/**
 * A Camel component that stands for exactly one eHealth transaction.
 * <p>
 * Implemented by the component base classes of all transaction families, which already provide the
 * accessor with a covariant return type. Its purpose is to let generic code — telemetry in
 * particular — obtain the transaction metadata of an endpoint without knowing which family the
 * endpoint belongs to.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public interface InteractionAwareComponent extends Component {

    /**
     * @return the ID of the eHealth transaction served by this component.
     */
    InteractionId getInteractionId();
}
