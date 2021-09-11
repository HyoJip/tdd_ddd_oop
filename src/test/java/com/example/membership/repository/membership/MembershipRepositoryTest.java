package com.example.membership.repository.membership;

import com.example.membership.domain.membership.AmountPolicy;
import com.example.membership.domain.membership.KakaoMembership;
import com.example.membership.domain.membership.Membership;
import com.example.membership.domain.membership.NaverMembership;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MembershipRepositoryTest {

  @Autowired
  MembershipRepository membershipRepository;

  @Test
  @DisplayName("[saveMembership] 멤버쉽 가입을 할 수 있다.")
  void saveMembership_whenSignupRequestIsValid_saveInDB() {
    // given
    Membership membership = new KakaoMembership(1L, new AmountPolicy(1_000L));

    // when
    Membership inDB = this.membershipRepository.save(membership);

    //then
    assertThat(inDB.getId()).isNotNull();
    assertThat(inDB.getCreatedAt()).isNotNull();
    assertThat(inDB.getPoint()).isEqualTo(1_000L);
    assertThat(inDB.getUpdatedAt()).isNotNull();
    assertThat(inDB.getClass()).isEqualTo(KakaoMembership.class);
  }

  @Test
  @DisplayName("[saveMembership] 한 회원은 여러 멤버쉽을 동시에 가입할 수 있다.")
  void saveMembership_whenMembershipRequestIsValid_canSignupMultipleMembership() {
    // given
    Membership kakaoMembership = new KakaoMembership(1L, new AmountPolicy(1_000L));
    Membership naverMembership = new NaverMembership(1L, new AmountPolicy(100L));

    // when
    Membership kakaoInDB = this.membershipRepository.save(kakaoMembership);
    Membership naverInDB = this.membershipRepository.save(naverMembership);

    //then
    assertThat(kakaoInDB.getUserId()).isEqualTo(naverInDB.getUserId());
  }

  @Test
  @DisplayName("[findAllByUserId] 회원 아이디로 멤버쉽을 조회할 수 있다.")
  void findAllByUserId_whenUserIdIsProvided_receiveMemberships() {
    // given
    Membership kakaoMembership = new KakaoMembership(1L, new AmountPolicy(1_000L));
    Membership naverMembership = new NaverMembership(1L, new AmountPolicy(100L));
    this.membershipRepository.save(kakaoMembership);
    this.membershipRepository.save(naverMembership);

    // when
    List<Membership> memberships = this.membershipRepository.findAllByUserId(1L);

    //then
    assertThat(memberships.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("[findAllByUserId] 회원 아이디로 멤버쉽을 조회할 수 있다.")
  void findAllByUserId_whenUserIdIsProvidedButNoMembership_receiveEmpty() {
    // when
    List<Membership> memberships = this.membershipRepository.findAllByUserId(1L);

    //then
    assertThat(memberships.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("[findByUserIdAndDtype] 회원 아이디와 멤버쉽 종류로 조회할 수 있다.")
  void findByUserIdAndDtype_whenUserIdAndDtypeIsProvided_receiveTheMembership() {
    // given
    Membership kakaoMembership = new KakaoMembership(1L, new AmountPolicy(1_000L));
    this.membershipRepository.save(kakaoMembership);

    // when
    Membership membership = this.membershipRepository.findByUserIdAndType(1L, "KAKAO").get();

    //then
    assertThat(membership.getUserId()).isEqualTo(kakaoMembership.getUserId());
  }
}