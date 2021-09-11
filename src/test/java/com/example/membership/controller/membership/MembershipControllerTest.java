package com.example.membership.controller.membership;

import com.example.membership.TestUtils;
import com.example.membership.common.MembershipConst;
import com.example.membership.controller.ApiStatus;
import com.example.membership.domain.membership.*;
import com.example.membership.service.memebership.MembershipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MembershipController.class)
class MembershipControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper om;

  @MockBean
  MembershipService membershipService;

  @Test
  @DisplayName("[멤버쉽가입] 헤더에 회원 아이디가 없을 경우 서비스에러 200(400) 받는다.")
  void postMembershipSignup_whenRequestIsNotValid_receiveBadRequest() throws Exception {
    // when
    // then
    postSignup(null, om.writeValueAsString(new MembershipSignupRequest(MembershipType.KAKAO)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.error.status").value(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  @DisplayName("[멤버쉽가입] 유효한 요청일 경우 200 받는다.")
  void postMembershipSignup_whenRequestIsValid_receiveCreated() throws Exception {
    // when
    // then
    postSignup(1L, om.writeValueAsString(new MembershipSignupRequest(MembershipType.KAKAO)))
      .andExpect(status().isOk());
  }

  @Test
  @DisplayName("[멤버쉽가입] 멤버쉽 타입이 없을 경우 서비스에러 200(400) 받는다.")
  void postMembershipSignup_whenMembershipTypeIsNull_receiveBadRequest() throws Exception {
    // when
    // then
    postSignup(1L, om.writeValueAsString(new MembershipSignupRequest(null)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.error.status").value(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  @DisplayName("[멤버쉽가입] 서비스에서 멤버쉽 중복 에러 발생할 경우 서비스에러 200(400) 받는다.")
  void postMembershipSignup_whenDuplicateErrorInService_receiveBadRequest() throws Exception {
    // given
    when(membershipService.signup(Mockito.any(Membership.class))).thenThrow(new IllegalStateException());

    // when
    // then
    postSignup(1L, om.writeValueAsString(new MembershipSignupRequest(MembershipType.KAKAO)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.error.status").value(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  @DisplayName("[회원 멤버쉽 조회] 회원 아이디가 없을 경우 서비스에러 200(400) 받는다.")
  void getFindUserAllMembership_whenIdIsNotProvided_receiveBadRequest() throws Exception {
    getUserAllMembership(null)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.error.status").value(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  @DisplayName("[회원 멤버쉽 조회] 회원 멤버쉽이 없더라도 빈 리스트를 받는다.")
  void getFindUserAllMembership_whenRequestIsValidButNoMembership_receiveEmptyList() throws Exception {
    // when
    // then
    getUserAllMembership(1L)
      .andExpect(jsonPath("$.response.length()").value(0));
  }

  @Test
  @DisplayName("[회원 멤버쉽 조회] 유효한 요청일 경우 200 받는다.")
  void getFindUserAllMembership_whenRequestIsValid_receiveOk() throws Exception {
    // given
    when(this.membershipService.getUserAllMembership(1L))
      .thenReturn(List.of(TestUtils.createKakaoMembership(1L, null)));

    // when
    // then
    getUserAllMembership(1L)
      .andExpect(status().isOk());
  }

  @Test
  @DisplayName("[회원 멤버쉽 조회] 회원 멤버쉽을 받는다.")
  void getFindUserAllMembership_whenRequestIsValid_receiveMembershipDTO() throws Exception {
    // given
    when(this.membershipService.getUserAllMembership(1L))
      .thenReturn(List.of(
        TestUtils.createKakaoMembership(1L, null),
        TestUtils.createNaverMembership(1L, null)
        )
      );

    // when
    // then
    getUserAllMembership(1L)
      .andExpect(jsonPath("$.response.length()").value(2))
      .andExpect(jsonPath("$.response[*][?(@.userId == 1)]", IsCollectionWithSize.hasSize(2)));
  }

  @Test
  @DisplayName("[회원 단일 멤버쉽 조회] 헤더에 회원 아이디가 없을 경우 200(400) 받는다.")
  void findMembershipByUserIdAndType_whenUserIdIsNotExistsInHeader_receiveBadRequest() throws Exception {
    // given
    Long userId = null;
    String membershipType = MembershipType.KAKAO.name();

    // when
    // then
    getMembership(userId, membershipType)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.error.status").value(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  @DisplayName("[회원 단일 멤버쉽 조회] 존재하지 않는 멤버쉽 타입일 경우 200(400)을 받는다.")
  void findMembershipByUserIdAndType_whenMembershipTypeIsNotExists_receiveBadRequest() throws Exception {
    // given
    Long userId = 1L;
    String membershipType = "notExistMembership";

    // when
    // then
    this.getMembership(userId, membershipType)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.error.status").value(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  @DisplayName("[회원 단일 멤버쉽 조회] 유효한 요청일 경우 200을 받는다.")
  void findMembershipByUserIdAndType_whenRequestIsValid_receiveOk() throws Exception {
    // given
    Long userId = 1L;
    String membershipType = MembershipType.KAKAO.name();

    // when
    // then
    this.getMembership(userId, membershipType)
      .andExpect(status().isOk());
  }

  @Test
  @DisplayName("[회원 단일 멤버쉽 조회] 서비스에서 멤버쉽을 못찾을 경우 서비스에러 200(900) 받음")
  void findMembershipByUserIdAndType_whenMembershipServiceThrowNotFound_receiveBadRequest() throws Exception {
    // given
    Long userId = 1L;
    String membershipType = MembershipType.KAKAO.name();
    when(this.membershipService.getUserMembership(anyLong(), any()))
      .thenThrow(new EntityNotFoundException());

    // when
    // then
    this.getMembership(userId, membershipType)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.error.status").value(ApiStatus.SERVICE_ERROR.code()));
  }



  // ****************************************************************** Method
  private ResultActions postSignup(Long userId, String content) throws Exception {
    return mockMvc
        .perform(
            post("/api/v1/membership/signup")
              .header(MembershipConst.USER_ID_HEADER, ObjectUtils.defaultIfNull(userId, ""))
              .contentType(MediaType.APPLICATION_JSON)
              .content(content)
        );
  }

  private ResultActions getUserAllMembership(Long userId) throws Exception {
    return mockMvc
      .perform(
        get("/api/v1/membership/list")
          .header(MembershipConst.USER_ID_HEADER, ObjectUtils.defaultIfNull(userId, ""))
          .contentType(MediaType.APPLICATION_JSON)
      );
  }

  private ResultActions getMembership(Long userId, String membershipType) throws Exception {
    return mockMvc.perform(
      get("/api/v1/membership")
        .header(MembershipConst.USER_ID_HEADER, ObjectUtils.defaultIfNull(userId, ""))
        .contentType(MediaType.APPLICATION_JSON)
        .param("type", membershipType)
    );
  }
}