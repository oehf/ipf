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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public class DateTimeAdapter extends XmlAdapter<Calendar, ZonedDateTime> {
    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
    
    @Override
    public ZonedDateTime unmarshal(Calendar calendar) {
        return Optional.ofNullable(calendar)
                .map(Calendar::toInstant)
                .map(i -> ZonedDateTime.ofInstant(i, UTC_ZONE_ID))
                .orElse(null);
    }

    @Override
    public Calendar marshal(ZonedDateTime zonedDateTime) {
        return Optional.ofNullable(zonedDateTime)
                .map(GregorianCalendar::from)
                .orElse(null);
    }

}
