package com.example.membership.common.exceptions;

import com.example.membership.utils.MessageUtils;

public class EntityUnauthorizedException extends ServiceRuntimeException {

  public static final String MESSAGE_KEY = "exception.auth";

  public static final String MESSAGE_DETAIL = "exception.auth.detail";

  public EntityUnauthorizedException(String... args) {
    super(MESSAGE_KEY, MESSAGE_DETAIL, args);
  }

  @Override
  public String getMessage() {
    return MessageUtils.getMessage(super.getMessageDetail(), super.getArgs());
  }

  @Override
  public String toString() {
    return MessageUtils.getMessage(super.getMessageKey());
  }
}
