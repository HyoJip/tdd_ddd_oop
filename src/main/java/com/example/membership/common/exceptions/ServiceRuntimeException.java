package com.example.membership.common.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ServiceRuntimeException extends RuntimeException {

  private final String messageKey;

  private final String messageDetail;

  private final Object[] args;

}
