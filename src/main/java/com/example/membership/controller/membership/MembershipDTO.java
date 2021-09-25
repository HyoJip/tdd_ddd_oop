package com.example.membership.controller.membership;

import com.example.membership.domain.membership.Membership;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MembershipDTO {

  private long id;

  private long userId;

  private long point;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @Builder
  public MembershipDTO(long id, long userId, long point) {
    this(id, userId, point, null, null);
  }

  public MembershipDTO(long id, long userId, long point, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.userId = userId;
    this.point = point;
    this.createdAt = ObjectUtils.defaultIfNull(createdAt, LocalDateTime.now());
    this.updatedAt = ObjectUtils.defaultIfNull(updatedAt, LocalDateTime.now());
  }

  public MembershipDTO (Membership membership) {
    BeanUtils.copyProperties(membership, this);
  }
}
