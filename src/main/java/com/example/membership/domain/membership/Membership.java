package com.example.membership.domain.membership;

import com.example.membership.domain.payment.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Inheritance
public abstract class Membership {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @ColumnDefault("0")
  @Column(nullable = false)
  protected Long point;

  @Column(nullable = false, updatable = false, length = 20)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(length = 20)
  private LocalDateTime updatedAt;

  @Transient
  protected MileagePolicy mileagePolicy;

  protected Membership(Long userId, MileagePolicy mileagePolicy) {
    this(null, userId, null, null, null, mileagePolicy);
  }

  protected Membership(Long id, Long userId, Long point, LocalDateTime createdAt, LocalDateTime updatedAt, MileagePolicy mileagePolicy) {
    this.id = id;
    this.userId = userId;
    this.point = defaultIfNull(point, 0L);
    this.createdAt = defaultIfNull(createdAt, LocalDateTime.now());
    this.updatedAt = defaultIfNull(updatedAt, LocalDateTime.now());
    this.mileagePolicy = mileagePolicy;
  }

  public long collectMileage(Payment payment) {
    return this.mileagePolicy.calculateMileage(payment);
  }
}
