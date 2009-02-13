package org.openehealth.ipf.commons.lbs.store;

import static org.apache.commons.lang.Validate.notNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Registry for all stores
 * <p>
 * This component keeps track of all stores within the current system and provides
 * lookup of stores via their URI.
 * The URI schemes used by the stores should guarantee that the URI of different 
 * stores are unique. E.g. the disk store can be configured to do this by using
 * a unique base URI for each store. 
 * @author Jens Riemschneider
 */
public class StoreRegistration {
    private static final Log log = LogFactory.getLog(StoreRegistration.class); 
        
    private static final Set<LargeBinaryStore> registeredStores =
        Collections.synchronizedSet(new HashSet<LargeBinaryStore>());

    /**
     * Registers the given store
     * @param store
     *          the store to register
     */
    public static void register(LargeBinaryStore store) {
        notNull(store, "store cannot be null");
        registeredStores.add(store);
        log.debug("Registered store: " + store);
    }
    
    /**
     * Resets the registry
     * <p>
     * This is used in unit tests to perform clean-up
     */
    public static void reset() {
        registeredStores.clear();
        log.debug("Reset store registration");
    }
    
    /**
     * Retrieves the store that contains the given URI 
     * @param resourceUri
     *          the URI to look for
     * @return the store that contained the URI
     * @throws IllegalArgumentException  if the URI was not found in any of the stores
     * @throws AssertionError  if the URI was found in multiple stores
     */
    public static LargeBinaryStore getStore(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        List<LargeBinaryStore> matchingStores = getMatchingStores(resourceUri);
        
        if (matchingStores.size() == 0) {
            log.warn("resource URI not found in any registered store: " + resourceUri);
            throw new IllegalArgumentException("resource URI not found in any registered store: " + resourceUri);
        }
        
        if (matchingStores.size() > 1) {
            log.fatal("resource Uri was found in multiple stores. resourceUri=" + resourceUri + ", stores=" + matchingStores);
            throw new AssertionError("resource Uri was found in multiple stores. resourceUri=" + resourceUri + ", stores=" + matchingStores);
        }
        
        return matchingStores.get(0);
    }

    private static List<LargeBinaryStore> getMatchingStores(URI resourceUri) {
        synchronized(registeredStores) {
            List<LargeBinaryStore> matchingStores = new ArrayList<LargeBinaryStore>();
            for (LargeBinaryStore store : registeredStores) {
                if (store.contains(resourceUri)) {
                    matchingStores.add(store);
                }
            }
            return matchingStores;
        }
    }
}
