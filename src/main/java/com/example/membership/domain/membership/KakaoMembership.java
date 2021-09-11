package com.example.membership.domain.membership;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import java.time.LocalDateTime;

import static com.example.membership.common.MembershipConst.KAKAO_MEMBERSHIP_NAME;

@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(KAKAO_MEMBERSHIP_NAME)
public class KakaoMembership extends Membership {

  @Transient
  public static final PercentPolicy DEFUALT_POLICY = new PercentPolicy(0.01);

  @Transient
  private static final Long DEFAULT_POINT = 1_000L;

  public KakaoMembership(Long userId) {
    this(userId, null);
  }

  public KakaoMembership(Long userId, MileagePolicy mileagePolicy) {
    this(null, userId, null, null, null, mileagePolicy);
  }

  @Builder
  public KakaoMembership(Long id, Long userId, Long point, LocalDateTime createdAt, LocalDateTime updatedAt, MileagePolicy mileagePolicy) {
    super(id, userId, point, createdAt, updatedAt, mileagePolicy);
    this.point = ObjectUtils.defaultIfNull(point, DEFAULT_POINT);
    this.mileagePolicy = ObjectUtils.defaultIfNull(mileagePolicy, DEFUALT_POLICY);
  }
}
