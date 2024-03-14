package net.ihe.gazelle.gen.common;

import net.ihe.gazelle.validation.Error;
import net.ihe.gazelle.validation.Info;
import net.ihe.gazelle.validation.Note;
import net.ihe.gazelle.validation.Warning;

public class NotificationMan {
	
	private NotificationMan(){}
	
	public static Error createError(String errorName, String errorDescription, String errorIdentifier){
		Error error = new Error();
		error.setTest(errorName);
		error.setDescription(errorDescription);
		error.setIdentifiant(errorIdentifier);
		return error;
	}
	
	public static Note createNote(String noteName, String noteDescription, String noteIdentifier){
		Note note = new Note();
		note.setTest(noteName);
		note.setDescription(noteDescription);
		note.setIdentifiant(noteIdentifier);
		return note;
	}
	
	public static Warning createWarning(String warningName, String warningDescription, String warningIdentifier){
		Warning warning = new Warning();
		warning.setTest(warningName);
		warning.setDescription(warningDescription);
		warning.setIdentifiant(warningIdentifier);
		return warning;
	}
	
	public static Info createInfo(String infoName, String infoDescription, String infoIdentifier){
		Info info = new Info();
		info.setTest(infoName);
		info.setDescription(infoDescription);
		info.setIdentifiant(infoIdentifier);
		return info;
	}

}
