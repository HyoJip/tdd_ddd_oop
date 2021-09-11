package com.example.membership.repository.membership;

import com.example.membership.domain.membership.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

  List<Membership> findAllByUserId(long userId);

  @Query("select m from Membership m where userId = :userId and dtype = :type")
  Optional<Membership> findByUserIdAndType(long userId, String type);

}
