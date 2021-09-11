package com.example.membership.controller.membership;

import com.example.membership.controller.ApiResult;
import com.example.membership.domain.membership.MembershipType;
import com.example.membership.service.memebership.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.membership.common.MembershipConst.USER_ID_HEADER;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/membership")
public class MembershipController {

  private final MembershipService membershipService;

  @PostMapping("/signup")
  public ApiResult<MembershipSignupResponse> signup(
    @RequestHeader(USER_ID_HEADER) Long userId,
    @RequestBody @Valid MembershipSignupRequest request
  ) {
    return ApiResult.ok(
        this.membershipService.signup(request.newMembership(userId))
    );
  }

  @GetMapping("/list")
  public ApiResult<List<MembershipDTO>> findAllListOf(@RequestHeader(USER_ID_HEADER) Long userId) {
    return ApiResult.ok(
      this.membershipService.getUserAllMembership(userId).stream()
        .map(MembershipDTO::new)
        .collect(Collectors.toList())
    );
  }

  @GetMapping
  public ApiResult<MembershipDTO> findMembership(
    @RequestHeader(USER_ID_HEADER) Long userId,
    @RequestParam("type") MembershipType membershipType
  ) {
    return ApiResult.ok(
      new MembershipDTO(this.membershipService.getUserMembership(userId, membershipType))
    );
  }
}
