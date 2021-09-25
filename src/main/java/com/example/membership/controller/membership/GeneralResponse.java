package com.example.membership.controller.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeneralResponse {

  private final String message;

  public static GeneralResponse of(String message) {
    return new GeneralResponse(message);
  }
}
