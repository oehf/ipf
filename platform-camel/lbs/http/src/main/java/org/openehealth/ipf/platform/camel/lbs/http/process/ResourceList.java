package org.openehealth.ipf.platform.camel.lbs.http.process;

import java.util.ArrayList;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;

/**
 * List of large binary resources
 * <p>
 * Stored via a list of {@link ResourceDataSource} 
 * @author Jens Riemschneider
 */
public class ResourceList extends ArrayList<ResourceDataSource> {
    /** Serialization UID */
    private static final long serialVersionUID = 4830631651672370092L;
}
