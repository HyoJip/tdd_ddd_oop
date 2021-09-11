package com.example.membership.controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApiStatus {
  SERVICE_ERROR(900);

  private final int statusCode;

  public int code() {
    return this.statusCode;
  }

}
