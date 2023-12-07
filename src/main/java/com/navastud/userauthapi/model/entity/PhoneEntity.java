package com.navastud.userauthapi.model.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  private long number;
  private int cityCode;
  private String countryCode;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;
}
