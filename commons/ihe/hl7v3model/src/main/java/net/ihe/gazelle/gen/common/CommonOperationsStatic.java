package net.ihe.gazelle.gen.common;

import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class CommonOperationsStatic {
	
	private static CommonOperations commonOperations = new CommonOperations();
	
	private CommonOperationsStatic() {}

    public static Boolean matches(double value, String regex) {
        return commonOperations.matches(value, regex);
    }

    public static Boolean matches(int value, String regex) {
        return commonOperations.matches(value, regex);
    }

    public static Boolean matches(String value, String regex) {
		return commonOperations.matches(value, regex);
	}

	public static Boolean matches(String value, String regex, boolean dotAll){
		return commonOperations.matches(value, regex, dotAll);
	}

	public static Boolean isOID(String value){
		return commonOperations.isOID(value);
	}

	public static Boolean isUUID(String value){
		return commonOperations.isUUID(value);
	}

	public static Boolean isRUID(String value){
		return commonOperations.isRUID(value);
	}

	public static Boolean isUID(String value){
		return commonOperations.isUID(value);
	}

	public static Boolean isCSType(String value){
		return commonOperations.isCSType(value);
	}

	public static Boolean isSTType(String value){
		return commonOperations.isSTType(value);
	}

	public static Boolean isTSType(String value){
		return commonOperations.isTSType(value);
	}

	public static Boolean isBIN(String value){
		return commonOperations.isBIN(value);
	}
	public static Boolean matchesValueSet(String oid, String code, String codeSystem, String codeSystemName, String displayName){
		return commonOperations.matchesValueSet(oid, code, codeSystem, codeSystemName, displayName);
	}

	public static Boolean isXCN(String value){
		return commonOperations.isXCN(value);
	}

	public static Boolean isXPN(String value){
		return commonOperations.isXPN(value);
	}

	public static Boolean isXON(String value){
		return commonOperations.isXON(value);
	}

	public static Boolean isDTM(String value){
		return commonOperations.isDTM(value);
	}

	public static Boolean matchesValueSetWithDisplayName(String oid, String code, String codeSystem, String codeSystemName, String displayName){
		return commonOperations.matchesValueSetWithDisplayName(oid, code, codeSystem, codeSystemName, displayName);
	}

	public static Boolean matchesCodeToValueSet(String oid, String code){
		return commonOperations.matchesCodeToValueSet(oid, code);
	}
	
	public static List<String> extractListLitteral(Class<?> cl){
		List<String> res = new ArrayList<String>();
		if (cl != null){
			List<?> aa = Arrays.asList(cl.getEnumConstants());
			for (Object object : aa) {
				System.out.println(object.getClass().getSimpleName());
				try {
					Field field = object.getClass().getDeclaredField("value");
					field.setAccessible(true);
					String value = (String) field.get(object);
					res.add(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
	
	public static Boolean validateByXPATHV2(Object obj, String xpath){
		Boolean res = false;
		if (obj == null) return null;
		try {
			Method method = obj.getClass().getDeclaredMethod("get_xmlNodePresentation");
			org.w3c.dom.Node _xmlNode = (Node) method.invoke(obj);
			res = net.ihe.gazelle.gen.common.XpathUtils.evaluateByNode(_xmlNode, xpath);
		} catch (Exception e) {
			res = false;
		}
		return res;
	}
	
	public static String stringValueOf(Object obj) {
		return extractLitteralValue(obj);
	}
	
	public static String extractLitteralValue(Object object){
		String res = null;
		try {
			Field field = object.getClass().getDeclaredField("value");
			field.setAccessible(true);
			res = (String) field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static Integer extractNumberDigits(Double number){
		if (number != null){
			String db = String.valueOf(number);
			if (db.indexOf('.')>=0){
				return db.substring(db.indexOf('.')).length()-1;
			}
		}
		return 0;
	}
	
	public static String extractFractionDigits(Double number){
		if (number != null){
			String db = String.valueOf(number);
			if (db.indexOf('.')>=0){
				return db.substring(db.indexOf('.')+1);
			}
		}
		return "";
	}
	
	public static Boolean isLowerOrEqual(Double dbl, String str){
		try{
			Double comp = Double.valueOf(str);
			return dbl <= comp;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public static Boolean isGreaterOrEqual(Double dbl, String str){
		try{
			Double comp = Double.valueOf(str);
			return dbl >= comp;
		}
		catch(Exception e){
			return false;
		}
	}

	public static Boolean compareExactType(Object obj, String type) {
		if (obj != null && type != null) {
			String objClassName = obj.getClass().getSimpleName();
			return objClassName.equals(type);
		} else {
			return false;
		}
	}

}
