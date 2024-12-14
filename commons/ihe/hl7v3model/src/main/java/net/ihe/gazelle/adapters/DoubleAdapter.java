package net.ihe.gazelle.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * The Class DoubleAdapter.
 */
public class DoubleAdapter extends XmlAdapter<String, Double>{

	/**
	 * Marshal a double.
	 *
	 * @param v the double to be marshalled
	 * @return the marshaling
     */
	@Override
	public String marshal(final Double v) {
		if ((v != null) && !Double.isInfinite(v) && !Double.isNaN(v)) {
			return String.valueOf(v.doubleValue());
		}
		return null;
	}

	/**
	 * Unmarshal a string to a double.
	 *
	 * @param v the String to be unmarshalled.
	 * @return the double, result of unmarshalling.
     */
	@Override
	public Double unmarshal(final String v) {
		if ((v == null) || (v.isEmpty())){
			return null;
		}
		try{
			return Double.valueOf(v);
		} catch(NumberFormatException e){
			return null;
		}
	}

}
