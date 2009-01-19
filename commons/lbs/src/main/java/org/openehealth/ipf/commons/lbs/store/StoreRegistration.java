package org.openehealth.ipf.commons.lbs.store;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StoreRegistration {
    private static final Log log = LogFactory.getLog(StoreRegistration.class); 
        
    private static final Set<LargeBinaryStore> registeredStores =
        Collections.synchronizedSet(new HashSet<LargeBinaryStore>());

    public static void register(LargeBinaryStore store) {
        registeredStores.add(store);
        log.debug("Registered store: " + store);
    }
    
    public static void reset() {
        registeredStores.clear();
        log.debug("Reset store registration");
    }
    
    public static LargeBinaryStore getStore(URI resourceUri) {
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
