package com.example.membership.domain.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Payment {

  private final Orderer orderer;
  private final Long price;
  private final LocalDateTime when;

  public Payment(Orderer orderer, Long price) {
    this.orderer = orderer;
    this.price = price;
    this.when = LocalDateTime.now();
  }

}
