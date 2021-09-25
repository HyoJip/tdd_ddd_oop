package com.example.membership.controller.membership;

import com.example.membership.domain.payment.Orderer;
import com.example.membership.domain.payment.Payment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;

@ToString
@Getter
public class PointCollectingRequest {

  private final Long ordererId;
  private final Long membershipId;
  private final Long price;
  private final LocalDateTime when;

  @Builder
  public PointCollectingRequest(Long ordererId, Long membershipId, Long price) {
    this(ordererId, membershipId, price, null);
  }

  @JsonCreator
  public PointCollectingRequest(
    @JsonProperty("ordererId") Long ordererId,
    @JsonProperty("membershipId") Long membershipId,
    @JsonProperty("price") Long price,
    @JsonProperty("when") LocalDateTime when) {
    this.ordererId = ordererId;
    this.membershipId = membershipId;
    this.price = price;
    this.when = ObjectUtils.defaultIfNull(when, LocalDateTime.now());
  }

  public Payment toPayment() {
    return new Payment(new Orderer(this.ordererId, this.membershipId), this.price, this.when);
  }
}
