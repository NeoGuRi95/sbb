package com.mysite.sbb.qwixx.dto;

import com.mysite.sbb.qwixx.Participant;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ReadyInfoResponse {
  private String responseType = "readyInfo";
  private List<String> usernames = Arrays.asList("-", "-", "-", "-");
  private List<Boolean> readys = Arrays.asList(null, null, null, null);
  private Boolean isAllReady = true;

  public void setParticipants(List<Participant> participants) {
    for (int i = 0; i < participants.size(); i++) {
      Participant participant = participants.get(i);
      String username = participant.getSiteUser().getUsername();
      usernames.set(i, username);
      readys.set(i, participant.getReady());
      if (participant.getReady() == false) this.isAllReady = false;
    }
  }
}
