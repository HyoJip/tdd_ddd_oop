package com.example.membership.domain.membership;

import com.example.membership.domain.payment.Payment;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class TimeCondition implements MileageCondition {

  private final int deadLine;

  @Override
  public boolean isSatisfiedBy(Payment payment) {
    return payment.getWhen()
        .plus(this.deadLine, ChronoUnit.DAYS)
        .isBefore(ChronoLocalDateTime.from(LocalDateTime.now()));
  }
}
