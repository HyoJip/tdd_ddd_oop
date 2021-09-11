package com.example.membership.controller.membership;

import com.example.membership.domain.membership.Membership;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

  public MembershipDTO(Membership membership) {
    BeanUtils.copyProperties(membership, this);
  }
}
