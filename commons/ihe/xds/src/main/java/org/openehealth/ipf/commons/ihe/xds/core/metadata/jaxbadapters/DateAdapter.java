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
package org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;

public class DateAdapter extends XmlAdapter<Calendar, DateTime> {

    @Override
    public Calendar marshal(DateTime dateTime) throws Exception {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toGregorianCalendar();
    }

    @Override
    public DateTime unmarshal(Calendar calendar) throws Exception {
        if (calendar == null) {
            return null;
        }
        return new DateTime(calendar).toDateTime(DateTimeZone.UTC);
    }
}
