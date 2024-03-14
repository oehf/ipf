package net.ihe.gazelle.gen.common;

public interface ValuesetChecker {
	
	public void setValueSetProvider(ValueSetProvider valueSetProvider);
	
	public void setConceptProvider(ConceptProvider conceptProvider);

	public Boolean matchesValueSet(String oidparam, String code, String codeSystem, String codeSystemName, String displayName);

	public Boolean matchesCodeToValueSet(String oidparam, String code);
	
	public Boolean matchesValueSetWithDisplayName(String oidparam, String code, String codeSystem, String codeSystemName, String displayName);

}