package com.mysite.sbb.qwixx.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mysite.sbb.qwixx.RoomSiteUser;

import lombok.Data;

@Data
public class GameInfoResponse {
  private Long roomId;

  private Boolean fullRoom = false;

  private List<RoomSiteUser> participants = new ArrayList<>();

  /*
   * 주사위 정보
   */
  Random random = new Random();

  private Integer white1;

  private Integer white2;

  private Integer red;

  private Integer yellow;

  private Integer green;

  private Integer blue;

  private Integer whiteSum;

  private Integer redSum1;

  private Integer redSum2;

  private Integer yellowSum1;

  private Integer yellowSum2;

  private Integer greenSum1;

  private Integer greenSum2;

  private Integer blueSum1;

  private Integer blueSum2;

  public void addParticipant(RoomSiteUser roomSiteUser) {
    participants.add(roomSiteUser);
  }

  public void roll() {
    this.white1 = this.random.nextInt(1, 7);
    this.white2 = this.random.nextInt(1, 7);
    this.red = this.random.nextInt(1, 7);
    this.yellow = this.random.nextInt(1, 7);
    this.green = this.random.nextInt(1, 7);
    this.blue = this.random.nextInt(1, 7);

    this.whiteSum = this.white1 + this.white2;
    this.redSum1 = this.white1 + this.red;
    this.redSum2 = this.white2 + this.red;
    this.yellowSum1 = this.white1 + this.red;
    this.yellowSum2 = this.white2 + this.red;
    this.redSum1 = this.white1 + this.red;
    this.redSum2 = this.white2 + this.red;
  }
}
