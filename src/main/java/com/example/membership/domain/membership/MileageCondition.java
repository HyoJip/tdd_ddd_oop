package com.example.membership.domain.membership;

import com.example.membership.domain.payment.Payment;

public interface MileageCondition {

  boolean isSatisfiedBy(Payment payment);

}
