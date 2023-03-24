package com.mysite.sbb.qwixx.dto;

import java.util.ArrayList;
import java.util.List;

import com.mysite.sbb.qwixx.RoomSiteUser;

import lombok.Data;

@Data
public class GameResponse {
  private Long roomId;

  private Boolean fullRoom = false;

  private List<RoomSiteUser> participants = new ArrayList<>();

  public void addParticipant(RoomSiteUser roomSiteUser) {
    participants.add(roomSiteUser);
  }
}
