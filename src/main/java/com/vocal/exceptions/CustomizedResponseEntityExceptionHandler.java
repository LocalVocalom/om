package com.vocal.exceptions;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vocal.viewmodel.ErrorDetailsDto;


@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDetailsDto errorDetails = new ErrorDetailsDto(new Date(), "Validation failed", ex.getBindingResult().toString() );
		return new ResponseEntity<Object>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleUsernameAlreadyExistsException(DataIntegrityViolationException dive,
			WebRequest request) {
		ErrorDetailsDto errorDetails = new ErrorDetailsDto(new Date(), dive.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintVoilationException(ConstraintViolationException cve, WebRequest request) {
		ErrorDetailsDto dto = new ErrorDetailsDto(new Date(), cve.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(RecordNotFoundException rnfe, WebRequest request) {
		ErrorDetailsDto errorDetails = new ErrorDetailsDto(new Date(), rnfe.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException iae, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(new Date(), iae.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UnsupportedEncodingException.class)
	public ResponseEntity<?> handleUnsupportedEncodingException(UnsupportedEncodingException uee, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(new Date(), "make sure you are using utf-8 encoding", request.getDescription(true));
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<?> handleUnauthorizedAccess(AuthorizationException aue, WebRequest request) {
		ErrorDetailsDto dto = new ErrorDetailsDto(new Date(), aue.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(NoContentAvailableException.class)
	public ResponseEntity<?> handleNoContentAvailableException(NoContentAvailableException aue, WebRequest request) {
		ErrorDetailsDto dto = new ErrorDetailsDto(new Date(), aue.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(CustomizedRuntimeException.class)
	public ResponseEntity<?> handleCustomizedRuntimeException(CustomizedRuntimeException cre, WebRequest request) {
		ErrorDetailsDto dto = new ErrorDetailsDto(new Date(), cre.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ParseException.class)
	public ResponseEntity<?> handleParsingExceptions(ParseException pex) {
		ErrorDetailsDto dto = new ErrorDetailsDto(new Date(), pex.getMessage(), pex.getCause().getMessage());
		return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(QuerySyntaxException.class) 
	public ResponseEntity<?> handleQuerySyntaxException(QuerySyntaxException qex){
		ErrorDetailsDto dto = new ErrorDetailsDto(new Date(), "Exception in query", "Check that the query is correct");
		return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ConditionViolationException.class)
	public ResponseEntity<?> handleConditionViolationException(ConditionViolationException cve) {
		ErrorDetailsDto dto = new ErrorDetailsDto(new Date(), cve.getMessage(), cve.getCause().getMessage());
		return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(StorageException.class)
	public ResponseEntity<?> handleStorageException(StorageException stEx) {
		ErrorDetailsDto dto = new ErrorDetailsDto(new Date(), stEx.getMessage(), stEx.getCause().getMessage());
		return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
	}
	
}