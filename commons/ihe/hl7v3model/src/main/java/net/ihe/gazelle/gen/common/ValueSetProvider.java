package net.ihe.gazelle.gen.common;

import java.util.List;

public interface ValueSetProvider {
	
	List<Concept> getConceptsListFromValueSet(String valueSetId, String lang);
	
	List<Concept> getConceptsListFromValueSet(String valueSetId, String version, String lang);
	
}
