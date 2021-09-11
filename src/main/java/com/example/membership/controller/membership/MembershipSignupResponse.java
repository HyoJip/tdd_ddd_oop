package com.example.membership.controller.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MembershipSignupResponse {

  private final Long id;

  private final String membershipType;

}
