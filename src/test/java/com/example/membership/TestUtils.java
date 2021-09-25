package com.example.membership;

import com.example.membership.controller.membership.PointCollectingRequest;
import com.example.membership.domain.membership.KakaoMembership;
import com.example.membership.domain.membership.Membership;
import com.example.membership.domain.membership.MileagePolicy;
import com.example.membership.domain.membership.NaverMembership;

public class TestUtils {

  private static long membershipIdSequence = 0L;

  public static Membership createKakaoMembership(long userId, MileagePolicy mileagePolicy) {
    return KakaoMembership.builder()
      .id(membershipIdSequence++)
      .userId(userId)
      .mileagePolicy(mileagePolicy)
      .build();
  }

  public static Membership createNaverMembership(long userId, MileagePolicy mileagePolicy) {
    return NaverMembership.builder()
      .id(membershipIdSequence++)
      .userId(userId)
      .mileagePolicy(mileagePolicy)
      .build();
  }

  public static PointCollectingRequest createPointCollectRequest(long ordererId, long membershipId, long price) {
    return PointCollectingRequest.builder()
      .ordererId(ordererId)
      .membershipId(membershipId)
      .price(price)
      .build();
  }
}
