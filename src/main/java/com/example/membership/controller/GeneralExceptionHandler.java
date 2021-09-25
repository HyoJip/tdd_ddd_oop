package com.example.membership.controller;

import com.example.membership.common.exceptions.PermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

  private ApiResult<?> newResponse(Throwable throwable, HttpStatus status) {
    return ApiResult.error(throwable, status);
  }

  private ApiResult<?> newResponse(Throwable throwable, int statusCode) {
    return ApiResult.error(throwable, statusCode);
  }

  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler({
    IllegalStateException.class, IllegalArgumentException.class, MethodArgumentNotValidException.class,
    MissingRequestHeaderException.class, MethodArgumentTypeMismatchException.class})
  public ApiResult<?> handleBadRequest(Exception e) {
    return this.newResponse(e, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler({EntityNotFoundException.class})
  public ApiResult<?> handleServiceException(Exception e) {
    return this.newResponse(e, ApiStatus.SERVICE_ERROR.code());
  }

  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler({PermissionDeniedException.class})
  public ApiResult<?> handlePermissionException(Exception e) {
    return this.newResponse(e, ApiStatus.PERMISSION_ERROR.code());
  }

  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(Throwable.class)
  public ApiResult<?> handleException(Exception e) {
    return this.newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}