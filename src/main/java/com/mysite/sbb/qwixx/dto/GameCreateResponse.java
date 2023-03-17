package com.mysite.sbb.qwixx.dto;

import java.util.List;

import com.mysite.sbb.qwixx.RoomSiteUser;

import lombok.Data;

import java.util.ArrayList;

@Data
public class GameCreateResponse {
  private Long roomId;

  private List<RoomSiteUser> participants = new ArrayList<>();

  public void setParticipants(RoomSiteUser firstRoomSiteUser) {
    this.participants.add(firstRoomSiteUser);
  }
}
