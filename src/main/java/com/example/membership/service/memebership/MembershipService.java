package com.example.membership.service.memebership;

import com.example.membership.common.exceptions.EntityUnauthorizedException;
import com.example.membership.controller.membership.MembershipDTO;
import com.example.membership.controller.membership.MembershipSignupResponse;
import com.example.membership.domain.membership.Membership;
import com.example.membership.domain.membership.MembershipType;
import com.example.membership.domain.payment.Payment;
import com.example.membership.repository.membership.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.DiscriminatorValue;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.google.common.base.Preconditions.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {

  private final MembershipRepository membershipRepository;

  @Transactional
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
    checkState(userId >= 0, "userId can not be negative.");

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

  @Transactional
  public void cancelMembership(long membershipId, long userId) {
    Membership membership = getMembership(membershipId);

    if (membership.getUserId() != userId) {
      throw new EntityUnauthorizedException(String.format("It's not same owner(%d != %d)", membership.getUserId(), userId));
    }

    this.membershipRepository.deleteById(membershipId);
  }

  public Membership getMembership(Long membershipId) {
    checkNotNull(membershipId, "MembershipId must be provided.");

    return this.membershipRepository.findById(membershipId)
      .orElseThrow(() -> new EntityNotFoundException(
          String.format("The Membership[id=%d] can not found.", membershipId)
        )
      );
  }

  @Transactional
  public MembershipDTO accumulate(Long loggedUserId, Payment payment) {

    checkArgument(payment != null, "Payment must be provided.");
    checkState(loggedUserId != null && loggedUserId == payment.getOrderer().getMemberId(),
      "LoggedUserId must be provided and Payment ordererId must be same with.");

    Membership membership = this.getMembership(payment.getOrderer().getMembershipId());
    membership.collectMileage(payment);

    return new MembershipDTO(this.membershipRepository.save(membership));
  }
}
