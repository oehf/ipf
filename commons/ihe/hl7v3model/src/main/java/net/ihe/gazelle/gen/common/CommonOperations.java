package net.ihe.gazelle.gen.common;

import org.apache.commons.lang3.StringUtils;

import jakarta.xml.bind.annotation.XmlTransient;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlTransient
public class CommonOperations {
	
	private static ValuesetChecker valuesetChecker;

	/**
	 * @return the valuesetChecker
	 */
	public static ValuesetChecker getValuesetChecker() {
		if (valuesetChecker == null) {
			valuesetChecker = new ValuesetCheckerImpl();
		}
		return valuesetChecker;
	}
	
	public static void setValueSetProvider(ValueSetProvider valueSetProvider) {
		getValuesetChecker().setValueSetProvider(valueSetProvider);
	}

	/**
	 * @param valuesetChecker the valuesetChecker to set
	 */
	public static void setValuesetChecker(ValuesetChecker valuesetChecker) {
		CommonOperations.valuesetChecker = valuesetChecker;
	}

    public Boolean matches(double value, String regex) {
        return matches(Double.toString(value), regex);
    }

    public Boolean matches(int value, String regex) {
        return matches(Integer.toString(value), regex);
    }

	public Boolean matches(String value, String regex){
		return value.matches(regex);
	}

	public Boolean matches(String value, String regex, boolean dotAll){
		if (dotAll){
			Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
			Matcher matcher = pattern.matcher(value);
			return matcher.matches();
		}else{
			return matches(value, regex);
		}
	}

	public Boolean isOID(String value){
		if (value == null) {
			return false;
		}
		String reg = "^(0|[1-9]\\d*)\\.((0|[1-9]\\d*)\\.)*(0|[1-9]\\d*)$";
		boolean res = this.matches(value, reg);
		res = res && (value.length()<65);
		return res;
	}

	public Boolean isUUID(String value){
		if (value == null) {
			return false;
		}
		String reg = "^(urn:uuid:)?[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}$";
		return value.matches(reg);
	}

	public Boolean isRUID(String value){
		if (value == null) {
			return false;
		}
		String reg = "^[A-Za-z][A-Za-z0-9\\-]*$";
		return value.matches(reg);
	}

	public Boolean isUID(String value){
		return  this.isOID(value) || this.isUUID(value) || this.isRUID(value);
	}

	public Boolean isCSType(String value){
		if (value == null) {
			return false;
		}
		String reg = "[^\\s]+";
		return value.matches(reg);
	}

	public Boolean isSTType(String value){
		if (value == null) {
			return false;
		}
		String reg = ".+";
		return value.matches(reg);
	}

	public Boolean isTSType(String value){
		if (value == null) {
			return false;
		}
		String reg = "[0-9]{1,8}|([0-9]{9,14}|[0-9]{14,14}\\.[0-9]+)([+\\-][0-9]{1,4})?";
		return value.matches(reg);
	}

	public Boolean isBIN(String value){
		if (value == null) {
			return false;
		}
		String reg = "[A-Za-z0-9\\+/=]";
		return value.matches(reg);
	}
	public Boolean matchesValueSet(String oidparam, String code, String codeSystem, String codeSystemName, String displayName){
		return this.getValuesetChecker().matchesValueSet(oidparam, code, codeSystem, codeSystemName, displayName);
	}

	public Boolean isXCN(String value){
		if (value == null) {
			return false;
		}
		String reg = "^([^\\^]*?)(\\^(([^\\^]*?)(\\^(([^\\^]*?)(\\^(([^\\^]*?)(\\^(([^\\^]*?)(\\^(([^\\^]*?)(\\^(\\^(\\^(&([^\\^]*?)&ISO((\\^[^\\^]*?){0,14}))?)?)?)?)?)?)?)?)?)?)?)?)?)?$";
		boolean res = value.matches(reg);
		Pattern pp = Pattern.compile(reg);
		Matcher mm = pp.matcher(value);
		if (mm.find()){
			String oo = mm.group(21);
			if (!StringUtils.isEmpty(oo))
				res = res && this.isOID(oo);

			String id = mm.group(1);
			String name = mm.group(4);
			String fname = mm.group(7);
			res = res && oneOfIdNameFnameIsNotEmpty(id, name, fname);
		}else{
			res = false;
		}
		res = res && (value.length()<257);
		return res;
	}

	private boolean oneOfIdNameFnameIsNotEmpty(String id, String name, String fname) {
		return !StringUtils.isEmpty(id) ||
		!StringUtils.isEmpty(name) ||
		!StringUtils.isEmpty(fname);
	}
	
	public Boolean isXPN(String value){
		if (value == null){
			return false;
		}else{
			String regex = "[^\\^]*(\\^[^\\^]*){1,13}";
			return value.matches(regex);
		}
	}

	public Boolean isXON(String value){
		if (value == null) {
			return false;
		}
		String reg = "^([^\\^]+?)(\\^(\\^(\\^(\\^(\\^((&([^\\^]*?)(&ISO))?(\\^(\\^(\\^(\\^([^\\^]*))?)?)?)?)?)?)?)?)?)?$";
		boolean res = value.matches(reg);
		Pattern pp = Pattern.compile(reg);
		Matcher mm = pp.matcher(value);
		if (mm.find()){
			String oi = mm.group(15);
			String aa = mm.group(9);
			String aat = mm.group(10);
			if ((oi != null) && (!"".equals(oi)) && !this.isOID(oi)){
				res = res && (!"".equals(aa)) && (!"".equals(aat));
			}
		}
		return res;
	}

	public Boolean isDTM(String value){
		if (value == null) {
			return false;
		}
		String reg = "^((19|20)\\d\\d)((0[1-9]|1[012])((0[1-9]|[12][0-9]|3[01])((0[0-9]|1[0-9]|2[0123])(([0-5][0-9])([0-5][0-9])?)?)?)?)?$";
		return value.matches(reg);
	}

	public Boolean matchesValueSetWithDisplayName(String oidparam, String code, String codeSystem, String codeSystemName, String displayName){
		return this.getValuesetChecker().matchesValueSetWithDisplayName(oidparam, code, codeSystem, codeSystemName, displayName);
	}
	
	public Boolean matchesCodeToValueSet(String oidparam, String code){
		return this.getValuesetChecker().matchesCodeToValueSet(oidparam, code);
	}

}
