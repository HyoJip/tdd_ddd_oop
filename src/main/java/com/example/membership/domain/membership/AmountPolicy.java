package com.example.membership.domain.membership;

public class AmountPolicy extends MileagePolicy {

  private long amount;

  public AmountPolicy(long amount) {
    super();
    this.amount = amount;
  }

  public AmountPolicy(long amount, MileageCondition condition) {
    super(condition);
    this.amount = amount;
  }

  @Override
  protected long getMileagePoint(long price) {
    return price - this.amount;
  }
}
