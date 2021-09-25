package com.example.membership.domain.membership;

public class PercentPolicy extends MileagePolicy {

  private double percent;

  public PercentPolicy(double percent) {
    super();
    this.percent = percent;
  }

  public PercentPolicy(double percent, MileageCondition condition) {
    super(condition);
    this.percent = percent;
  }

  @Override
  protected Long getMileagePoint(Long price) {
    Double calculatedPoint = price * (1 + percent);
    return calculatedPoint.longValue();
  }
}
