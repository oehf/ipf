package net.ihe.gazelle.com.templates;

public enum ValidationResultKind {
	
	ERROR("Error"), REPORT("Report"), WARNING("Warning"), MISSING("Missing");
	
	private String kind;
	
	private ValidationResultKind(String kind) {
		this.kind = kind;
	}
	
	public String getKind() {
		return kind;
	}

}
