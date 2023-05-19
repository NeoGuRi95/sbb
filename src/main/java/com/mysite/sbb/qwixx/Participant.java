package com.mysite.sbb.qwixx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysite.sbb.IntegerArrayConverter;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Boolean isMaster;

  private Boolean ready = false;

  @ManyToOne
  private Room room;

  @ManyToOne
  private SiteUser siteUser;

  @Convert(converter = IntegerArrayConverter.class)
  private List<Integer> redLine;

  @Convert(converter = IntegerArrayConverter.class)
  private List<Integer> yellowLine;

  @Convert(converter = IntegerArrayConverter.class)
  private List<Integer> greenLine;

  @Convert(converter = IntegerArrayConverter.class)
  private List<Integer> blueLine;

  @Builder
  public Participant(Boolean isMaster, SiteUser siteUser, Room room) {
    this.isMaster = isMaster;
    this.siteUser = siteUser;
    this.room = room;
  }

  public void init() {
    this.redLine = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
    this.yellowLine = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
    this.greenLine = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
    this.blueLine = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
  }
}
