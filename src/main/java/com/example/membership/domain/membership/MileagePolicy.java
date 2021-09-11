package com.example.membership.domain.membership;

import com.example.membership.domain.payment.Payment;
import com.google.common.base.Preconditions;

public abstract class MileagePolicy {

  private MileageCondition condition;

  public MileagePolicy() {
   this(new TimeCondition(30));
  }

  protected MileagePolicy(MileageCondition condition) {
    this.condition = condition;
  }

  public long calculateMileage(Payment payment) {
    Preconditions.checkState(!this.condition.isSatisfiedBy(payment), "check mileage condition");
    return this.getMileagePoint(payment.getPrice());
  }

  protected abstract long getMileagePoint(long price);
}
