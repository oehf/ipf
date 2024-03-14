package net.ihe.gazelle.validation;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Assertion", propOrder = { "idScheme", "assertionId"})
public class Assertion {
	
	@XmlAttribute(name = "idScheme", required = true)
	protected String idScheme;
	
	@XmlAttribute(name = "assertionId", required = true)
	protected String assertionId;
	
	/**
	 * empty constructor for Assertion class.
	 */
	public Assertion() {
		// empty constructor for Assertion class
	}
	
	public Assertion(String idScheme, String assertionId) {
		super();
		this.idScheme = idScheme;
		this.assertionId = assertionId;
	}



	public String getIdScheme() {
		return idScheme;
	}

	public void setIdScheme(String idScheme) {
		this.idScheme = idScheme;
	}

	public String getAssertionId() {
		return assertionId;
	}

	public void setAssertionId(String assertionId) {
		this.assertionId = assertionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assertionId == null) ? 0 : assertionId.hashCode());
		result = prime * result + ((idScheme == null) ? 0 : idScheme.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Assertion other = (Assertion) obj;
		if (assertionId == null) {
			if (other.assertionId != null)
				return false;
		} else if (!assertionId.equals(other.assertionId))
			return false;
		if (idScheme == null) {
			if (other.idScheme != null)
				return false;
		} else if (!idScheme.equals(other.idScheme))
			return false;
		return true;
	}
	
	

}
