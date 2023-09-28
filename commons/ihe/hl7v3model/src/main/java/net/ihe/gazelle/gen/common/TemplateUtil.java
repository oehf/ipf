package net.ihe.gazelle.gen.common;

import java.util.ArrayList;
import java.util.List;

import net.ihe.gazelle.com.templates.Template;
import net.ihe.gazelle.com.templates.TemplateId;

public final class TemplateUtil {
	
	private TemplateUtil() { }
	
	public static List<String> extractListTemplateId(Template parentTemplate) {
		List<String> listParentId = new ArrayList<>();
		if (parentTemplate != null) {
			for (TemplateId tid : parentTemplate.getTemplateId()) {
				listParentId.add(tid.getId());
			}
		}
		return listParentId;
	}
	
	public static List<Template> extractListTemplateByOID(Template parentTemplate, String oid) {
		List<Template> res = new ArrayList<>();
		if (parentTemplate != null) {
			fulfillListTemplateByOID(parentTemplate, oid, res);
		}
		return res;
	}
	
	public static boolean templateContainsSubTemplate(Template parentTemplate, String subtemp) {
		boolean res = false;
		if (parentTemplate != null && subtemp != null) {
			for (Template temp : parentTemplate.getTemplate()) {
				if (checkIfTemplateContainsOID(temp, subtemp)) {
					return true;
				}
			}
		}
		return res;
	}
	
	public static boolean checkIfTemplateContainsOID(Template temp, String oid) {
		if (temp != null) {
			for (TemplateId tid : temp.getTemplateId()) {
				if (tid.getId() != null && tid.getId().equals(oid)) {
					return true;
				}
			}
		}
		return false;
	}

	private static void fulfillListTemplateByOID(Template parentTemplate, String oid, List<Template> res) {
		if (parentTemplate != null) {
			for (TemplateId tid : parentTemplate.getTemplateId()) {
				if (tid.getId() != null && tid.getId().equals(oid)) {
					res.add(parentTemplate);
				}
			}
			for (Template template : parentTemplate.getTemplate()) {
				fulfillListTemplateByOID(template, oid, res);
			}
		}
	}

}
