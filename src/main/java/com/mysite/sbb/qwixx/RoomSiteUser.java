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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomSiteUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Room room;

  @ManyToOne
  private SiteUser siteUser;

  @Convert(converter = IntegerArrayConverter.class)
  private List<Boolean> redLine;

  @Convert(converter = IntegerArrayConverter.class)
  private List<Boolean> yellowLine;

  @Convert(converter = IntegerArrayConverter.class)
  private List<Boolean> greenLine;

  @Convert(converter = IntegerArrayConverter.class)
  private List<Boolean> blueLine;

  @Builder
  public RoomSiteUser(SiteUser siteUser, Room room) {
    this.siteUser = siteUser;
    this.room = room;
  }

  public void init() {
    this.redLine = new ArrayList<>(
        Arrays.asList(false, false, false, false, false, false, false, false, false, false, false, false));
    this.yellowLine = new ArrayList<>(
        Arrays.asList(false, false, false, false, false, false, false, false, false, false, false, false));
    this.greenLine = new ArrayList<>(
        Arrays.asList(false, false, false, false, false, false, false, false, false, false, false, false));
    this.blueLine = new ArrayList<>(
        Arrays.asList(false, false, false, false, false, false, false, false, false, false, false, false));
  }
}
