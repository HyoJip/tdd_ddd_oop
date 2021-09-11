package com.example.membership.domain.payment;

import com.example.membership.domain.membership.Orderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Payment {

  private final Orderer orderer;
  private final long price;
  private final LocalDateTime when;

  public Payment(Orderer orderer, long price) {
    this.orderer = orderer;
    this.price = price;
    this.when = LocalDateTime.now();
  }
}
