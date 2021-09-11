package com.example.membership.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MembershipConst {

  public static final String USER_ID_HEADER = "X-USER-ID";

  public static final String KAKAO_MEMBERSHIP_NAME = "KAKAO";
  public static final String NAVER_MEMBERSHIP_NAME = "NAVER";
  public static final String LINE_MEMBERSHIP_NAME = "LINE";
}
