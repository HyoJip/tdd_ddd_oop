package com.example.membership.domain.membership;

import com.example.membership.domain.payment.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

class OrdererTest {

  Orderer orderer;

  @BeforeEach
  void setup() {
    this.orderer = new Orderer(1L);
  }

  @Test
  @DisplayName("멤버쉽 가입하지 않은 회원은 마일리지 적립할 수 없다.")
  void userWithoutMembership_canNotCollectMileage() {
    // given
    Payment payment = new Payment(this.orderer, 100_000L);

    // when
    // then
    assertThatThrownBy(() -> this.orderer.collectMileage(payment),
        "collection mileage needs a membership signup.");
    assertThatNullPointerException();
  }

  @Test
  @DisplayName("멤버쉽 가입한 회원은 마일리지 적립할 수 있다.")
  void userWithMembership_canCollectMileage() {
    // given
    this.orderer.signupMembership(new KakaoMembership(this.orderer.getMemberId(), new AmountPolicy(100)));
    Payment payment = new Payment(this.orderer, 100_000L);

    // when
    this.orderer.collectMileage(payment);

    // then
    assertThatNoException();
  }

  @Test
  @DisplayName("마일리지 적립은 기본적으로 결제일 30일 이내에 가능하다.")
  void mileagePolicy_canCollectWithin30Days() {
    // given
    this.orderer.signupMembership(new KakaoMembership(this.orderer.getMemberId(), new AmountPolicy(100)));
    Payment payment = new Payment(this.orderer, 100_000L, LocalDateTime.now().minus(30, ChronoUnit.DAYS));

    // when
    this.orderer.collectMileage(payment);

    // then
    assertThatNoException();
  }

  @Test
  @DisplayName("결제일로부터 30일 초과시 마일리지를 적립할 수 없다.")
  void mileagePolicy_throwIllegalState_whenOver30Days() {
    // given
    this.orderer.signupMembership(new KakaoMembership(this.orderer.getMemberId(), new AmountPolicy(100)));
    Payment payment = new Payment(this.orderer, 100_000L, LocalDateTime.now().minus(31, ChronoUnit.DAYS));

    // when
    // then
    assertThatThrownBy(() -> this.orderer.collectMileage(payment), "check mileage condition");
    assertThatIllegalStateException();
  }


}
