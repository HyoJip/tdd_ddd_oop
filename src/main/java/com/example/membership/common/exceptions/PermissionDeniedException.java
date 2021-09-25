package com.example.membership.common.exceptions;

import com.example.membership.utils.MessageUtils;

public class PermissionDeniedException extends ServiceRuntimeException {

  public static final String MESSAGE_KEY = "exception.permission";

  public static final String MESSAGE_DETAIL = "exception.permission.detail";

  public PermissionDeniedException(Object... args) {
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
