package com.abo.demo.usersecure;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
@Component
public class NewUserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		 return SignUpForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
		         "username", "required.username","Field name is required.");
		   }
	}


