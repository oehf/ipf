package org.openehealth.ipf.platform.camel.lbs.http.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

    /**
     * Default constructor.
     */
    public ResourceList() {
        super();
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param org  
     *          the collection whose elements are to be placed into this list.
     */
    public ResourceList(Collection<? extends ResourceDataSource> org) {
        super(org);
    }
    
    /**
     * Constructs a list containing the given elements.
     * @param elements
     *          the initial elements of this list.
     */
    public ResourceList(ResourceDataSource... elements) {
        super(Arrays.asList(elements));
    }
}
