package com.mysite.sbb.qwixx;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Boolean status;

  private Integer participantsNumber;

  @Builder
  public Room(Boolean status, Integer participantsNumber) {
    this.status = status;
    this.participantsNumber = participantsNumber;
  }
}
