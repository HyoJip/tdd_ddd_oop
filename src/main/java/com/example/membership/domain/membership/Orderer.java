package com.example.membership.domain.membership;

import com.example.membership.domain.payment.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@RequiredArgsConstructor
public class Orderer {

  private final Long memberId;
  private Membership membership;

  public void collectMileage(Payment payment) {
    checkNotNull(this.membership, "collection mileage needs a membership signup.");
    this.membership.collectMileage(payment);
  }

  public void signupMembership(Membership membership) {
    this.membership = membership;
  }

}
