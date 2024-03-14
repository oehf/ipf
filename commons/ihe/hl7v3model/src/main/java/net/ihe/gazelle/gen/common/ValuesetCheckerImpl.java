package net.ihe.gazelle.gen.common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ValuesetCheckerImpl implements ValuesetChecker {

	private static ValueSetProvider valueSetProvider;
	
	private static ConceptProvider conceptProvider;
	
	/**
	 * @return the conceptProvider
	 */
	public ConceptProvider getConceptProvider() {
		return conceptProvider;
	}

	/**
	 * @param conceptProvider the conceptProvider to set
	 */
	public void setConceptProvider(ConceptProvider conceptProvider) {
		ValuesetCheckerImpl.conceptProvider = conceptProvider;
	}

	public ValueSetProvider getValueSetProvider() {
		return valueSetProvider;
	}

	public void setValueSetProvider(ValueSetProvider valueSetProvider) {
		ValuesetCheckerImpl.valueSetProvider = valueSetProvider;
	}
	
	@Override
	public Boolean matchesValueSet(String oidparam, String code, String codeSystem, String codeSystemName, String displayName){
		String oid = this.extractOID(oidparam);
		String version = this.extractVersion(oidparam);
		String lang = this.extractLang(oidparam);
		Concept conc = new Concept(code, displayName, codeSystem, codeSystemName);
		return matchesValueSetByValueSetProvider(oid, version, lang, conc) || matchesValueSetByConceptProvider(oid, version, lang, conc);
	}

	private boolean matchesValueSetByValueSetProvider(String oid, String version, String lang, Concept conc) {
		if (getValueSetProvider() != null) {
			List<Concept> lconc = getValueSetProvider().getConceptsListFromValueSet(oid, version, lang);
			if ((lconc != null) && (!lconc.isEmpty())){
				for (Concept concept : lconc) {
					if (this.twoConceptAreEquals(concept,conc)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean matchesValueSetByConceptProvider(String oid, String version, String lang, Concept conc) {
		if (getConceptProvider() != null) {
			List<Concept> lconc = getConceptProvider().getConceptFromValueSet(oid, conc.getCode(), conc.getCodeSystem(), lang, version);
			if ((lconc != null) && (!lconc.isEmpty())){
				for (Concept concept : lconc) {
					if (this.twoConceptAreEquals(concept,conc)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public Boolean matchesCodeToValueSet(String oidparam, String code){
		String oid = this.extractOID(oidparam);
		String version = this.extractVersion(oidparam);
		String lang = this.extractLang(oidparam);
		return matchesCodeToValueSetByConceptProvider(code, oid, version, lang) || matchesCodeToValueSetByValueSetProvider(code, oid, version, lang);
	}
	
	private boolean matchesCodeToValueSetByConceptProvider(String code, String oid, String version, String lang) {
		if (getConceptProvider() != null) {
			List<Concept> lconc = getConceptProvider().getConceptFromValueSet(oid, code, null, lang, version);
			if ((lconc != null) && (!lconc.isEmpty())){
				for (Concept concept : lconc) {
					if (code.equals(concept.getCode())){
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean matchesCodeToValueSetByValueSetProvider(String code, String oid, String version, String lang) {
		if (getValueSetProvider() != null) {
			List<Concept> lconc = getValueSetProvider().getConceptsListFromValueSet(oid, version, lang);
			if ((lconc != null) && (!lconc.isEmpty())){
				for (Concept concept : lconc) {
					if (code.equals(concept.getCode())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public Boolean matchesValueSetWithDisplayName(String oidparam, String code, String codeSystem, String codeSystemName, String displayName){
		String oid = this.extractOID(oidparam);
		String version = this.extractVersion(oidparam);
		String lang = this.extractLang(oidparam);
		Concept conc = new Concept(code, displayName, codeSystem, codeSystemName);
		return matchesVSWithDNByConceptProvider(oid, version, lang, conc) || matchesVSWithDNByValueSetProvider(oid, version, lang, conc);
	}
	
	public Boolean matchesVSWithDNByConceptProvider(String oid, String version, String lang, Concept conc) {
		if (getConceptProvider() != null) {
			List<Concept> lconc = getConceptProvider().getConceptFromValueSet(oid, conc.getCode(), conc.getCodeSystem(), lang, version);
			if ((lconc != null) && (!lconc.isEmpty())){
				for (Concept concept : lconc) {
					if (this.twoConceptAreEqualsWithDisplayName(concept,conc)){
						return true;
					}
				}
			}
		}
		return false;
	}

	public Boolean matchesVSWithDNByValueSetProvider(String oid, String version, String lang, Concept conc) {
		if (getValueSetProvider() != null) {
			List<Concept> lconc = getValueSetProvider().getConceptsListFromValueSet(oid, version, lang);
			if ((lconc != null) && (!lconc.isEmpty())){
				for (Concept concept : lconc) {
					if (this.twoConceptAreEqualsWithDisplayName(concept,conc)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private String extractOID(String oidparam) {
		String[] ss = oidparam.split("&");
		return ss[0];
	}

	private String extractVersion(String oidparam) {
		String[] ss = oidparam.split("&");
		String version = null;
		if (ss.length>0) {
			for (int i=1; i<ss.length; i++) {
				version = ss[i].contains("version=")?ss[i].substring(8):version;
			}
		}
		return version;
	}
	
	private String extractLang(String oidparam) {
		String[] ss = oidparam.split("&");
		String lang = null;
		if (ss.length>0) {
			for (int i=1; i<ss.length; i++) {
				lang = ss[i].contains("lang=")?ss[i].substring(5):lang;
			}
		}
		return lang;
	}
	
	protected boolean twoConceptAreEqualsWithDisplayName(Concept conc1, Concept conc2){
		return twoConceptAreEquals(conc1, conc2) && 
				StringUtils.equals(conc1.getCodeSystemName(), conc2.getCodeSystemName()) &&  
				StringUtils.equals(conc1.getDisplayName(), conc2.getDisplayName());
	}
	
	protected boolean twoConceptAreEquals(Concept conc1, Concept conc2){
		return conc1 != null && conc2 != null && 
				StringUtils.equals(conc1.getCode(), conc2.getCode()) &&
				StringUtils.equals(conc1.getCodeSystem(), conc2.getCodeSystem());
	}

}
