package com.mysite.sbb.qwixx.dto;

import java.util.List;

import com.mysite.sbb.qwixx.RoomSiteUser;

import lombok.Data;

@Data
public class GameResponse {
  private Long roomId;

  private List<RoomSiteUser> participants;

  public void addRoomSiteUser(RoomSiteUser roomSiteUser) {
    participants.add(roomSiteUser);
  }
}
