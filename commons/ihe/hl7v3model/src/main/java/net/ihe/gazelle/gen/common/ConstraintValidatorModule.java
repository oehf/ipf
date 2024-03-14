package net.ihe.gazelle.gen.common;

import java.util.List;

import net.ihe.gazelle.validation.Notification;

public interface ConstraintValidatorModule {
	
	void validate(Object obj, String location, List<Notification> lnotif);

}
