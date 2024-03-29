//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.01 at 03:58:26 PM CET 
//

package net.ihe.gazelle.validation;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element ref="{}Warning" minOccurs="0"/&gt;
 *           &lt;element ref="{}Error" minOccurs="0"/&gt;
 *           &lt;element ref="{}Note" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref="{}Result"/&gt;
 *         &lt;element ref="{}ValidationCounters"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "warningOrErrorOrNote", "result",
		"validationCounters" })
@XmlRootElement(name = "MDAValidation")
public class MDAValidation {

	@XmlElements({ @XmlElement(name = "Warning", type = Warning.class),
			@XmlElement(name = "Note", type = Note.class),
			@XmlElement(name = "Info", type = Info.class),
			@XmlElement(name = "Error", type = Error.class) })
	protected List<Object> warningOrErrorOrNote;
	@XmlElement(name = "Result", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "NCName")
	protected String result;
	@XmlElement(name = "ValidationCounters", required = true)
	protected ValidationCounters validationCounters;

	/**
	 * Gets the value of the warningOrErrorOrNote property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the warningOrErrorOrNote property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getWarningOrErrorOrNote().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Warning }
	 * {@link Note } {@link Error }
	 * 
	 * 
	 */
	public List<Object> getWarningOrErrorOrNote() {
		if (warningOrErrorOrNote == null) {
			warningOrErrorOrNote = new ArrayList<>();
		}
		return this.warningOrErrorOrNote;
	}

	/**
	 * Gets the value of the result property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Sets the value of the result property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setResult(String value) {
		this.result = value;
	}

	/**
	 * Gets the value of the validationCounters property.
	 * 
	 * @return possible object is {@link ValidationCounters }
	 * 
	 */
	public ValidationCounters getValidationCounters() {
		return validationCounters;
	}

	/**
	 * Sets the value of the validationCounters property.
	 * 
	 * @param value
	 *            allowed object is {@link ValidationCounters }
	 * 
	 */
	public void setValidationCounters(ValidationCounters value) {
		this.validationCounters = value;
	}

}
