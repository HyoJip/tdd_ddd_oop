package com.example.membership.controller.membership;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PointCollectingResult {

  private final Long ordererId;
  private final Long membershipId;
  private final Long totalPoint;

  public PointCollectingResult(MembershipDTO membershipDTO) {
    this.ordererId = membershipDTO.getUserId();
    this.membershipId = membershipDTO.getId();
    this.totalPoint = membershipDTO.getPoint();
  }
}
