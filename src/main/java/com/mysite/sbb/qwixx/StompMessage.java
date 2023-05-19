package com.mysite.sbb.qwixx;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StompMessage {
  private Long roomId;

  private String username;
  
  private String actionType;

  private List<String> clickList;
}
