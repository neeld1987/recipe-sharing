package com.example.recipesharing.controller;

import com.example.recipesharing.exception.AuthenticationFailureException;
import com.example.recipesharing.exception.EntityAlreadyExistException;
import com.example.recipesharing.exception.EntityNotFoundException;
import com.example.recipesharing.exception.apierror.RecipeSharingApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
@RequestMapping(produces = "application/json")
public class RecipeSharingExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> assertionException(final IllegalArgumentException e) {
        return error(e, HttpStatus.NOT_ACCEPTABLE, e.getLocalizedMessage());
    }

    private ResponseEntity<Object> error(
            final Exception exception, final HttpStatus httpStatus, final String logRef) {
        final String message =
                Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(message);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolation(ConstraintViolationException constraintViolationException) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : constraintViolationException.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(constraintViolationException.getMessage());
        apiError.setErrors(errors);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleReservationDoesNotExistException
            (EntityNotFoundException entityNotFoundException) {
        List<String> errors = new ArrayList<String>();
        errors.add("No such element exist");

        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(entityNotFoundException.getMessage());
        apiError.setErrors(errors);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity handleEntityAlreadyExistException
            (EntityAlreadyExistException entityAlreadyExistException) {
        List<String> errors = new ArrayList<String>();
        errors.add("Element already exist");

        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.CONFLICT);
        apiError.setMessage(entityAlreadyExistException.getMessage());
        apiError.setErrors(errors);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(AuthenticationFailureException.class)
    public ResponseEntity handleAuthenticationFailureException
            (AuthenticationFailureException authenticationFailureException) {
        List<String> errors = new ArrayList<String>();
        errors.add("Authentication has failed");

        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.UNAUTHORIZED);
        apiError.setMessage(authenticationFailureException.getMessage());
        apiError.setErrors(errors);
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.BAD_REQUEST);
        apiError.setErrors(errors);
        apiError.setMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(builder.toString());
        return buildResponseEntity(apiError);
    }



    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        RecipeSharingApiError apiError = new RecipeSharingApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(RecipeSharingApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }



}
