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
  protected Long getMileagePoint(Long price) {
    return price - this.amount;
  }
}
