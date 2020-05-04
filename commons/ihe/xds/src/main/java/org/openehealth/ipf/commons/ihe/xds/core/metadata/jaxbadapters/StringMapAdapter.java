/*
 * Copyright 2014 the original author or authors.
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class StringMapAdapter extends XmlAdapter<StringMap, Map<String, List<String>>> {

    @Override
    public StringMap marshal(Map<String, List<String>> v) throws Exception {
        if (v == null) {
            return null;
        }

        var result = new StringMap();
        result.entries = new ArrayList<>();
        for (var entry : v.entrySet()) {
            var extra = new StringMapEntry();
            extra.setKey(entry.getKey());
            extra.setValues(new ArrayList<>());
            extra.getValues().addAll(entry.getValue());
            result.entries.add(extra);
        }

        return result;
    }


    @Override
    public Map<String, List<String>> unmarshal(StringMap v) throws Exception {
        if ((v == null) || (v.entries == null)) {
            return null;
        }

        var result = new HashMap<String, List<String>>();
        for (var extra : v.entries) {
            result.put(extra.getKey(), extra.getValues());
        }
        return result;
    }

}
