package net.ihe.gazelle.gen.common;

import java.util.List;

public interface ValueSetProvider {
	
	public List<Concept> getConceptsListFromValueSet(String valueSetId, String lang);
	
	public List<Concept> getConceptsListFromValueSet(String valueSetId, String version, String lang);
	
}
