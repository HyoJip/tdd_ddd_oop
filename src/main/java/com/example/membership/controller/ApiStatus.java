package com.example.membership.controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApiStatus {
  SERVICE_ERROR(900),
  AUTHENTICATION_ERROR(401),
  PERMISSION_ERROR(403);

  private final int statusCode;

  public int code() {
    return this.statusCode;
  }

}
