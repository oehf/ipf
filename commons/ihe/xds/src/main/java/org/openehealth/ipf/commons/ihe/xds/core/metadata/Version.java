/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class Version implements Serializable {
    private static final long serialVersionUID = 4876325465142352011L;

    protected String versionName;
    protected String comment;

    /**
     * Constructs a localized string.
     */
    public Version() {
        this(null, null);
    }

    /**
     * Constructs a localized string.
     * @param versionName
     *          the version name.
     */
    public Version(String versionName) {
        this(versionName, null);
    }

    /**
     * Constructs a localized string.
     * @param versionName
     *          the version name.
     * @param comment
     *          the comment.
     */
    public Version(String versionName, String comment) {
        this.versionName = versionName;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version that = (Version) o;

        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (versionName != null ? !versionName.equals(that.versionName) : that.versionName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = versionName != null ? versionName.hashCode() : 0;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

