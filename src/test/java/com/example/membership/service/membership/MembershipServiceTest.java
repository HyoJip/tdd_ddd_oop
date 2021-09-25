package com.example.membership.service.membership;

import com.example.membership.TestUtils;
import com.example.membership.common.exceptions.EntityUnauthorizedException;
import com.example.membership.controller.membership.MembershipSignupResponse;
import com.example.membership.domain.membership.KakaoMembership;
import com.example.membership.domain.membership.Membership;
import com.example.membership.domain.membership.MembershipType;
import com.example.membership.repository.membership.MembershipRepository;
import com.example.membership.service.memebership.MembershipService;
import com.example.membership.utils.MessageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

  @Mock
  MembershipRepository membershipRepository;

  @InjectMocks
  MembershipService membershipService;

  @BeforeEach
  void setup() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("/i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    MessageUtils.setMessageSourceAccessor(new MessageSourceAccessor(messageSource));
  }

  @Test
  @DisplayName("[멤버쉽가입] 중복된 멤버쉽 가입은 불가능하다.")
  void signup_whenSameMembershipIsAlreadyExists_receiveError() {
    // given
    Membership membership = TestUtils.createKakaoMembership(1L, null);
    when(membershipRepository.findByUserIdAndType(membership.getUserId(), MembershipType.KAKAO.name()))
      .thenReturn(Optional.of(membership));

    // when
    // then
    assertThatThrownBy(() -> membershipService.signup(membership))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("The membership already exists: userId=1, type=KAKAO");
  }

  @Test
  @DisplayName("[멤버쉽가입] 유효한 멤버십 가입요청은 가입가능하다.")
  void signup_whenMembershipSignupRequestIsValid_successSignup() {
    // given
    Membership membership = TestUtils.createKakaoMembership(1L, null);
    when(membershipRepository.save(membership))
      .thenReturn(membership);
    when(membershipRepository.findByUserIdAndType(membership.getUserId(), MembershipType.KAKAO.name()))
      .thenReturn(Optional.empty());

    // when
    this.membershipService.signup(membership);

    // then
    assertThatNoException();
    Mockito.verify(this.membershipRepository, Mockito.times(1))
      .findByUserIdAndType(Mockito.anyLong(), Mockito.anyString());
    Mockito.verify(this.membershipRepository, Mockito.times(1))
      .save(Mockito.any(Membership.class));
  }

  @Test
  @DisplayName("[멤버쉽가입] 유효한 멤버십 가입요청은 성공 응답 객체를 반환한다.")
  void signup_whenMembershipSignupRequestIsValid_receiveSuccessResponseDTO() {
    // given
    Membership membership = TestUtils.createKakaoMembership(1L, null);
    when(membershipRepository.save(membership))
      .thenReturn(membership);
    when(membershipRepository.findByUserIdAndType(membership.getUserId(), MembershipType.KAKAO.name()))
      .thenReturn(Optional.empty());

    // when
    MembershipSignupResponse response = this.membershipService.signup(membership);

    // then
    assertThat(response.getId()).isEqualTo(membership.getId());
    assertThat(response.getMembershipType()).isEqualTo(MembershipType.KAKAO.name());
  }

  @Test
  @DisplayName("[멤버쉽조회] 회원 아이디로 멤버쉽을 조회할 수 있다.")
  void findUsersAllMembership_whenMembershipIsMoreThanZero_receiveMemberships() {
    // given
    Membership membership = TestUtils.createKakaoMembership(1L, null);
    when(membershipRepository.findAllByUserId(1L))
      .thenReturn(List.of(membership));

    // when
    List<Membership> memberships = this.membershipService.getUserAllMembership(1L);

    // then
    assertThat(memberships.size()).isEqualTo(1);
    assertThat(memberships.get(0).getUserId()).isEqualTo(1L);
    assertThat(memberships.get(0).getClass()).isEqualTo(KakaoMembership.class);
  }

  @Test
  @DisplayName("[멤버쉽조회] 회원 아이디와 멤버쉽 종류로 해당 멤버쉽을 조회할 수 있다.")
  void findByUserIdAndType_whenMembershipIsExist_receiveTheMembership() {
    // given
    Membership membership = TestUtils.createKakaoMembership(1L, null);
    when(membershipRepository.findByUserIdAndType(1L, MembershipType.KAKAO.name()))
      .thenReturn(Optional.of(membership));

    // when
    Membership inDB = this.membershipService.getUserMembership(1L, MembershipType.KAKAO);

    // then
    assertThat(inDB.getUserId()).isEqualTo(membership.getUserId());
    assertThat(inDB.getClass()).isEqualTo(KakaoMembership.class);
  }

  @Test
  @DisplayName("[멤버쉽조회] 회원 아이디가 음수면 예외 발생")
  void findByUserIdAndType_whenUserIdIsNotProvided_throwException() {
    // given
    long userId = -1L;

    // when
    // then
    assertThatThrownBy(() -> this.membershipService.getUserMembership(userId, MembershipType.KAKAO))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("userId must be positive.");
  }

  @Test
  @DisplayName("[멤버쉽조회] 멤버쉽 타입이 널이면 예외 발생")
  void findByUserIdAndType_whenMembershipTypeIsNull_throwException() {
    // given
    MembershipType membershipType = null;

    // when
    // then
    assertThatThrownBy(() -> this.membershipService.getUserMembership(1L, membershipType))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("membershipType is must be provided.");
  }

  @Test
  @DisplayName("[멤버쉽조회] 해당하는 회원의 멤버쉽이 없을 경우 예외 발생")
  void findByUserIdAndType_whenTheMembershipIsNotExists_throwException() {
    // given
    when(this.membershipRepository.findByUserIdAndType(anyLong(), any()))
      .thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> this.membershipService.getUserMembership(1L, MembershipType.KAKAO))
      .isInstanceOf(EntityNotFoundException.class)
      .hasMessage("The Membership[userId=1, membershipType=KAKAO] can not found.");
  }

  @Test
  @DisplayName("[멤버쉽삭제] 멤버쉽 아이디가 없을 경우 예외 발생")
  void deleteMembership_whenMembershipIsNotExist_throwException() {
    // given
    when(this.membershipRepository.findById(anyLong()))
      .thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> this.membershipService.cancelMembership(1L, 1L))
      .isInstanceOf(EntityNotFoundException.class)
      .hasMessage("The Membership[id=1] can not found.");
  }

  @Test
  @DisplayName("[멤버쉽삭제] 해당 멤버쉽이 본인 것이 아닐 경우 서비스 예외 발생")
  void deleteMembership_whenMembershipIsNotOwn_throwException() {
    // given
    Membership kakaoMembership = TestUtils.createKakaoMembership(1L, null);
    when(this.membershipRepository.findById(anyLong()))
      .thenReturn(Optional.of(kakaoMembership));

    // when
    // then
    assertThatThrownBy(() -> this.membershipService.cancelMembership(kakaoMembership.getId(), 999L))
      .isInstanceOf(EntityUnauthorizedException.class)
      .hasMessage("Not authorized: It's not same owner(1 != 999)");
  }

}