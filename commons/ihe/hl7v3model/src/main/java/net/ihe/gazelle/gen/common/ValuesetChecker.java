package net.ihe.gazelle.gen.common;

public interface ValuesetChecker {
	
	void setValueSetProvider(ValueSetProvider valueSetProvider);
	
	void setConceptProvider(ConceptProvider conceptProvider);

	Boolean matchesValueSet(String oidparam, String code, String codeSystem, String codeSystemName, String displayName);

	Boolean matchesCodeToValueSet(String oidparam, String code);
	
	Boolean matchesValueSetWithDisplayName(String oidparam, String code, String codeSystem, String codeSystemName, String displayName);

}