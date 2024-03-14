package net.ihe.gazelle.gen.common;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Concept implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4518318071899080800L;

	private String code;
	private String displayName;
	private String codeSystem;
	private String codeSystemName;

	public Concept() {
		// to provide empty constructor
	}

	public Concept(String inCode, String inDisplayName, String inCodeSystem, String inCodeSystemName) {
		this.code = inCode;
		this.displayName = inDisplayName;
		this.codeSystem = inCodeSystem;
		this.codeSystemName = inCodeSystemName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getCodeSystemName() {
		return codeSystemName;
	}

	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((codeSystem == null) ? 0 : codeSystem.hashCode());
		result = prime * result + ((codeSystemName == null) ? 0 : codeSystemName.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Concept other = (Concept) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		if (codeSystem == null) {
			if (other.codeSystem != null) {
				return false;
			}
		} else if (!codeSystem.equals(other.codeSystem)) {
			return false;
		}
		if (codeSystemName == null) {
			if (other.codeSystemName != null) {
				return false;
			}
		} else if (!codeSystemName.equals(other.codeSystemName)) {
			return false;
		}
		if (displayName == null) {
			if (other.displayName != null) {
				return false;
			}
		} else if (!displayName.equals(other.displayName)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Concept [code=" + code + ", displayName=" + displayName + ", codeSystem=" + codeSystem
				+ ", codeSystemName=" + codeSystemName + "]";
	}

	public static Concept inverseString(String cc) {
		Pattern p = Pattern
				.compile("Concept \\[code=(.*?), displayName=(.*?), codeSystem=(.*?), codeSystemName=(.*?)\\]");
		Matcher m = p.matcher(cc);
		while (m.find()) {
			Concept cons = new Concept();
			cons.code = m.group(1);
			cons.displayName = m.group(2);
			cons.codeSystem = m.group(3);
			cons.codeSystemName = m.group(4);
			return cons;
		}
		return null;
	}

}
