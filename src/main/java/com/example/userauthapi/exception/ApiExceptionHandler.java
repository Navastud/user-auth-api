package com.example.userauthapi.exception;

import com.example.userauthapi.model.dto.ErrorResponseDTO;
import io.jsonwebtoken.JwtException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.JDBCException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.FORBIDDEN);
  }


  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handlerNotFoundException(NotFoundException exception) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Object> handlerUserAlreadyExistsException(
      UserAlreadyExistsException exception) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler({TokenExpiredException.class, JwtException.class})
  public ResponseEntity<Object> handlerTokenExpiredException(Exception exception) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({
      BadRequestException.class,
      DuplicateKeyException.class,
      MissingRequestHeaderException.class
  })
  public ResponseEntity<Object> handlerBadRequestException(BadRequestException exception) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<Object> handlerConflictException(ConflictException exception) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<Object> handlerInternalServerErrorException(
      InternalServerErrorException exception) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(
        exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(JDBCException.class)
  public ResponseEntity<Object> handlerJDBCException(JDBCException exception) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(
        exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
  public ResponseEntity<Object> handlerArgumentNotValid(WebRequest request, Exception exception) {
    String detail = exception.getMessage();

    if (exception instanceof ConstraintViolationException) {
      ConstraintViolationException ex = (ConstraintViolationException) exception;
      for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
        detail = violation.getMessage();
      }
    }
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(detail, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception exception,
      Object body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    log.error(exception);
    exception.printStackTrace();

    BindingResult bindingResult = exception.getBindingResult();
    String message =
        bindingResult.getAllErrors().stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.joining(", "));

    return buildResponseEntityErrorResponseDTO(message, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

    MethodArgumentTypeMismatchException ex = (MethodArgumentTypeMismatchException) exception;

    String detail = "Error in argument " + ex.getName() + " with value '" + ex.getValue() + "'";
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(
      ServletRequestBindingException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.error(exception);
    exception.printStackTrace();
    return buildResponseEntityErrorResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  private static ResponseEntity<Object> buildResponseEntityErrorResponseDTO(
      String detail, HttpStatus status) {
    return ResponseEntity.status(status)
        .body(
            ErrorResponseDTO.builder()
                .code(status.value())
                .detail(detail)
                .timestamp(LocalDateTime.now())
                .build());
  }
}
