package com.example.membership.domain.membership;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import static com.example.membership.common.MembershipConst.LINE_MEMBERSHIP_NAME;

@Entity
@DiscriminatorValue(LINE_MEMBERSHIP_NAME)
public class LineMembership extends Membership {
}
