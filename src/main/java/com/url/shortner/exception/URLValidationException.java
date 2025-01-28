package com.url.shortner.exception;

import java.util.ArrayList;
import java.util.List;

public class URLValidationException extends Exception {
	private List<String> errors;
	
	

	public URLValidationException(String message, List<String> errors) {
		super(message);
		if(null == this.errors) {
			this.errors = new ArrayList<String>();
		}
		this.errors.addAll(errors);
	}

	public URLValidationException(String string) {
		super(string);
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
