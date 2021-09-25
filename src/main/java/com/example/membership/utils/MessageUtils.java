package com.example.membership.utils;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {

  private static MessageSourceAccessor messageSourceAccessor;

  public static String getMessage(String key) {
    Preconditions.checkState(MessageUtils.messageSourceAccessor != null, "MessageSourceAccessor must be initialized.");
    return MessageUtils.messageSourceAccessor.getMessage(key);
  }

  public static String getMessage(String key, Object... args) {
    Preconditions.checkState(MessageUtils.messageSourceAccessor != null, "MessageSourceAccessor must be initialized.");
    return MessageUtils.messageSourceAccessor.getMessage(key, args);
  }

  public static void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
    MessageUtils.messageSourceAccessor = messageSourceAccessor;
  }
}
