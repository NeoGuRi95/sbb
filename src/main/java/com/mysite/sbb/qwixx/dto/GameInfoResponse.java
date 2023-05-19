package com.mysite.sbb.qwixx.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mysite.sbb.qwixx.Participant;

import lombok.Data;

@Data
public class GameInfoResponse {
  private Long roomId;
  //참가자 정보
  private Map<String, List<Integer>> redLineInfo = new HashMap<>();
  private Map<String, List<Integer>> yellowLineInfo = new HashMap<>();
  private Map<String, List<Integer>> greenLineInfo = new HashMap<>();
  private Map<String, List<Integer>> blueLineInfo = new HashMap<>();
  private List<String> usernames = Arrays.asList("-", "-", "-", "-");
  private List<Boolean> readys = Arrays.asList(null, null, null, null);
  private Boolean isAllReady = true;
  private Boolean isFullRoom = false;
  //주사위 정보
  private Integer white1 = 0;
  private Integer white2 = 0;
  private Integer red = 0;
  private Integer yellow = 0;
  private Integer green = 0;
  private Integer blue = 0;
  private Integer whiteSum = 0;
  private Integer redSum1 = 0;
  private Integer redSum2 = 0;
  private Integer yellowSum1 = 0;
  private Integer yellowSum2 = 0;
  private Integer greenSum1 = 0;
  private Integer greenSum2 = 0;
  private Integer blueSum1 = 0;
  private Integer blueSum2 = 0;

  public void setParticipants(List<Participant> participants) {
    for (int i = 0; i < participants.size(); i++) {
      Participant participant = participants.get(i);
      String username = participant.getSiteUser().getUsername();
      usernames.set(i, username);
      readys.set(i, participant.getReady());
      redLineInfo.put(username, participant.getRedLine());
      yellowLineInfo.put(username, participant.getYellowLine());
      greenLineInfo.put(username, participant.getGreenLine());
      blueLineInfo.put(username, participant.getBlueLine());
      if (participant.getReady() == false) this.isAllReady = false;
    }
    if (participants.size() == 4) isFullRoom = true;
  }

  public Boolean isParticipant(String name) {
    for (String username : usernames) {
      if (username.equals(name)) return true;
    }
    return false;
  }
}
