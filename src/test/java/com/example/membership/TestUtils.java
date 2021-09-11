package com.example.membership;

import com.example.membership.domain.membership.KakaoMembership;
import com.example.membership.domain.membership.Membership;
import com.example.membership.domain.membership.MileagePolicy;
import com.example.membership.domain.membership.NaverMembership;

public class TestUtils {

  private static long membershipIpSequence = 0L;

  public static Membership createKakaoMembership(long userId, MileagePolicy mileagePolicy) {
    return KakaoMembership.builder()
      .id(membershipIpSequence++)
      .userId(userId)
      .mileagePolicy(mileagePolicy)
      .build();
  }

  public static Membership createNaverMembership(long userId, MileagePolicy mileagePolicy) {
    return NaverMembership.builder()
      .id(membershipIpSequence++)
      .userId(userId)
      .mileagePolicy(mileagePolicy)
      .build();
  }
}
