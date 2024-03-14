package net.ihe.gazelle.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerAdapter extends XmlAdapter<String, Integer>{

	@Override
	public String marshal(final Integer v) throws Exception {
		if (v != null) {
			return String.valueOf(v.intValue());
		}
		return null;
	}

	@Override
	public Integer unmarshal(final String v) throws Exception {
		if ((v == null) || (v.isEmpty())){
			return null;
		}
		try{
			return Integer.valueOf(v);
		}
		catch(NumberFormatException e){
			return null;
		}
	}

}
