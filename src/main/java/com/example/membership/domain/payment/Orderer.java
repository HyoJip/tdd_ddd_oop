package com.example.membership.domain.payment;

import com.example.membership.domain.membership.Membership;
import com.example.membership.domain.payment.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@RequiredArgsConstructor
public class Orderer {

  private final Long memberId;
  private final Long membershipId;

}
