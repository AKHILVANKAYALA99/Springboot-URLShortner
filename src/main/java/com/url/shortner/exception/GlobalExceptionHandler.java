package com.url.shortner.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<String, Object>();
		body.put("timestamp", Instant.now());
		body.put("status", status.value());
		
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
		
		body.put("errors", errors);
		return new ResponseEntity<Object>(body, status);
	}
	
	@ExceptionHandler(value = {URLValidationException.class})
	public ResponseEntity<Object> invalidUserInput(URLValidationException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<String, Object>();
		body.put("timestamp", Instant.now());
		body.put("status", HttpStatus.BAD_REQUEST);
		body.put("message", ex.getMessage());
		body.put("errors", ex.getErrors());
		return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {URLRedirectionException.class})
	public ModelAndView redirectException(URLRedirectionException ex, WebRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("error", ex.getMessage());
		mv.setViewName("redirectError");
		return mv;
	}

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> applicationException(Exception ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<String, Object>();
		body.put("timestamp", Instant.now());
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
		body.put("message", "Internal Server Error. Please try again after sometime.");
		return new ResponseEntity<Object>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
