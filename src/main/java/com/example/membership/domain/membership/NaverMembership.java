package com.example.membership.domain.membership;

import com.example.membership.common.MembershipConst;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import java.time.LocalDateTime;

import static com.example.membership.common.MembershipConst.NAVER_MEMBERSHIP_NAME;

@Entity
@DiscriminatorValue(NAVER_MEMBERSHIP_NAME)
public class NaverMembership extends Membership {

  @Transient
  public static final PercentPolicy DEFUALT_POLICY = new PercentPolicy(0.01);
  @Transient
  private static final Long DEFAULT_POINT = 0L;

  public NaverMembership(Long userId) {
    this(userId, DEFUALT_POLICY);
  }

  public NaverMembership(Long userId, MileagePolicy mileagePolicy) {
    super(userId, mileagePolicy);
    this.point = DEFAULT_POINT;
  }

  @Builder
  public NaverMembership(Long id, Long userId, Long point, LocalDateTime createdAt, LocalDateTime updatedAt, MileagePolicy mileagePolicy) {
    super(id, userId, point, createdAt, updatedAt, mileagePolicy);
    this.point = ObjectUtils.defaultIfNull(point, DEFAULT_POINT);
    this.mileagePolicy = ObjectUtils.defaultIfNull(mileagePolicy, DEFUALT_POLICY);
  }
}
