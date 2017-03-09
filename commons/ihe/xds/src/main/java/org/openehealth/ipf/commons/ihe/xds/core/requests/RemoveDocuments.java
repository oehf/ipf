/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.requests;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Request object for the Remove Documents transaction.
 * @since 3.3
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoveDocuments")
@XmlRootElement(name = "removeDocuments")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoveDocuments extends NonconstructiveDocumentSetRequest implements Serializable {
    private static final long serialVersionUID = 8725483535110142072L;
}
