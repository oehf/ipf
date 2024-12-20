package net.ihe.gazelle.gen.common;

import java.util.List;

public interface ConceptProvider {
	
	List<Concept> getConceptFromValueSet(String valueSetId, String conceptCode, String conceptCodeSystem, String lang, String version);
	
}
