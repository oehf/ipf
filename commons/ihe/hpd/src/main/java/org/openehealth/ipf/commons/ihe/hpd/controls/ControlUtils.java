/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.controls;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.NotImplementedException;
import org.openehealth.ipf.commons.ihe.hpd.controls.strategies.ControlStrategy;
import org.openehealth.ipf.commons.ihe.hpd.controls.strategies.PaginationControlStrategy;
import org.openehealth.ipf.commons.ihe.hpd.controls.strategies.SortControlStrategy;
import org.openehealth.ipf.commons.ihe.hpd.controls.strategies.SortResponseControlStrategy;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.Control;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.DsmlMessage;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchResponse;

import javax.naming.ldap.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Methods for mapping of Controls to and from DSMLv2 and Strings.
 *
 * @author Dmytro Rud
 * @since 4.3
 */
@UtilityClass
public class ControlUtils {

    /**
     * Map from control OIDs to control strategies.  Applications can extend this map.
     */
    private static final Map<String, ControlStrategy> STRATEGIES = new HashMap<>();

    static {
        STRATEGIES.put(PagedResultsControl.OID, new PaginationControlStrategy());
        STRATEGIES.put(SortControl.OID, new SortControlStrategy());
        STRATEGIES.put(SortResponseControl.OID, new SortResponseControlStrategy());
    }

    public static Map<String, ControlStrategy> getStrategies() {
        return STRATEGIES;
    }

    public static <T extends BasicControl> T extractControl(byte[] berBytes, String type, boolean criticality) throws IOException {
        ControlStrategy strategy = STRATEGIES.get(type);
        if (strategy != null) {
            return (T) strategy.deserializeDsml2(berBytes, criticality);
        } else {
            throw new NotImplementedException("Cannot handle control type " + type);
        }
    }

    public static PagedResultsResponseControl convert(PagedResultsControl control) throws IOException {
        byte[] berBytes = control.getEncodedValue();
        return new PagedResultsResponseControl(PagedResultsResponseControl.OID, control.isCritical(), berBytes);
    }

    public static <T extends BasicControl> T extractControl(List<Control> controls, String type) throws IOException {
        for (Control control : controls) {
            if (type.equals(control.getType())) {
                return extractControl((byte[]) control.getControlValue(), type, control.isCriticality());
            }
        }
        return null;
    }

    public static <T extends BasicControl> T extractControl(SearchResponse searchResponse, String type) throws IOException {
        return ((searchResponse != null) && (searchResponse.getSearchResultDone() != null))
               ? extractControl(searchResponse.getSearchResultDone().getControl(), type)
               : null;
    }

    public static <T extends BasicControl> T extractControl(DsmlMessage dsmlMessage, String type) throws IOException {
        return (dsmlMessage != null)
               ? extractControl(dsmlMessage.getControl(), type)
               : null;
    }

    public static Control toDsmlv2(BasicControl bc) {
        Control control = new Control();
        control.setType(bc.getID());
        control.setCriticality(bc.isCritical());
        control.setControlValue(bc.getEncodedValue());
        return control;
    }

    public static void setControl(SearchResponse response, BasicControl bc) {
        setControl(response.getSearchResultDone(), bc);
    }

    public static void setControl(DsmlMessage dsmlMessage, BasicControl bc) {
        List<Control> controls = dsmlMessage.getControl();
        for (int i = 0; i < controls.size(); ++i) {
            if (bc.getID().equals(controls.get(i).getType())) {
                controls.set(i, toDsmlv2(bc));
                return;
            }
        }
        controls.add(toDsmlv2(bc));
    }

}
