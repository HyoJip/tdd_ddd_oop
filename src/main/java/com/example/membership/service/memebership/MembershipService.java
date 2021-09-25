package com.example.membership.service.memebership;

import com.example.membership.common.exceptions.EntityUnauthorizedException;
import com.example.membership.controller.membership.MembershipSignupResponse;
import com.example.membership.domain.membership.Membership;
import com.example.membership.domain.membership.MembershipType;
import com.example.membership.repository.membership.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.DiscriminatorValue;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
public class MembershipService {

  private final MembershipRepository membershipRepository;

  public MembershipSignupResponse signup(Membership membership) {
    this.checkDuplicate(membership);
    Membership inDB = this.membershipRepository.save(membership);

    return new MembershipSignupResponse(inDB.getId(), this.getMembershipType(inDB));
  }

  private void checkDuplicate(Membership membership) {
    this.membershipRepository.findByUserIdAndType(
        membership.getUserId(),
        getMembershipType(membership)
    ).ifPresent(inDB -> {
      throw new IllegalStateException(
        String.format(
          "The membership already exists: userId=%d, type=%s",
          inDB.getUserId(),
          this.getMembershipType(inDB)
        )
      );
    });
  }

  private String getMembershipType(Membership membership) {
    return membership.getClass().getDeclaredAnnotation(DiscriminatorValue.class).value();
  }

  public List<Membership> getUserAllMembership(long userId) {
    checkNotNull(userId, "userId must be provided.");

    return this.membershipRepository.findAllByUserId(userId);
  }

  public Membership getUserMembership(long userId, MembershipType type) {
    checkArgument(userId >= 0, "userId must be positive.");
    checkNotNull(type, "membershipType is must be provided.");

    return this.membershipRepository.findByUserIdAndType(userId, type.name())
      .orElseThrow(() -> new EntityNotFoundException(
        String.format("The Membership[userId=%d, membershipType=%s] can not found.", userId, type.name())
        )
      );
  }

  public void cancelMembership(long membershipId, long userId) {
    Membership membership = this.membershipRepository.findById(membershipId)
      .orElseThrow(() -> new EntityNotFoundException(
          String.format("The Membership[id=%d] can not found.", membershipId)
        )
      );

    if (membership.getUserId() != userId) {
      throw new EntityUnauthorizedException(String.format("It's not same owner(%d != %d)", membership.getUserId(), userId));
    }

    this.membershipRepository.deleteById(membershipId);
  }
}
