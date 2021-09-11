package com.example.membership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
public class ApiResult<T> {

  @NotNull
  private final boolean success;

  @Nullable
  private final T response;

  @Nullable
  private final ApiError error;

  public static <T> ApiResult<T> ok(T response) {
    return new ApiResult<>(true, response, null);
  }

  public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
    return ApiResult.error(throwable.getMessage(), status.value());
  }

  public static ApiResult<?> error(Throwable throwable, int status) {
    return ApiResult.error(throwable.getMessage(), status);
  }

  public static ApiResult<?> error(String message, HttpStatus status) {
    return ApiResult.error(message, status.value());
  }

  public static ApiResult<?> error(String message, int status) {
    return new ApiResult<>(false, null, new ApiError(message, status));
  }
}
