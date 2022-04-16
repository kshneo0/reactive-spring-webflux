package com.reactivespring.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.reactivespring.exception.MoviesInfoClientException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler {
	
	@ExceptionHandler(MoviesInfoClientException.class)
	public ResponseEntity<String> handleClientException(MoviesInfoClientException exception){
		log.error("Exception caught in handleClientException :  {} " ,exception.getMessage(),  exception);
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
	}
}
