package com.example.membership.domain.membership;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MembershipType {

  KAKAO("카카오", KakaoMembership.class),
  NAVER("네이버", NaverMembership.class),
  LINE("라인", LineMembership.class);

  private final String name;
  private final Class<?> clazz;

  public Membership newInstance(Long userId) {
    try {
      return (Membership) this.clazz.getDeclaredConstructor(Long.class).newInstance(userId);
    } catch (Exception e) {
      throw new IllegalStateException("Class constructor can not access.", e);
    }
  }
}
