package com.example.membership.controller.membership;

import com.example.membership.domain.membership.Membership;
import com.example.membership.domain.membership.MembershipType;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;

public class MembershipSignupRequest {

  @NotNull
  private final MembershipType membershipType;

  @JsonCreator
  public MembershipSignupRequest(MembershipType membershipType) {
    this.membershipType = membershipType;
  }

  public Membership newMembership(Long userId) {
    return this.membershipType.newInstance(userId);
  }
}
